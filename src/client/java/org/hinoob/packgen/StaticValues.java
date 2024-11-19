package org.hinoob.packgen;

import net.minecraft.client.gui.screen.pack.PackListWidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StaticValues {

    // Yes this is a mess

    public static boolean WAITING_FOR_RESOURCE_PACK = false;
    public static String RESOURCE_PACK_NAME = "";

    public static PackListWidget AVAILABLE, SELECTED;
    public static List<File> toDelete = new ArrayList<>();
}
