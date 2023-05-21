package ecs.graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import starter.Game;
import tools.Constants;
import tools.Point;

public class GameOverHUD<T extends Actor> extends ScreenController<T> {

    /** Creates a new PauseMenu with a new Spritebatch */
    public GameOverHUD() {
        this(new SpriteBatch());
    }
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public GameOverHUD(SpriteBatch batch) {
        super(batch);
        ScreenText screenText =
                new ScreenText(
                        "Game Over",
                        new Point(0, 0),
                        3,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.RED)
                                .build());
        screenText.setFontScale(4);
        screenText.setPosition(
                (Constants.WINDOW_WIDTH) / 2f - screenText.getWidth() - 30,
                (Constants.WINDOW_HEIGHT) / 1.5f + screenText.getHeight(),
                Align.center | Align.bottom);

        ScreenButton newGame =
                new ScreenButton(
                        "New Game",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                // Neues level laden
                                Game.restartGame();
                                System.out.println("Restart");
                                hideMenu();
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        newGame.setPosition(
                (Constants.WINDOW_WIDTH) / 2f - newGame.getWidth()+20,
                (Constants.WINDOW_HEIGHT) / 2f + newGame.getHeight(),
                Align.center | Align.bottom);

        ScreenButton quit =
                new ScreenButton(
                        "Quit",
                        new Point(0f, 0f),
                        new TextButtonListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                System.exit(0);
                                System.out.println("System Exit");
                            }
                        },
                        new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontColor(Color.WHITE)
                                .setOverFontColor(Color.RED)
                                .build());
        quit.setScale(3);
        quit.setPosition(
                (Constants.WINDOW_WIDTH) / 2f + quit.getWidth()+20,
                (Constants.WINDOW_HEIGHT) / 2f + quit.getHeight(),
                Align.center | Align.bottom);

        add((T) newGame);
        add((T) quit);
        add((T) screenText);

        hideMenu();
    }

    /** shows the Game Over screen */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }
    /** hide the Game Over screen */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
