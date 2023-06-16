package ecs.items;

import ecs.graphic.Animation;
import java.util.List;
import java.util.Random;

/** Generator which creates a random ItemData based on the Templates prepared. */
public class ItemDataGenerator {
    //private static final List<String> missingTexture = List.of("animation/missingTexture.png");

    private static final List<String> bookOfRa = List.of("item/world/BookOfRa/BookOfRa.png");
    private static final List<String> greatsword = List.of("item/world/Greatsword/greatsword.png");
    private static final List<String> invinciblePotion = List.of("item/world/InvinciblePotion/InvinciblePotion_anim_f0.png");


    private List<ItemData> templates =
            List.of(
                    new ItemData(
                            ItemType.Basic,
                            new Animation(bookOfRa, 1),
                            new Animation(bookOfRa, 1),
                            "Buch",
                            "Ein sehr lehrreiches Buch."),
                    new ItemData(
                            ItemType.Basic,
                            new Animation(greatsword, 1),
                            new Animation(greatsword, 1),
                            "Tuch",
                            "Ein sauberes Tuch.."),
                    new ItemData(
                            ItemType.Basic,
                            new Animation(invinciblePotion, 1),
                            new Animation(invinciblePotion, 1),
                            "Namensschild",
                            "Ein Namensschild wo der Name nicht mehr lesbar ist.."));
    private Random rand = new Random();

    /**
     * @return a new randomItemData
     */
    public ItemData generateItemData() {
        return templates.get(rand.nextInt(templates.size()));
    }
}
