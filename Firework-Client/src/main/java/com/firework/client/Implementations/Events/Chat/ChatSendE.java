package com.firework.client.Implementations.Events.Chat;

import com.google.common.base.Strings;
import ua.firework.beet.Event;

public class ChatSendE extends Event {
    private String message;
    private final String originalMessage;
    public ChatSendE(String message)
    {
        this.setMessage(message);
        this.originalMessage = Strings.nullToEmpty(message);
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = Strings.nullToEmpty(message);
    }

    public String getOriginalMessage()
    {
        return originalMessage;
    }
}
