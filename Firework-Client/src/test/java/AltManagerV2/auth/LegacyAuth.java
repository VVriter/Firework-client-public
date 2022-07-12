package AltManagerV2.auth;

import net.minecraft.util.Session;

public class LegacyAuth {
    public static Session login(final String username) {
        return new Session(
                username, "", "", Session.Type.LEGACY.toString()
        );
    }
}