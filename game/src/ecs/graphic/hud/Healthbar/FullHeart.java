package ecs.graphic.hud.Healthbar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.graphic.hud.ScreenImage;
import java.util.logging.Logger;
import tools.Point;

/**
 * The class is used to display a Full-Heart image graphically, when the health points of the hero
 * are greater than 50% of his maximal health points.
 *
 * @param <T> a data typ
 */
public class FullHeart<T extends Actor> extends ScreenController<T> {
    private static final Logger logger = Logger.getLogger(FullHeart.class.getName());
    private static final String texturePath = "hud/ui_heart_full.png";
    private static final Point position = new Point(3f, 429f);
    private static ScreenImage screenImage;

    /** Creates a new default Constructor with a new Spritebatch */
    public FullHeart() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and an image for the UI will be also created.
     *
     * @param batch the batch which should be used to draw with.
     */
    public FullHeart(SpriteBatch batch) {
        super(batch);
        screenImage = new ScreenImage<>(texturePath, position);
        add((T) screenImage);
    }
    /** shows the Image */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
        logger.info("Created Full Heart");
    }
    /** hides the Image */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
