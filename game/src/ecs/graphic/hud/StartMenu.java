package ecs.graphic.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;
import starter.Game;
import tools.Constants;
import tools.Point;

/**
 * The class is used to display a start menu graphically. the player should then decide what he
 * exactly wants.
 *
 * @param <T> Typ-Variable
 */
public class StartMenu<T extends Actor> extends ScreenController<T> {
    private ScreenText newGame, continue0, highestScore, quit, dungeon,value;
    private final String hintergrund = "hud/main_background.png";
    private final String leftPath = "character/Knight/idleLeft/knight_m_idle_anim_mirrored_f0.png";
    private final String rightPath = "character/Knight/idleRight/knight_m_idle_anim_f0.png";
    private static final Point hintergrundBild = new Point(0,0);
    private static final Point leftPosition = new Point(240f, 390f);
    private static final Point rightPosition = new Point(355f, 390f);
    private static final Logger logger = Logger.getLogger(StartMenu.class.getName());

    /** Creates a new default Constructor with a new Spritebatch */
    public StartMenu() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and the four options the player can select will also displayed as text
     *
     * @param batch the batch which should be used to draw with
     */
    public StartMenu(SpriteBatch batch) {
        super(batch);
        setupMenu();
    }
    private void setupMenu(){
        ScreenImage<Actor> background = new ScreenImage<>(hintergrund, hintergrundBild);
        background.scaleBy(-1.2f);
        add((T) background);
        logger.log(Level.FINE, "Background loaded.");

        ScreenImage<Actor> screenImage = new ScreenImage<>(leftPath, leftPosition);
        add((T)screenImage);

        ScreenImage<Actor> screenImage1 = new ScreenImage<>(rightPath, rightPosition);
        add((T)screenImage1);

        dungeon =  new ScreenText(
            "Dungeon", new Point(270, 400), 1f);
        dungeon.setColor(Color.WHITE);
        dungeon.setFontScale(1.5f);
        add((T) dungeon);
        logger.log(Level.FINE, "Dungeon ScreenText initialized.");

        newGame =
            new ScreenText(
                "New Game", new Point((float) Constants.WINDOW_WIDTH / 2 - 60, 250), 1f);
        newGame.setColor(Color.WHITE);
        newGame.setFontScale(1.5f);
        add((T) newGame);
        logger.log(Level.FINE, "New Game ScreenText initialized.");

        continue0 =
            new ScreenText(
                "Continue",
                new Point((float) Constants.WINDOW_WIDTH / 2 - 60, 200),
                1f);
        continue0.setColor(Color.WHITE);
        continue0.setFontScale(1.5f);
        add((T) continue0);
        logger.log(Level.FINE, "Continue ScreenText initialized.");

        highestScore =   new ScreenText(
            "Highest Score",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 60, 150),
            1f);
        highestScore.setColor(Color.WHITE);
        highestScore.setFontScale(1.5f);
        add((T) highestScore);
        logger.log(Level.FINE, "Highest Score ScreenText initialized.");

        quit =   new ScreenText(
            "Exit",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 60, 100),
            1f);
        quit.setColor(Color.WHITE);
        quit.setFontScale(1.5f);
        add((T) quit);
        logger.log(Level.FINE, "Quit ScreenText initialized");
    }

    /**
     * the player will select what exactly he wants to do with the appropriate key
     */
    @Override
    public void update() {
        super.update();
           //New Game
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            Game.setGameStart(true);
            Game.restartGame();
            hideMenu();
            logger.log(Level.SEVERE, "Start new game.");
        }
        // Continue
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            Game.setGameStart(true);
            hideMenu();
            logger.log(Level.SEVERE, "Start new game.");
        }
        // Highest Score.
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)){
         Game.setGameStart(false);
         screenHighestScore();
        }
        // Exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            hideMenu();
            System.exit(0);
        }
    }
    private void screenHighestScore(){
        value =  new ScreenText(
            String.valueOf(Game.getHighestScore()), new Point(450f, 150f), 1f);
        value.setColor(Color.WHITE);
        value.setFontScale(1.5f);
        add((T) value);
        logger.log(Level.FINE, "The Highest Score ScreenText initialized.");
    }

    /** Shows the Start Menu */
    public void showMenu() {
        logger.info("Start Menu will be displayed");
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** Hide the Start Menu */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

}

/*


        ScreenText screenText =
                new ScreenText(
                        "Dungeon",
                        new Point(0, 0),
                        3,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.BLUE)
                                .build());
        screenText.setFontScale(3);
        screenText.setPosition(70f, 180f, Align.center | Align.bottom);


 ScreenButton newGame =
                new ScreenButton(
                        "New Game",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {

                               // Game.setGameStart(true);
                                logger.log(Level.SEVERE, "New Game Started");
                                hideMenu();
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        newGame.setPosition((Constants.WINDOW_WIDTH) / 2f, 250, Align.center | Align.bottom);

        ScreenButton exit =
                new ScreenButton(
                        "Exit",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Game.restartGame();
                                System.exit(0);
                                logger.log(Level.SEVERE, "Closed Game");
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        exit.setScale(3);
        exit.setPosition((Constants.WINDOW_WIDTH) / 2f, 150, Align.center | Align.bottom);

        ScreenButton highestScore =
                new ScreenButton(
                        "Highest Score",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                ScreenText screenText2 =
                                        new ScreenText(
                                                String.valueOf(Game.getHighestScore()),
                                                new Point(0, 0),
                                                3,
                                                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                                        .setFontcolor(Color.GOLD)
                                                        .build());
                                screenText2.setFontScale(3);
                                screenText2.setPosition(270, 300, Align.center | Align.bottom);

                                logger.log(Level.SEVERE, "Highest Score will be displayed");
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        highestScore.setScale(10);
        highestScore.setPosition((Constants.WINDOW_WIDTH) / 2f, 200, Align.center | Align.bottom);

        ScreenButton continue1 =
                new ScreenButton(
                        "Continue",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Game.setGameStart(true);
                                hideMenu();
                                logger.log(Level.SEVERE, "Continue the Game.");
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        continue1.setScale(3);
        continue1.setPosition((Constants.WINDOW_WIDTH) / 2f, 300, Align.center | Align.bottom);






 */
