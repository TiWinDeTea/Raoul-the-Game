package com.github.tiwindetea.dungeonoflegend.model;

/**
 * Created by organic-code on 5/10/16.
 */
public class Tester {
    public static void main(String[] args) {
        Player player = new Player("Roger", 10, 5, 100, 100, 5, 3, 7, 2, 1, 2, 5);
        player.addToInventory(new Pair<>(new Pot(10, 20, 30, 40, 50, 60, 70)));
        player.addToInventory(new Pair<>(new Scroll(10, 20, 30)));
        player.addToInventory(new Pair<>(new Weapon(3, 4, 5)));
        player.addToInventory(new Pair<>(new Armor(2, 1, ArmorType.BOOTS)));
        player.addToInventory(new Pair<>(new Armor(10, 0, ArmorType.BREAST_PLATE)));

        player.equipWithWeapon(new Pair<>(new Weapon(3, 1, 0)));
        player.equipWithArmor(new Pair<>(new Armor(10, 10, ArmorType.BOOTS)));

        System.out.println(player);
        System.out.println(Player.parsePlayer(player.toString()));
    }
}
