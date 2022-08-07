package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.MovementUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Arrays;

@ModuleManifest(
        name = "Sprint",
        category = Module.Category.MOVEMENT,
        description = "Keeps sprinting every time"
)
public class Sprint extends Module {
    public static Setting<Boolean> enabled = null;
    public Sprint() {
        enabled = this.isEnabled;
    }

    public Setting<String> mode = new Setting<>("Mode", "Legit", this, Arrays.asList("Legit", "Multi"));
    public Setting<Boolean> autoYaw = new Setting<>("AutoYaw", true, this).setVisibility(v-> mode.getValue().equals("Multi"));
    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (mode.getValue().equals("Legit")) {
            if (mc.player.moveForward > 0 && !mc.player.collidedHorizontally) {
                mc.player.setSprinting(true);
            }
        } else if (mode.getValue().equals("Multi")) {
            if (autoYaw.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(MovementUtil.getMoveYaw(mc.player.rotationYaw),mc.player.rotationPitch,mc.player.onGround));
            }

            if (Sprint.mc.player == null) {
                return;
            }
            if ((Sprint.mc.player.moveForward != 0.0f || Sprint.mc.player.moveStrafing != 0.0f)) {
                Sprint.mc.player.setSprinting(true);
            }
            else {
                Sprint.mc.player.setSprinting(Sprint.mc.player.moveForward > 0.0f || Sprint.mc.player.moveStrafing > 0.0f);
            }
        }
    });
}
