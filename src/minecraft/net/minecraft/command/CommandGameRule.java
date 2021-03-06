package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "gamerule";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.gamerule.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        GameRules gamerules = this.getOverWorldGameRules(server);
        String arg1 = args.length > 0 ? args[0] : "";
        String s1 = args.length > 1 ? buildString(args, 1) : "";

        switch (args.length)
        {
            case 0:
                sender.addChatMessage(new TextComponentString(joinNiceString(gamerules.getRules())));
                break;

            case 1:
                if (!gamerules.hasRule(arg1))
                {
                    throw new CommandException("commands.gamerule.norule", new Object[] {arg1});
                }

                String s2 = gamerules.getString(arg1);
                sender.addChatMessage((new TextComponentString(arg1)).appendText(" = ").appendText(s2));
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(arg1));
                break;

            default:
                if (gamerules.areSameType(arg1, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1))
                {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[] {s1});
                }

                gamerules.setOrCreateGameRule(arg1, s1);
                notifyGameRuleChange(gamerules, arg1, server);
                notifyCommandListener(sender, this, "commands.gamerule.success", new Object[] {arg1, s1});
        }
    }

    public static void notifyGameRuleChange(GameRules rules, String gamerule, MinecraftServer server)
    {
        if ("reducedDebugInfo".equals(gamerule))
        {
            byte b0 = (byte)(rules.getBoolean(gamerule) ? 22 : 23);

            for (EntityPlayerMP entityplayermp : server.getPlayerList().getPlayerList())
            {
                entityplayermp.connection.sendPacket(new SPacketEntityStatus(entityplayermp, b0));
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, this.getOverWorldGameRules(server).getRules());
        }
        else
        {
            if (args.length == 2)
            {
                GameRules gamerules = this.getOverWorldGameRules(server);

                if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE))
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"true", "false"});
                }

                if (gamerules.areSameType(args[0], GameRules.ValueType.FUNCTION))
                {
                    return getListOfStringsMatchingLastWord(args, server.getOverworldFunctionManager().getLocationFunctionMap().keySet());
                }
            }

            return Collections.<String>emptyList();
        }
    }

    /**
     * Get the game rules for the overworld
     */
    private GameRules getOverWorldGameRules(MinecraftServer server)
    {
        return server.worldServerForDimension(0).getGameRules();
    }
}
