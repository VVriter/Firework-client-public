package xyz.firework.autentification.Initators;

import com.firework.client.Implementations.Managers.Parser.JsonParser;
import com.firework.client.Implementations.Managers.Parser.JsonPrefixPraser;
import com.firework.client.Implementations.Managers.Parser.JsonReader;

public class InitConfigs {
    public static void initate(){
        JsonParser.parse();
        JsonPrefixPraser.parse();
        JsonReader.getPrefix();
        JsonReader.getWebhook();
        JsonReader.getSpamText();

        //FriendManager.getFriends();
        //MuteManager.getListOfNamesOfMutedPlayers();
    }
}
