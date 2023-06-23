package ecs.items;

import ecs.graphic.Animation;
import ecs.items.newItems.Greatsword;
import ecs.items.newItems.InvinciblePotion;
import java.util.List;
import java.util.Random;

/** Generator which creates a random ItemData based on the Templates prepared. */
public class ItemDataGenerator {

    private static final List<String> bookOfRa = List.of("item/drop/BookOfRa/BookOfRa.png");
    private static final List<String> greatsword = List.of("item/drop/Greatsword/greatsword.png");
    private static final List<String> invinciblePotion =
            List.of("item/drop/InvinciblePotion/InvinciblePotion_anim_f0.png");

    private List<ItemData> templates =
            List.of(
                    new Greatsword(
                            ItemType.Passive,
                            new Animation(greatsword, 1),
                            new Animation(greatsword, 1),
                            "Greatsword",
                            "Increases the owners damage by 20"),
                    new InvinciblePotion(
                            ItemType.Active,
                            new Animation(invinciblePotion, 1),
                            new Animation(invinciblePotion, 1),
                            "Invincible Potion",
                            "A Potion which makes you immortal for 5 seconds"));
    private Random rand = new Random();

    /**
     * @return a new randomItemData
     */
    public ItemData generateItemData() {
        return templates.get(rand.nextInt(templates.size()));
    }
}
