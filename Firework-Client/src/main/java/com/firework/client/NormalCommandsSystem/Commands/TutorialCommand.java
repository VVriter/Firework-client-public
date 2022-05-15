package com.firework.client.NormalCommandsSystem.Commands;


import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.NormalCommandsSystem.Command;
import com.firework.client.NormalCommandsSystem.CommandManifest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;

@CommandManifest(label = "ww", aliases = {"tut"})
public class TutorialCommand extends Command {

    @Override
    public void execute(String[] args) {
        Minecraft.getMinecraft().gameSettings.tutorialStep = TutorialSteps.NONE;
        Minecraft.getMinecraft().getTutorial().setStep(TutorialSteps.NONE);
        MessageUtil.sendClientMessage("Set tutorial step to none!", -11114);
    }

}