package ecs.graphic.hud.Healthbar;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.graphic.hud.ScreenImage;
import starter.Game;
import tools.Point;
import java.util.logging.Logger;
/**
 * The class is used to display an Empty-Heart image graphically,
 * when the health points of the hero are Nearly empty.
 * @param <T> a data typ.
 */
public class EmptyHeart <T extends Actor> extends ScreenController<T> {

    private static final String texturePath = "hud/ui_heart_empty.png";
    private static final Point position = new Point(3f,429f);
    private final Logger logger = Logger.getLogger(getClass().getName());

    /** Creates a new default Constructor with a new Spritebatch */
    public EmptyHeart(){
        this(new SpriteBatch());
    }
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize,and an image for the UI will be also created.
     * @param batch the batch which should be used to draw with.
     */
    public EmptyHeart(SpriteBatch batch) {
        super(batch);
        ScreenImage<T> screenImage = new ScreenImage<>(texturePath, position);
        if (Game.getPlayHero().getCurrentHealth() <= 10){
            add((T) screenImage);
            logger.info("the Emptyheart was displayed");
        }
    }
}
