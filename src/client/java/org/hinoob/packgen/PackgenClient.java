package org.hinoob.packgen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import joptsimple.OptionSet;
import net.fabricmc.api.ClientModInitializer;
import net.hypixel.resourcepack.MinecraftVersion;
import net.hypixel.resourcepack.Options;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class PackgenClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        System.out.println("Packgen initialized");
        for(File f : new File(".").listFiles()) {
            if(f.getName().endsWith("_tmp")) {
                FileUtils.delete(new File("resourcepacks", f.getName().replace("_tmp", "_new")));
                FileUtils.cut(f, new File("resourcepacks", f.getName().replace("_tmp", "")));
            }
        }
    }
}
