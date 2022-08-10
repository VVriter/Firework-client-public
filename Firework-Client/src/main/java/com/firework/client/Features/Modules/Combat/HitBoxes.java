package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "HitBoxes",
        category = Module.Category.COMBAT,
        description = "Add size to default hitboxes"
)
public class HitBoxes extends Module {

    public Setting<Double> expand = new Setting<>("Expand", (double)0.5, this, 0, 2);

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(event-> {
        for (Entity entity : mc.world.playerEntities) {
            if(entity != mc.player){
                float width = entity.width;
                float height = entity.height;
                float expandValue = expand.getValue().floatValue();
                entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - width - expandValue, entity.posY, entity.posZ + width + expandValue, entity.posX + width + expandValue, entity.posY + height + expandValue, entity.posZ - width - expandValue));
            }
        }
    });
}
