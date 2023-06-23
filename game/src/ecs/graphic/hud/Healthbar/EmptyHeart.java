package ecs.graphic.hud.Healthbar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.graphic.hud.ScreenImage;
import java.util.logging.Logger;
import tools.Point;

/**
 * The class is used to display an Empty-Heart image graphically, when the health points of the hero
 * are Nearly empty.
 *
 * @param <T> a data typ.
 */
public class EmptyHeart<T extends Actor> extends ScreenController<T> {
    private static ScreenImage screenImage;
    private static final String texturePath = "hud/ui_heart_empty.png";
    private static final Point position = new Point(3f, 429f);
    private static final Logger logger = Logger.getLogger(EmptyHeart.class.getName());

    /** Creates a new default Constructor with a new Spritebatch */
    public EmptyHeart() {
        this(new SpriteBatch());
    }
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and an image for the UI will be also created.
     *
     * @param batch the batch which should be used to draw with.
     */
    public EmptyHeart(SpriteBatch batch) {
        super(batch);
        screenImage = new ScreenImage<>(texturePath, position);
        add((T) screenImage);
        hideMenu();
    }
    /** shows the Image */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
        logger.info("Created Empty Heart");
    }
    /** hides the Image */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
