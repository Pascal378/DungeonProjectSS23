package dslToGame;

import ecs.graphic.Animation;
import ecs.graphic.textures.TextureHandler;
import semanticAnalysis.types.DSLTypeAdapter;

public class AnimationBuilder {
    public static int frameTime = 5;

    @DSLTypeAdapter(t = Animation.class)
    public static Animation buildAnimation(String path) {
        return new Animation(TextureHandler.getInstance().getTexturePaths(path), frameTime);
    }
}
