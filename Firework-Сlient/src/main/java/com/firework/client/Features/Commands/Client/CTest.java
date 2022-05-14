package com.firework.client.Features.Commands.Client;

import com.firework.client.Features.Commands.Command;
import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Managers.Module.ModuleManager;

public class CTest extends Command {

    public CTest() {
        super(ModuleManager.modules.get(0), "test");
    }
}
