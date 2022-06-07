package xyz.firework.autentification;

import com.firework.client.Firework;

public class NoStackTraceThrowable extends RuntimeException {

    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "" + Firework.VERSION;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}