package ecs.graphic.hud.Healthbar;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.graphic.hud.ScreenImage;
import starter.Game;
import tools.Point;
import java.util.logging.Logger;
/**
 * The class is used to display a Half-Heart image graphically,
 * when the health points of the hero are less than 50% of his maximal health points.
 * @param <T> a data typ
 */
public class HalfHeart <T extends Actor> extends ScreenController<T> {
    private static final Logger logger = Logger.getLogger(HalfHeart.class.getName());
    private static final String texturePath1 = "hud/ui_heart_half.png";
    private static final Point position = new Point(3f,429f);
    private  static ScreenImage screenImage;

    /** Creates a new default Constructor with a new Spritebatch */
    public HalfHeart(){
        this(new SpriteBatch());
    }
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and an image for the UI will be also created.
     * @param batch the batch which should be used to draw with.
     */
    public HalfHeart(SpriteBatch batch) {
        super(batch);
        screenImage = new ScreenImage<>(texturePath1, position);
        add((T) screenImage);
        hideMenu();
    }
}
