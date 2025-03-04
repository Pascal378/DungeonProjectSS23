package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.SystemController;
import ecs.components.InventoryComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Friendly.FriendlyGhost;
import ecs.entities.Friendly.Hero;
import ecs.entities.Friendly.PreMonsterChest;
import ecs.entities.Monsters.BossMonster;
import ecs.entities.Monsters.Demon;
import ecs.entities.Monsters.Imp;
import ecs.entities.Monsters.Slime;
import ecs.entities.Traps.BearTrap;
import ecs.entities.Traps.Mine;
import ecs.graphic.DungeonCamera;
import ecs.graphic.Painter;
import ecs.graphic.hud.GameOverHUD;
import ecs.graphic.hud.Healthbar.EmptyHeart;
import ecs.graphic.hud.Healthbar.FullHeart;
import ecs.graphic.hud.Healthbar.HalfHeart;
import ecs.graphic.hud.InventoryHUD;
import ecs.graphic.hud.PauseMenu;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.items.newItems.Bag;
import ecs.items.newItems.BookOfRa;
import ecs.items.newItems.Greatsword;
import ecs.items.newItems.InvinciblePotion;
import ecs.systems.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import level.IOnLevelLoader;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.tools.LevelElement;
import level.tools.LevelSize;
import tools.Constants;
import tools.Point;

/** The heart of the framework. From here all strings are pulled. */
public class Game extends ScreenAdapter implements IOnLevelLoader {
    private LevelSize levelSize = LevelSize.SMALL;

    /**
     * The batch is necessary to draw ALL the stuff. Every object that uses draw need to know the
     * batch.
     */
    protected SpriteBatch batch;

    private static Game game;

    /** Contains all Controller of the Dungeon */
    protected List<AbstractController<?>> controller;

    public static DungeonCamera camera;
    /** Draws objects */
    protected Painter painter;

    protected LevelAPI levelAPI;
    /** Generates the level */
    protected IGenerator generator;

    private boolean doSetup = true;
    private static boolean paused = false;

    /** All entities that are currently active in the dungeon */
    private static final Set<Entity> entities = new HashSet<>();
    /** All entities to be removed from the dungeon in the next frame */
    private static final Set<Entity> entitiesToRemove = new HashSet<>();
    /** All entities to be added from the dungeon in the next frame */
    private static final Set<Entity> entitiesToAdd = new HashSet<>();

    /** List of all Systems in the ECS */
    public static SystemController systems;

    public static ILevel currentLevel;
    private static PauseMenu<Actor> pauseMenu;
    private static InventoryHUD<Actor> inventoryHUD;
    private static GameOverHUD<Actor> gameOverHUD;
    private static FullHeart<Actor> fullHeart;
    private static HalfHeart<Actor> halfHeart;
    private static EmptyHeart<Actor> emptyHeart;
    private static boolean inventoryOpen = false;
    private static Entity hero;
    private static Hero playHero;
    private Logger gameLogger;
    private static int currentLvl = 0;
    private FriendlyGhost friendlyGhost;
    private SaveGame saveGame;
    private boolean onceLoaded = false;

