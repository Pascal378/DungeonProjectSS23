package ecs.graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class is used to display a start menu graphically.
 * the player should then decide what he exactly wants.
 * @param <T> a data typ
 */
public class StartMenu <T extends Actor> extends ScreenController<T> {
    private final String leftPath = "character/Knight/idleLeft/knight_m_idle_anim_mirrored_f0.png";
    private final String rightPath = "character/Knight/idleRight/knight_m_idle_anim_f0.png";
    private static final Point leftPosition = new Point(210f, 390f);
    private static final Point rightPosition = new Point(415f, 390f);
    private static final Logger logger = Logger.getLogger(StartMenu.class.getName());

    /** Creates a new default Constructor with a new Spritebatch */
    public StartMenu() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public StartMenu(SpriteBatch batch) {
        super(batch);
        ScreenImage<Actor> screenImage = new ScreenImage<>(leftPath, leftPosition);
        ScreenImage<Actor> screenImage1 = new ScreenImage<>(rightPath, rightPosition);

        ScreenText screenText =
            new ScreenText(
                "Dungeon",
                new Point(0, 0),
                3,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.BLUE)
                    .build());
        screenText.setFontScale(3);
        screenText.setPosition(
            270,
            400,
            Align.center | Align.bottom);

        ScreenButton newGame =
            new ScreenButton(
                "New Game",
                new Point(0f, 0f),
                new TextButtonListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Game.restartGame();
                        logger.log(Level.SEVERE, "New Game Started");
                        hideMenu();
                    }
                },
                new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontColor(Color.WHITE)
                    .setOverFontColor(Color.RED)
                    .build());
             newGame.setPosition(
                 (Constants.WINDOW_WIDTH) / 2f,
            250,
            Align.center | Align.bottom);

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
        exit.setPosition(
            (Constants.WINDOW_WIDTH) / 2f ,
            150,
            Align.center | Align.bottom);

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
                        screenText2.setPosition(
                            270,
                            300,
                            Align.center | Align.bottom);

                        logger.log(Level.SEVERE, "Highest Score will be displayed");
                    }
                },
                new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontColor(Color.WHITE)
                    .setOverFontColor(Color.RED)
                    .build());
        highestScore.setScale(10);
        highestScore.setPosition(
            (Constants.WINDOW_WIDTH) / 2f,
            200,
            Align.center | Align.bottom);

        ScreenButton continue1 =
            new ScreenButton(
                "Continue",
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
        continue1.setScale(3);
        continue1.setPosition(
            (Constants.WINDOW_WIDTH) / 2f,
            300,
            Align.center | Align.bottom);



        add((T) screenImage1);
        add((T) screenImage);
        add((T) screenText);
        add((T) newGame);
        add((T) highestScore);
        add((T)continue1);
        add((T) exit);
        showMenu();
    }

    /** Shows the Start Menu  */
    public void showMenu() {
        logger.info("Start Menu will be displayed");
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** Hide the Start Menu  */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}

