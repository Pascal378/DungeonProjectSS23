package ecs.graphic.hud.Healthbar;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.graphic.hud.ScreenImage;
import starter.Game;
import tools.Point;
import java.util.logging.Logger;

/**
 * The class is used to display a Full-Heart image graphically,
 * when the health points of the hero are greater than 50% of his maximal health points.
 * @param <T> a data typ
 */
public class FullHeart <T extends Actor> extends ScreenController<T> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private static final String texturePath = "hud/ui_heart_full.png";
    private static final Point position = new Point(3f,429f);

    /** Creates a new default Constructor with a new Spritebatch */
    public FullHeart() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and an image for the UI will be also created.
     * @param batch the batch which should be used to draw with.
     */
    public FullHeart(SpriteBatch batch) {
        super(batch);
        screenImage = new ScreenImage<>(texturePath, position);
        add((T) screenImage);
    }
}
