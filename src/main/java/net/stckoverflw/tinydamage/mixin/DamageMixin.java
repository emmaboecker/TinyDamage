package net.stckoverflw.tinydamage.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(PlayerEntity.class)
public abstract class DamageMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        PlayerEntity instance = ((PlayerEntity)(Object)this);

        float health = instance.getHealth() + instance.getAbsorptionAmount();

        ScaleTypes.BASE.getScaleData(instance).setScale(health / 20);
    }

}
