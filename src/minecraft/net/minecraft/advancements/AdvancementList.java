package net.minecraft.advancements;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementList
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, Advancement> advancementResourceLocationMap = Maps.<ResourceLocation, Advancement>newHashMap();
    private final Set<Advancement> advancementSet = Sets.<Advancement>newLinkedHashSet();
    private final Set<Advancement> advancementSet1 = Sets.<Advancement>newLinkedHashSet();
    private AdvancementList.Listener advancementListener;

    private void func_192090_a(Advancement advancement)
    {
        for (Advancement advancement1 : advancement.getAdvancementSet())
        {
            this.func_192090_a(advancement1);
        }
        
        LOGGER.info(advancement.getLocation());

        LOGGER.info("Forgot about advancement " + advancement.getLocation());
        this.advancementResourceLocationMap.remove(advancement.getLocation());

        if (advancement.getAdvancement() == null)
        {
            this.advancementSet.remove(advancement);

            if (this.advancementListener != null)
            {
                this.advancementListener.func_191928_b(advancement);
            }
        }
        else
        {
            this.advancementSet1.remove(advancement);

            if (this.advancementListener != null)
            {
                this.advancementListener.func_191929_d(advancement);
            }
        }
    }

    public void func_192085_a(Set<ResourceLocation> resourceLocationSet)
    {
        for (ResourceLocation resourcelocation : resourceLocationSet)
        {
            Advancement advancement = this.advancementResourceLocationMap.get(resourcelocation);

            if (advancement == null)
            {
                LOGGER.warn("Told to remove advancement " + resourcelocation + " but I don't know what that is");
            }
            else
            {
                this.func_192090_a(advancement);
            }
        }
    }

    public void func_192083_a(Map<ResourceLocation, Advancement.Builder> p_192083_1_)
    {
        Function<ResourceLocation, Advancement> function = Functions.<ResourceLocation, Advancement>forMap(this.advancementResourceLocationMap, null);
        label42:

        while (!p_192083_1_.isEmpty())
        {
            boolean flag = false;
            Iterator<Entry<ResourceLocation, Advancement.Builder>> iterator = p_192083_1_.entrySet().iterator();

            while (iterator.hasNext())
            {
                Entry<ResourceLocation, Advancement.Builder> entry = (Entry)iterator.next();
                ResourceLocation resourcelocation = entry.getKey();
                Advancement.Builder advancement$builder = entry.getValue();

                if (advancement$builder.func_192058_a(function))
                {
                    Advancement advancement = advancement$builder.func_192056_a(resourcelocation);
                    this.advancementResourceLocationMap.put(resourcelocation, advancement);
                    flag = true;
                    iterator.remove();

                    if (advancement.getAdvancement() == null)
                    {
                        this.advancementSet.add(advancement);

                        if (this.advancementListener != null)
                        {
                            this.advancementListener.func_191931_a(advancement);
                        }
                    }
                    else
                    {
                        this.advancementSet1.add(advancement);

                        if (this.advancementListener != null)
                        {
                            this.advancementListener.func_191932_c(advancement);
                        }
                    }
                }
            }

            if (!flag)
            {
                iterator = p_192083_1_.entrySet().iterator();

                while (true)
                {
                    if (!iterator.hasNext())
                    {
                        break label42;
                    }

                    Entry<ResourceLocation, Advancement.Builder> entry1 = (Entry)iterator.next();
                    LOGGER.error("Couldn't load advancement " + entry1.getKey() + ": " + entry1.getValue());
                }
            }
        }

        LOGGER.info("Loaded " + this.advancementResourceLocationMap.size() + " advancements");
    }

    public void reset()
    {
        this.advancementResourceLocationMap.clear();
        this.advancementSet.clear();
        this.advancementSet1.clear();

        if (this.advancementListener != null)
        {
            this.advancementListener.func_191930_a();
        }
    }

    public Iterable<Advancement> func_192088_b()
    {
        return this.advancementSet;
    }

    public Iterable<Advancement> func_192089_c()
    {
        return this.advancementResourceLocationMap.values();
    }

    @Nullable
    public Advancement getAdvancementFrom(ResourceLocation resourceLocation)
    {
        return this.advancementResourceLocationMap.get(resourceLocation);
    }

    public void func_192086_a(@Nullable AdvancementList.Listener listener)
    {
        this.advancementListener = listener;

        if (listener != null)
        {
            for (Advancement advancement : this.advancementSet)
            {
                listener.func_191931_a(advancement);
            }

            for (Advancement advancement1 : this.advancementSet1)
            {
                listener.func_191932_c(advancement1);
            }
        }
    }

    public interface Listener
    {
        void func_191931_a(Advancement p_191931_1_);

        void func_191928_b(Advancement p_191928_1_);

        void func_191932_c(Advancement p_191932_1_);

        void func_191929_d(Advancement p_191929_1_);

        void func_191930_a();
    }
}
