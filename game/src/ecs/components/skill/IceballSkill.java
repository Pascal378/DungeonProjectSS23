package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import tools.Point;

public class IceballSkill extends DamageProjectileSkill {
    public IceballSkill (ITargetSelection targetSelection) {
        super(
            "skills/iceball/iceBall_Down/",
            0.1f,
            new Damage(3, DamageType.ICE, null),
            new Point(10, 10),
            targetSelection,
            5f);
    }
}
