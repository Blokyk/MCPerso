package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunctionManager implements ITickable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final File functionFile;
    private final MinecraftServer mcServer;
    private final Map<ResourceLocation, FunctionObject> locationFunctionMap = Maps.<ResourceLocation, FunctionObject>newHashMap();
    private String name = "-";
    private FunctionObject function;
    private final ArrayDeque<FunctionManager.QueuedCommand> commandQueue = new ArrayDeque<FunctionManager.QueuedCommand>();
    private boolean misteryBool = false;
    private final ICommandSender sender = new ICommandSender()
    {
        public String getName()
        {
            return FunctionManager.this.name;
        }
        public boolean canCommandSenderUseCommand(int permLevel, String commandName)
        {
            return permLevel <= 2;
        }
        public World getEntityWorld()
        {
            return FunctionManager.this.mcServer.worldServers[0];
        }
        public MinecraftServer getServer()
        {
            return FunctionManager.this.mcServer;
        }
    };

    public FunctionManager(@Nullable File functionFile, MinecraftServer mcServer)
    {
        this.functionFile = functionFile;
        this.mcServer = mcServer;
        this.reset();
    }

    @Nullable
    public FunctionObject getFunctionByName(ResourceLocation location)
    {
        return this.locationFunctionMap.get(location);
    }

    public ICommandManager getCommandManager()
    {
        return this.mcServer.getCommandManager();
    }

    public int getMaxCommandChainLength()
    {
        return this.mcServer.worldServers[0].getGameRules().getInt("maxCommandChainLength");
    }

    public Map<ResourceLocation, FunctionObject> getLocationFunctionMap()
    {
        return this.locationFunctionMap;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        String s = this.mcServer.worldServers[0].getGameRules().getString("gameLoopFunction");

        if (!s.equals(this.name))
        {
            this.name = s;
            this.function = this.getFunctionByName(new ResourceLocation(s));
        }

        if (this.function != null)
        {
            this.func_194019_a(this.function, this.sender);
        }
    }

    public int func_194019_a(FunctionObject function, ICommandSender sender)
    {
        int maxChainLength = this.getMaxCommandChainLength();

        if (this.misteryBool)
        {
            if (this.commandQueue.size() < maxChainLength)
            {
                this.commandQueue.addFirst(new FunctionManager.QueuedCommand(this, sender, new FunctionObject.FunctionEntry(function)));
            }

            return 0;
        }
        else
        {
            int l;

            try
            {
                this.misteryBool = true;
                int j = 0;
                FunctionObject.Entry[] afunctionobject$entry = function.func_193528_a();

                for (int k = afunctionobject$entry.length - 1; k >= 0; --k)
                {
                    this.commandQueue.push(new FunctionManager.QueuedCommand(this, sender, afunctionobject$entry[k]));
                }

                while (true)
                {
                    if (this.commandQueue.isEmpty())
                    {
                        l = j;
                        return l;
                    }

                    (this.commandQueue.removeFirst()).func_194222_a(this.commandQueue, maxChainLength);
                    ++j;

                    if (j >= maxChainLength)
                    {
                        break;
                    }
                }

                l = j;
            }
            finally
            {
                this.commandQueue.clear();
                this.misteryBool = false;
            }

            return l;
        }
    }

    
    /**
     * Probably reset the function object
     */
    public void reset()
    {
        this.locationFunctionMap.clear(); // Reset longname/function map
        this.function = null; // Reset the function object
        this.name = "-"; // Reset the function name
        this.func_193061_h();
    }

    private void func_193061_h()
    {
        if (this.functionFile != null)
        {
            this.functionFile.mkdirs();

            for (File file1 : FileUtils.listFiles(this.functionFile, new String[] {"mcfunction"}, true))
            {
                String s = FilenameUtils.removeExtension(this.functionFile.toURI().relativize(file1.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2)
                {
                    ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);

                    try
                    {
                        this.locationFunctionMap.put(resourcelocation, FunctionObject.func_193527_a(this, Files.readLines(file1, StandardCharsets.UTF_8)));
                    }
                    catch (Throwable throwable)
                    {
                        LOGGER.error("Couldn't read custom function " + resourcelocation + " from " + file1, throwable);
                    }
                }
            }

            if (!this.locationFunctionMap.isEmpty())
            {
                LOGGER.info("Loaded " + this.locationFunctionMap.size() + " custom command functions");
            }
        }
    }

    public static class QueuedCommand
    {
        private final FunctionManager field_194223_a;
        private final ICommandSender field_194224_b;
        private final FunctionObject.Entry field_194225_c;

        public QueuedCommand(FunctionManager p_i47603_1_, ICommandSender p_i47603_2_, FunctionObject.Entry p_i47603_3_)
        {
            this.field_194223_a = p_i47603_1_;
            this.field_194224_b = p_i47603_2_;
            this.field_194225_c = p_i47603_3_;
        }

        public void func_194222_a(ArrayDeque<FunctionManager.QueuedCommand> p_194222_1_, int p_194222_2_)
        {
            this.field_194225_c.func_194145_a(this.field_194223_a, this.field_194224_b, p_194222_1_, p_194222_2_);
        }

        public String toString()
        {
            return this.field_194225_c.toString();
        }
    }
}
