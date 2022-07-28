package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Test;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleManifest(
        name = "AutoTunnel",
        category = Module.Category.WORLD,
        description = "Automatically mines tunnels"
)
public class AutoTunnel extends Module {

    public Setting<mode> Mode = new Setting<>("Mode", mode.Tunnel1x2, this);
    public enum mode{
        Tunnel1x2, Tunnel2x2, Tunnel2x3, Tunnel3x3
    }
    private List<BlockPos> _blocksToDestroy = new CopyOnWriteArrayList<>();

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        _blocksToDestroy.clear();

        BlockPos playerPos = PlayerUtil.GetLocalPlayerPosFloored();

        switch (PlayerUtil.GetFacing())
        {
            case East:
                switch (Mode.getValue())
                {
                    case Tunnel1x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    case Tunnel2x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    case Tunnel2x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().up().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());
                            _blocksToDestroy.add(playerPos.east().north().up().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    case Tunnel3x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().up().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());
                            _blocksToDestroy.add(playerPos.east().north().up().up());
                            _blocksToDestroy.add(playerPos.east().north().north());
                            _blocksToDestroy.add(playerPos.east().north().north().up());
                            _blocksToDestroy.add(playerPos.east().north().north().up().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case North:
                switch (Mode.getValue())
                {
                    case Tunnel1x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    case Tunnel2x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    case Tunnel2x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().up().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());
                            _blocksToDestroy.add(playerPos.north().east().up().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    case Tunnel3x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().up().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());
                            _blocksToDestroy.add(playerPos.north().east().up().up());
                            _blocksToDestroy.add(playerPos.north().east().east());
                            _blocksToDestroy.add(playerPos.north().east().east().up());
                            _blocksToDestroy.add(playerPos.north().east().east().up().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case South:
                switch (Mode.getValue())
                {
                    case Tunnel1x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    case Tunnel2x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    case Tunnel2x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().up().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());
                            _blocksToDestroy.add(playerPos.south().west().up().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    case Tunnel3x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().up().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());
                            _blocksToDestroy.add(playerPos.south().west().up().up());
                            _blocksToDestroy.add(playerPos.south().west().west());
                            _blocksToDestroy.add(playerPos.south().west().west().up());
                            _blocksToDestroy.add(playerPos.south().west().west().up().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case West:
                switch (Mode.getValue())
                {
                    case Tunnel1x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    case Tunnel2x2:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    case Tunnel2x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().up().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());
                            _blocksToDestroy.add(playerPos.west().south().up().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    case Tunnel3x3:
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().up().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());
                            _blocksToDestroy.add(playerPos.west().south().up().up());
                            _blocksToDestroy.add(playerPos.west().south().south());
                            _blocksToDestroy.add(playerPos.west().south().south().up());
                            _blocksToDestroy.add(playerPos.west().south().south().up().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    });

    public Setting<Boolean> blockBreaker = new Setting<>("BlockBreaker", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockBreaker.mineModes> switchMode = new Setting<>("MineMode", BlockBreaker.mineModes.Classic, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> raytrace = new Setting<>("Rotate", true, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(v-> blockBreaker.getValue());
    BlockBreaker breaker;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> breakerListner = new Listener<>(e-> {
        for (BlockPos pos : _blocksToDestroy) {
            if (BlockUtil.getBlock(pos) == Blocks.AIR || BlockUtil.getBlock(pos) instanceof BlockLiquid) continue;
            breaker.breakBlock(pos,mc.player.inventory.getCurrentItem().getItem());
        }
    });


    @Override
    public void onEnable() {
        super.onEnable();
        breaker = new BlockBreaker(this,switchMode,rotate,raytrace,swing);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        breaker = null;
    }



    @Subscribe
    public Listener<Render3dE> listener1 = new Listener<>(e-> {
        for (BlockPos pos : _blocksToDestroy) {
            if (pos != null) {
                RenderUtils.drawBoxESP(pos, Color.CYAN,1,true,false,150,1);
            }
        }
    });
}
