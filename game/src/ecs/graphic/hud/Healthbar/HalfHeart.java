package ecs.graphic.hud.Healthbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import ecs.graphic.hud.ScreenImage;
import tools.Point;

public class HalfHeart <T extends Actor> extends ScreenImage {
    private static final String texturePath1 = "hud/ui_heart_half.png";
    private static final Point position = new Point(0f,0f);

    /**
     * Creates an Image for the UI
     *
     * @param texturePath the Path to the Texture
     * @param position    the Position where the Image should be drawn
     */
    public HalfHeart(String texturePath, Point position) {
        super(texturePath, position);
    }

    public HalfHeart() {
        super(texturePath1,position);
    }
}
