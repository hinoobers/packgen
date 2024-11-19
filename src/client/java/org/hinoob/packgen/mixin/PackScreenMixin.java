package org.hinoob.packgen.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackProfile;
import org.hinoob.packgen.FileUtils;
import org.hinoob.packgen.StaticValues;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

@Mixin(PackScreen.class)
public class PackScreenMixin {

    @Shadow private PackListWidget availablePackList;

    @Shadow @Final private ResourcePackOrganizer organizer;

    @Shadow private PackListWidget selectedPackList;

    @Inject(at = @At("TAIL"), method = "updatePackList")
    public void a(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        try {
            for(File f : StaticValues.toDelete) {
                FileUtils.delete(f);
            }

            StaticValues.toDelete.clear();
        } catch(Exception ignored) {

        }

        // create a copy of packs
        if(StaticValues.WAITING_FOR_RESOURCE_PACK) {
            for(PackListWidget.ResourcePackEntry p : this.availablePackList.children()) {
                if(p.getName().equals(StaticValues.RESOURCE_PACK_NAME)) {
                    StaticValues.WAITING_FOR_RESOURCE_PACK = false;
                    p.toggle();

                    File f = new File("resourcepacks", p.getName().split("/")[1].replaceAll("_new", ""));
                    FileUtils.cut(f, new File(f.getName() + "_tmp"));
                    break;
                }
            }
        }

        if(StaticValues.AVAILABLE != null) {
            for(PackListWidget.ResourcePackEntry p : this.availablePackList.children()) {
                if(p.getName().endsWith("_new") && Arrays.stream(new File(".").listFiles()).anyMatch(s -> s.getName().endsWith("_tmp"))) {
                    // Move tmp file back to resourcepacks and delete the new file

                    for(File f : new File(".").listFiles()) {
                        if(f.getName().endsWith("_tmp")) {
                            StaticValues.toDelete.add(new File("resourcepacks", f.getName().replace("_tmp", "_new")));
                            FileUtils.cut(f, new File("resourcepacks", f.getName().replace("_tmp", "")));
                        }
                    }
                }
            }
        }

        StaticValues.AVAILABLE = this.availablePackList;
        StaticValues.SELECTED = this.selectedPackList;
    }
}
