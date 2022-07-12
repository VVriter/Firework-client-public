package AccountManager;

import com.firework.client.Implementations.Managers.Manager;

import java.util.ArrayList;


public class AltManager extends Manager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;

    static {
        registry = new ArrayList();
    }

    public AltManager() {
        super(false);
    }

    public ArrayList<Alt> getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt2) {
        lastAlt = alt2;
    }
}