package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Movement.InputUpdateEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.BaritoneUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import static java.lang.Math.floor;

@ModuleManifest(
        name = "AutoWalk",
        category = Module.Category.MOVEMENT,
        description = "Automatically walks"
)
public class AutoWalk extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.CLASSIC, this);

    private final int border = 30000000;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        mc.player.setSprinting(true);
        if(mode.getValue(modes.BARITONE)) {
            Vec3d dir = mc.player.getLookVec();
            int x = (int) (floor(mc.player.posX) + floor(dir.x) * border);
            int z = (int) (floor(mc.player.posZ) + floor(dir.z) * border);

            BlockPos goal = new BlockPos(x, mc.player.getPosition().getY(), z);

            if (BaritoneUtil.canPath(goal))
                BaritoneUtil.walkTo(goal);
        }
    });

    @Subscribe
    public Listener<InputUpdateEvent> onInput = new Listener<>(event -> {
        if(mode.getValue(modes.CLASSIC))
            event.getMovementInput().moveForward = 1.0f;
    });

    public enum modes{
        CLASSIC, BARITONE
    }
}
