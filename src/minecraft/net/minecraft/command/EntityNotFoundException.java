package net.minecraft.command;

public class EntityNotFoundException extends CommandException
{
    public EntityNotFoundException(String entityName)
    {
        this("commands.generic.entity.notFound", entityName);
    }

    public EntityNotFoundException(String message, Object... args)
    {
        super(message, args);
    }

    public synchronized Throwable fillInStackTrace()
    {
        return this;
    }
}