    public static void main(String[] args) {
        // start the game
        try {
            Configuration.loadAndGetConfiguration("dungeon_config.json", KeyboardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DesktopLauncher.run(game = new Game());
    }

    /**
     * Main game loop. Redraws the dungeon and calls the own implementation (beginFrame, endFrame
     * and onLevelLoad).
     *
     * @param delta Time since last loop.
     */
    @Override
    public void render(float delta) {
        if (doSetup) setup();
        batch.setProjectionMatrix(camera.combined);
        frame();
        clearScreen();
        levelAPI.update();
        controller.forEach(AbstractController::update);
        camera.update();
    }

    /** Called once at the beginning of the game. */
    protected void setup() {
        playHero = new Hero();
        hero = playHero;
        this.saveGame = new SaveGame();
        doSetup = false;
        controller = new ArrayList<>();
        setupCameras();
        painter = new Painter(batch, camera);
        generator = new RandomWalkGenerator();
        levelAPI = new LevelAPI(batch, painter, generator, this);
        initBaseLogger();
        gameLogger = Logger.getLogger(this.getClass().getName());
        systems = new SystemController();
        controller.add(systems);
        inventoryHUD = new InventoryHUD<>();
        controller.add(inventoryHUD);
        pauseMenu = new PauseMenu<>();
        controller.add(pauseMenu);
        emptyHeart = new EmptyHeart<>();
        controller.add(emptyHeart);
        halfHeart = new HalfHeart<>();
        controller.add(halfHeart);
        fullHeart = new FullHeart<>();
        controller.add(fullHeart);
        gameOverHUD = new GameOverHUD<>();
        controller.add(gameOverHUD);
        levelAPI = new LevelAPI(batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
        levelAPI.loadLevel(levelSize);
        createSystems();
    }

    /** Called at the beginning of each frame. Before the controllers call <code>update</code>. */
    protected void frame() {
        setCameraFocus();
        manageEntitiesSets();
        getHero().ifPresent(this::loadNextLevelIfEntityIsOnEndTile);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) togglePause();
    }

    public boolean checkSave() {
        if (new File("SaveFile.ser").exists() && currentLvl == 0) {
            entities.clear();
            saveGame.readSave();
            onceLoaded = true;
            return true;
        }
        return false;
    }

    @Override
    public void onLevelLoad() {
        currentLevel = levelAPI.getCurrentLevel();
        entities.clear();
        getHero().ifPresent(this::placeOnLevelStart);
        loadGhost();
        spawnChest();
        spawnMonster();
        new Mine();
        new BearTrap();

        spawnBoss();
        spawnItems();
        currentLvl++;
        bookCheck();
        Hero hero1 = (Hero) Game.hero;
        hero1.getXpCmp().addXP(hero1.getXpCmp().getXPToNextLevel());
        gameLogger.info("Current Level: " + currentLvl);
        saveGame.writeSave();
    }

    /** Spawn ghost, there is a 10% chance it doesn't spawn */
    private void loadGhost() {
        Random random = new Random();
        if (random.nextInt(0, 100) > 10) friendlyGhost = new FriendlyGhost(playHero);
    }

    private void setLevelSize(int currentLvl) {
        if (currentLvl >= 5) levelSize = LevelSize.MEDIUM;
        if (currentLvl >= 10) levelSize = LevelSize.LARGE;
    }

    /** Chance of Randomly spawn a Item */
    private void spawnItems() {
        int random = (int) (Math.random() * (100));
        if (random >= 0 && random <= 25) {
            new Bag(ItemType.Active);
        }
        if (random >= 25 && random <= 50) {
            new Greatsword();
        }
        if (random >= 50 && random <= 85) {
            new BookOfRa();
        }
        if (random >= 85 && random <= 90) {
            new Bag(ItemType.Active);
            new InvinciblePotion();
        }
    }

    /** Spawns monster in relation to current level progress */
    private void spawnMonster() {
        Random random = new Random();

        int monster = 0;

        for (int i = 0; i < ((currentLvl) + 1); i++) {

            int rng = random.nextInt(0, 3);

            if (rng == 0) new Imp(currentLvl);
            if (rng == 1) new Demon(currentLvl);
            if (rng == 2) new Slime(currentLvl);

            monster++;
        }
        gameLogger.info("Amount of monsters spawned in this level: " + monster);
    }

    private void spawnChest() {
        List<ItemData> items = new ArrayList<>();
        new PreMonsterChest(
                items,
                Game.currentLevel.getRandomTile(LevelElement.FLOOR).getCoordinate().toPoint());
    }

    private void spawnBoss() {
        if (currentLvl == 2) {
            new BossMonster(playHero);
            gameLogger.info("Boss Monster spawnt");
        } else {
            spawnMonster();
        }
    }

    public void bookCheck() {
        Hero worker = (Hero) hero;
        InventoryComponent inv = worker.getInv();
        BookOfRa books;

        for (ItemData item : inv.getItems()) {

            // Check for book bags
            if (item instanceof Bag) {
                for (ItemData book : ((Bag) item).getItems()) {
                    books = (BookOfRa) book;
                    books.grantXP();
                }
            }

            // Check for books
            if (item instanceof BookOfRa) {
                ((BookOfRa) item).grantXP();
            }
        }
    }

    private void manageEntitiesSets() {
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        for (Entity entity : entitiesToRemove) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was deleted.");
        }
        for (Entity entity : entitiesToAdd) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was added.");
        }
        entitiesToRemove.clear();
        entitiesToAdd.clear();
    }

