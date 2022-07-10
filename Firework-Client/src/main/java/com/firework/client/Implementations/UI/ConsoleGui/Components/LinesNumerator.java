package com.firework.client.Implementations.UI.ConsoleGui.Components;

import com.firework.client.Features.Modules.Client.Console;
import com.firework.client.Implementations.UI.ConsoleGui.Messages.ConsoleMessage;

public class LinesNumerator {
    public static void doNumeratorStuff() {
        ConsoleMessage.num(" 1", Console.x.getValue()+3,Console.y.getValue().floatValue()+10);
        ConsoleMessage.num(" 2",Console.x.getValue()+3,Console.y.getValue().floatValue()+20);
        ConsoleMessage.num(" 3",Console.x.getValue()+3,Console.y.getValue().floatValue()+30);
        ConsoleMessage.num(" 4",Console.x.getValue()+3,Console.y.getValue().floatValue()+40);
        ConsoleMessage.num(" 5",Console.x.getValue()+3,Console.y.getValue().floatValue()+50);
        ConsoleMessage.num(" 6",Console.x.getValue()+3,Console.y.getValue().floatValue()+60);
        ConsoleMessage.num(" 7",Console.x.getValue()+3,Console.y.getValue().floatValue()+70);
        ConsoleMessage.num(" 8",Console.x.getValue()+3,Console.y.getValue().floatValue()+80);
        ConsoleMessage.num(" 9",Console.x.getValue()+3,Console.y.getValue().floatValue()+90);
        ConsoleMessage.num("10",Console.x.getValue()+3,Console.y.getValue().floatValue()+100);
        ConsoleMessage.num("11",Console.x.getValue()+3,Console.y.getValue().floatValue()+110);
        ConsoleMessage.num("12",Console.x.getValue()+3,Console.y.getValue().floatValue()+120);
        ConsoleMessage.num("13",Console.x.getValue()+3,Console.y.getValue().floatValue()+130);
        ConsoleMessage.num("14",Console.x.getValue()+3,Console.y.getValue().floatValue()+140);
    }
}
