package net.minecraft.command;

public class PlayerNotFoundException extends CommandException
{
    public PlayerNotFoundException(String playerName)
    {
        super(playerName);
    }

    public PlayerNotFoundException(String message, Object... replacements)
    {
        super(message, replacements);
    }

    public synchronized Throwable fillInStackTrace()
    {
        return this;
    }
}
