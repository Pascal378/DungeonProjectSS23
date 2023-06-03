package ecs.components.ai.idle;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.entities.Monsters.BossMonster;
import level.elements.ILevel;
import level.elements.TileLevel;
import level.elements.tile.ExitTile;
import level.elements.tile.Tile;
import level.tools.LevelElement;
import level.tools.TileTextureFactory;
import starter.Game;
import tools.Constants;
import tools.Point;

import static ecs.components.ai.AITools.getRandomAccessibleTileCoordinateInRange;

public class BossWalk implements IIdleAI {
    private final float radius;
    private GraphPath<Tile> path;
    private final int breakTime;
    private int currentBreak = 0;
    private Point center;
    private Point currentPosition;
    private Point newEndTile;
    private Entity hero = Game.getHero().get();



    /**
     * Finds a point in the radius and then moves there. When the point has been reached, a new
     * point in the radius is searched for from the center.
     *
     * @param radius Radius in which a target point is to be searched for
     * @param breakTimeInSeconds how long to wait (in seconds) before searching a new goal
     */
    public BossWalk(float radius, int breakTimeInSeconds) {
        this.radius = radius;
        this.breakTime = breakTimeInSeconds * Constants.FRAME_RATE;
    }

    @Override
    public void idle(Entity entity) {



    }
}
