package com.firework.client.Features.CommandsSystem.Commands.Fun;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.tutorial.TutorialSteps;

@CommandManifest(label = "ww", aliases = {"tut"})
public class TutorialCommand extends Command {

    @Override
    public void execute(String[] args) {
        Firework.minecraft.gameSettings.tutorialStep = TutorialSteps.NONE;
        Firework.minecraft.getTutorial().setStep(TutorialSteps.NONE);
        MessageUtil.sendClientMessage("Set tutorial step to none!", -11114);
    }

}