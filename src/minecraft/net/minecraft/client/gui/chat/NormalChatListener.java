package net.minecraft.client.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class NormalChatListener implements IChatListener
{
    private final Minecraft mcInstance;

    public NormalChatListener(Minecraft mcInstance)
    {
        this.mcInstance = mcInstance;
    }

    public void printMessage(ChatType chatType, ITextComponent text)
    {
        this.mcInstance.ingameGUI.getChatGUI().printChatMessage(text);
    }
}
