//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame;

import com.github.tiwindetea.raoulthegame.model.MainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Settings.
 *
 * @author Lucas LAZARE
 */
public final class Settings {

    /**
     * Path to the config file
     */
    public static final String CONFIG_PATH = ResourceBundle.getBundle(MainPackage.name + ".OutputFiles").getString("settings-file");

    /**
     * Path to the save file
     */
    public static final String SAVE_PATH = ResourceBundle.getBundle(MainPackage.name + ".OutputFiles").getString("save-file");

    /**
     * Setting for the basic auto equip feature (enabled or disabled)
     */
    public static boolean simpleAutoEquip = true;

    /**
     * Setting for the advanced auto equip feature (enabled or disabled)
     */
    public static boolean autoEquip = false;

    /**
     * Setting for the advanced auto equip feature (true if it should drop items on floor when the inventory is full)
     */
    public static boolean autoEquipCanDrop = false;

    /**
     * Setting for the perma-death feature (ie: deletion of the save file upon death) (enabled or disabled)
     */
    public static boolean permaDeath = false;

    /**
     * True if mouse clicks should be inverted (ie: left click behaves as right click and vice-versa
     */
    public static boolean mouseInverted = false;

    /**
     * True if the intro should be skipped
     */
    public static boolean skipIntro = false;

    /**
     * The constant difficulty.
     */
    public static double difficulty = 0.5;

    /**
     * loads settings from the config file
     */
    public static void loadSettings() {
        try {
            Scanner file = new Scanner(new FileInputStream(Settings.CONFIG_PATH));
            int i;
            while (file.hasNext()) {
                String str = file.nextLine();
                i = str.indexOf('=');
                if (i > 0) {
                    try {
                        switch (str.substring(0, i)) {
                            case "simpleAutoEquip":
                                Settings.simpleAutoEquip = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "autoEquip":
                                Settings.autoEquip = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "autoEquipCanDrop":
                                Settings.autoEquipCanDrop = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "permaDeath":
                                Settings.permaDeath = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "mouseInverted":
                                Settings.mouseInverted = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "skipIntro":
                                Settings.skipIntro = Boolean.parseBoolean(str.substring(i + 1));
                                break;
                            case "difficulty":
                                Settings.difficulty = Double.parseDouble(str.substring(i + 1));
                                break;
                            default:
                                System.out.println("Unexpected setting: " + str);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Unexpected setting's value: " + str);
                    }
                } else {
                    System.out.println("Unexpected setting's format: " + str);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("Config file unreadable (" + CONFIG_PATH + ")");
        }
    }

    /**
     * Saves the settings to the config file.
     */
    public static void saveSettings() {
        try {
            FileWriter file = new FileWriter(new File(Settings.CONFIG_PATH));
            file.write("simpleAutoEquip=" + simpleAutoEquip + "\n");
            file.write("autoEquip=" + autoEquip + "\n");
            file.write("autoEquipCanDrop=" + autoEquipCanDrop + "\n");
            file.write("permaDeath=" + permaDeath + "\n");
            file.write("mouseInverted=" + mouseInverted + "\n");
            file.write("skipIntro=" + skipIntro + "\n");
            file.write("difficulty=" + difficulty + "\n");
            file.close();
        } catch (IOException e) {
            System.out.println("Config file unwriteable (" + CONFIG_PATH + ")");
        }
    }

    /**
     * Why would you instantiate this class anyway ?
     */
    private Settings() {
    }
}