    private void setCameraFocus() {
        if (getHero().isPresent()) {
            PositionComponent pc =
                    (PositionComponent)
                            getHero()
                                    .get()
                                    .getComponent(PositionComponent.class)
                                    .orElseThrow(
                                            () ->
                                                    new MissingComponentException(
                                                            "PositionComponent "));
            camera.setFocusPoint(pc.getPosition());

        } else camera.setFocusPoint(new Point(0, 0));
    }

    private void loadNextLevelIfEntityIsOnEndTile(Entity hero) {
        if (isOnEndTile(hero)) {
            setLevelSize(currentLvl);
            levelAPI.loadLevel(levelSize);
        }
    }

    private boolean isOnEndTile(Entity entity) {
        PositionComponent pc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        Tile currentTile = currentLevel.getTileAt(pc.getPosition().toCoordinate());
        return currentTile.equals(currentLevel.getEndTile());
    }

    private void placeOnLevelStart(Entity hero) {
        entities.add(hero);
        PositionComponent pc =
                (PositionComponent)
                        hero.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        pc.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());
    }

    /**
     * <<<<<<< HEAD the health points will be checked and then the appropriate image will be shown.
     *
     * @param amount The health points of the Hero.
     */
    public static void updateHeartBar(int amount) {
        if (amount < 51 && amount > 10) {
            fullHeart.hideMenu();
            emptyHeart.hideMenu();
            halfHeart.showMenu();
        } else if (amount <= 10) {
            fullHeart.hideMenu();
            halfHeart.hideMenu();
            emptyHeart.showMenu();

        } else {
            halfHeart.hideMenu();
            emptyHeart.hideMenu();
            fullHeart.showMenu();
        }
    }

    /** ======= >>>>>>> Feature/ChestMonster Toggle between pause and run */
    public static void togglePause() {
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (pauseMenu != null) {
            if (paused) pauseMenu.showMenu();
            else pauseMenu.hideMenu();
        }
    }

    /** Open Inventory */
    public static void openInventory() {
        inventoryOpen = !inventoryOpen;
        if (inventoryHUD != null) {
            if (inventoryOpen) inventoryHUD.showMenu();
            else inventoryHUD.hideMenu();
        }
    }

    /**
     * Returns the GameOverMenuObject
     *
     * @return GameOverMenuObject
     */
    public static GameOverHUD getGameOverMenu() {
        return gameOverHUD;
    }

    /**
     * Restarts the game
     *
     * <p>Used for the "Restart"-Function of the GameOverMenu. Creates a new level and resets all
     * important parameters.
     */
    public static void restartGame() {
        currentLvl = 0;
        game.setup();
    }

    /**
     * Given entity will be added to the game in the next frame
     *
     * @param entity will be added to the game next frame
     */
    public static void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    /**
     * Given entity will be removed from the game in the next frame
     *
     * @param entity will be removed from the game next frame
     */
    public static void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    /**
     * @return Set with all entities currently in game
     */
    public static Set<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Set with all entities that will be added to the game next frame
     */
    public static Set<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
    }

    /**
     * @return Set with all entities that will be removed from the game next frame
     */
    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * @return the player character, can be null if not initialized
     */
    public static Optional<Entity> getHero() {
        return Optional.ofNullable(hero);
    }

    /**
     * set the reference of the playable character careful: old hero will not be removed from the
     * game
     *
     * @param hero new reference of hero
     */
    public static void setHero(Entity hero) {
        Game.hero = hero;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void setupCameras() {
        camera = new DungeonCamera(null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.zoom = Constants.DEFAULT_ZOOM_FACTOR;

        // See also:
        // https://stackoverflow.com/questions/52011592/libgdx-set-ortho-camera
    }

    public static int getCurrentLvl() {
        return currentLvl;
    }

    public static void setCurrentLvl(int currentLvl) {
        Game.currentLvl = currentLvl;
    }

    private void createSystems() {
        new VelocitySystem();
        new DrawSystem(painter);
        new PlayerSystem();
        new AISystem();
        new CollisionSystem();
        new HealthSystem();
        new XPSystem();
        new SkillSystem();
        new ProjectileSystem();
        new QuestSystem();
    }
}
