package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class Announcer extends Module {

    public Setting<Boolean> join = new Setting<>("tS", false, this);
    public Setting<Boolean> leave = new Setting<>("tS", false, this);
    public Setting<Boolean> food = new Setting<>("tS", false, this);
    public Setting<Boolean> place = new Setting<>("tS", false, this);
    public Setting<Boolean> Break = new Setting<>("tS", false, this);
    public Setting<Boolean> worldTime = new Setting<>("tS", false, this);
    public Setting<Boolean> clientSideOnly = new Setting<>("tS", false, this);




    public Announcer(){super("Announcer",Category.CHAT);}



}
