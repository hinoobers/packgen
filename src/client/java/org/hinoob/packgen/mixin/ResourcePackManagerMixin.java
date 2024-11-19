package org.hinoob.packgen.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.resourcepack.MinecraftVersion;
import net.hypixel.resourcepack.PackConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import org.hinoob.packgen.FileUtils;
import org.hinoob.packgen.StaticValues;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.TimerTask;

@Environment(EnvType.CLIENT)
@Mixin(PackListWidget.ResourcePackEntry.class)
public class ResourcePackManagerMixin {

    @Shadow @Final private ResourcePackOrganizer.Pack pack;

    @Shadow @Final protected MinecraftClient client;

    @Shadow @Final private PackListWidget widget;

    @Inject(at = @At("HEAD"), method = "enable", cancellable = true)
    public void a(CallbackInfoReturnable<Boolean> cir) {
        if(!this.pack.getCompatibility().isCompatible() && !this.pack.getName().endsWith("_new")) {
            cir.cancel();

            File f = new File("resourcepacks", this.pack.getName().split("/")[1]);
            System.out.println("Converting " + f.toPath() + " (name=" + this.pack.getName() + ")");
            PackConverter converter = new PackConverter(false, MinecraftVersion.v1_21_2, f.toPath());
            try {
                converter.runForPack(new PackConverter.Callback() {
                    @Override
                    public void run() {
                        StaticValues.WAITING_FOR_RESOURCE_PACK = true;
                        StaticValues.RESOURCE_PACK_NAME = pack.getName() + "_new";
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            cir.setReturnValue(false);
        }
    }
}
