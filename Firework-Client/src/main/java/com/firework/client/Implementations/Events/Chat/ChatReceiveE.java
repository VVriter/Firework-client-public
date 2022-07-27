package com.firework.client.Implementations.Events.Chat;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import ua.firework.beet.Event;

public class ChatReceiveE extends Event {
    private ITextComponent message;
    private final ChatType type;
    public ChatReceiveE(ChatType type, ITextComponent message)
    {
        this.type = type;
        this.setMessage(message);
    }

    public ITextComponent getMessage()
    {
        return message;
    }

    public void setMessage(ITextComponent message)
    {
        this.message = message;
    }

    public ChatType getType()
    {
        return type;
    }
}
