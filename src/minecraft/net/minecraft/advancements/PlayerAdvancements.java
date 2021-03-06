package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson gson = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> locationAdvancementProgressMapTypeToken = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {} ;
    private final MinecraftServer mcServer;
    private final File progressFile;
    private final Map<Advancement, AdvancementProgress> advancementProgressMap = Maps.<Advancement, AdvancementProgress>newLinkedHashMap();
    private final Set<Advancement> advancementSet1 = Sets.<Advancement>newLinkedHashSet();
    private final Set<Advancement> advancementSet2 = Sets.<Advancement>newLinkedHashSet();
    private final Set<Advancement> advancementSet3 = Sets.<Advancement>newLinkedHashSet();
    private EntityPlayerMP playerMP;
    @Nullable
    private Advancement advancement;
    private boolean field_192763_k = true;

    public PlayerAdvancements(MinecraftServer mcServer, File progressFile, EntityPlayerMP playerMP)
    {
        this.mcServer = mcServer;
        this.progressFile = progressFile;
        this.playerMP = playerMP;
        this.func_192740_f();
    }

    public void func_192739_a(EntityPlayerMP p_192739_1_)
    {
        this.playerMP = p_192739_1_;
    }

    public void func_192745_a()
    {
        for (ICriterionTrigger<?> icriteriontrigger : CriteriaTriggers.getAllTriggers())
        {
            icriteriontrigger.func_192167_a(this);
        }
    }

    
    /**
     * Probably a reset function
     * 
     */
    public void func_193766_b()
    {
        this.func_192745_a();
        this.advancementProgressMap.clear();
        this.advancementSet1.clear();
        this.advancementSet2.clear();
        this.advancementSet3.clear();
        this.field_192763_k = true;
        this.advancement = null;
        this.func_192740_f();
    }

    private void func_192751_c()
    {
        for (Advancement advancement : this.mcServer.getOverworldAvancementManager().func_192780_b())
        {
            this.func_193764_b(advancement);
        }
    }

    private void func_192752_d()
    {
        List<Advancement> list = Lists.<Advancement>newArrayList();

        for (Entry<Advancement, AdvancementProgress> entry : this.advancementProgressMap.entrySet())
        {
            if (((AdvancementProgress)entry.getValue()).func_192105_a())
            {
                list.add(entry.getKey());
                this.advancementSet3.add(entry.getKey());
            }
        }

        for (Advancement advancement : list)
        {
            this.func_192742_b(advancement);
        }
    }

    private void func_192748_e()
    {
        for (Advancement advancement : this.mcServer.getOverworldAvancementManager().func_192780_b())
        {
            if (advancement.getStringCriterionMap().isEmpty())
            {
                this.func_192750_a(advancement, "");
                advancement.getReward().func_192113_a(this.playerMP);
            }
        }
    }

    private void func_192740_f()
    {
        if (this.progressFile.isFile())
        {
            try
            {
                String s = Files.toString(this.progressFile, StandardCharsets.UTF_8);
                Map<ResourceLocation, AdvancementProgress> map = (Map)JsonUtils.func_193840_a(gson, s, locationAdvancementProgressMapTypeToken.getType());

                if (map == null)
                {
                    throw new JsonParseException("Found null for advancements");
                }

                Stream<Entry<ResourceLocation, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));

                for (Entry<ResourceLocation, AdvancementProgress> entry : stream.collect(Collectors.toList()))
                {
                    Advancement advancement = this.mcServer.getOverworldAvancementManager().getAdvancementFrom(entry.getKey());

                    if (advancement == null)
                    {
                        LOGGER.warn("Ignored advancement '" + entry.getKey() + "' in progress file " + this.progressFile + " - it doesn't exist anymore?");
                    }
                    else
                    {
                        this.func_192743_a(advancement, entry.getValue());
                    }
                }
            }
            catch (JsonParseException jsonparseexception)
            {
                LOGGER.error("Couldn't parse player advancements in " + this.progressFile, (Throwable)jsonparseexception);
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't access player advancements in " + this.progressFile, (Throwable)ioexception);
            }
        }

        this.func_192748_e();
        this.func_192752_d();
        this.func_192751_c();
    }

    public void func_192749_b()
    {
        Map<ResourceLocation, AdvancementProgress> map = Maps.<ResourceLocation, AdvancementProgress>newHashMap();

        for (Entry<Advancement, AdvancementProgress> entry : this.advancementProgressMap.entrySet())
        {
            AdvancementProgress advancementprogress = entry.getValue();

            if (advancementprogress.func_192108_b())
            {
                map.put(((Advancement)entry.getKey()).getLocation(), advancementprogress);
            }
        }

        if (this.progressFile.getParentFile() != null)
        {
            this.progressFile.getParentFile().mkdirs();
        }

        try
        {
            Files.write(gson.toJson(map), this.progressFile, StandardCharsets.UTF_8);
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't save player advancements to " + this.progressFile, (Throwable)ioexception);
        }
    }

    public boolean func_192750_a(Advancement advancement, String p_192750_2_)
    {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);
        boolean flag1 = advancementprogress.func_192105_a();

        if (advancementprogress.func_192109_a(p_192750_2_))
        {
            this.func_193765_c(advancement);
            this.advancementSet3.add(advancement);
            flag = true;

            if (!flag1 && advancementprogress.func_192105_a())
            {
                advancement.getReward().func_192113_a(this.playerMP);

                if (advancement.getDisplayInfo() != null && advancement.getDisplayInfo().func_193220_i() && this.playerMP.world.getGameRules().getBoolean("announceAdvancements"))
                {
                    this.mcServer.getPlayerList().sendChatMsg(new TextComponentTranslation("chat.type.advancement." + advancement.getDisplayInfo().func_192291_d().func_192307_a(), new Object[] {this.playerMP.getDisplayName(), advancement.getText()}));
                }
            }
        }

        if (advancementprogress.func_192105_a())
        {
            this.func_192742_b(advancement);
        }

        return flag;
    }

    public boolean func_192744_b(Advancement p_192744_1_, String p_192744_2_)
    {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.func_192747_a(p_192744_1_);

        if (advancementprogress.func_192101_b(p_192744_2_))
        {
            this.func_193764_b(p_192744_1_);
            this.advancementSet3.add(p_192744_1_);
            flag = true;
        }

        if (!advancementprogress.func_192108_b())
        {
            this.func_192742_b(p_192744_1_);
        }

        return flag;
    }

    private void func_193764_b(Advancement p_193764_1_)
    {
        AdvancementProgress advancementprogress = this.func_192747_a(p_193764_1_);

        if (!advancementprogress.func_192105_a())
        {
            for (Entry<String, Criterion> entry : p_193764_1_.getStringCriterionMap().entrySet())
            {
                CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());

                if (criterionprogress != null && !criterionprogress.func_192151_a())
                {
                    ICriterionInstance icriterioninstance = ((Criterion)entry.getValue()).func_192143_a();

                    if (icriterioninstance != null)
                    {
                        ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.<ICriterionInstance>getTriggerOf(icriterioninstance.getResourceLocation());

                        if (icriteriontrigger != null)
                        {
                            icriteriontrigger.func_192165_a(this, new ICriterionTrigger.Listener(icriterioninstance, p_193764_1_, entry.getKey()));
                        }
                    }
                }
            }
        }
    }

    private void func_193765_c(Advancement p_193765_1_)
    {
        AdvancementProgress advancementprogress = this.func_192747_a(p_193765_1_);

        for (Entry<String, Criterion> entry : p_193765_1_.getStringCriterionMap().entrySet())
        {
            CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());

            if (criterionprogress != null && (criterionprogress.func_192151_a() || advancementprogress.func_192105_a()))
            {
                ICriterionInstance icriterioninstance = ((Criterion)entry.getValue()).func_192143_a();

                if (icriterioninstance != null)
                {
                    ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.<ICriterionInstance>getTriggerOf(icriterioninstance.getResourceLocation());

                    if (icriteriontrigger != null)
                    {
                        icriteriontrigger.func_192164_b(this, new ICriterionTrigger.Listener(icriterioninstance, p_193765_1_, entry.getKey()));
                    }
                }
            }
        }
    }

    public void func_192741_b(EntityPlayerMP p_192741_1_)
    {
        if (!this.advancementSet2.isEmpty() || !this.advancementSet3.isEmpty())
        {
            Map<ResourceLocation, AdvancementProgress> map = Maps.<ResourceLocation, AdvancementProgress>newHashMap();
            Set<Advancement> set = Sets.<Advancement>newLinkedHashSet();
            Set<ResourceLocation> set1 = Sets.<ResourceLocation>newLinkedHashSet();

            for (Advancement advancement : this.advancementSet3)
            {
                if (this.advancementSet1.contains(advancement))
                {
                    map.put(advancement.getLocation(), this.advancementProgressMap.get(advancement));
                }
            }

            for (Advancement advancement1 : this.advancementSet2)
            {
                if (this.advancementSet1.contains(advancement1))
                {
                    set.add(advancement1);
                }
                else
                {
                    set1.add(advancement1.getLocation());
                }
            }

            if (!map.isEmpty() || !set.isEmpty() || !set1.isEmpty())
            {
                p_192741_1_.connection.sendPacket(new SPacketAdvancementInfo(this.field_192763_k, set, set1, map));
                this.advancementSet2.clear();
                this.advancementSet3.clear();
            }
        }

        this.field_192763_k = false;
    }

    public void func_194220_a(@Nullable Advancement p_194220_1_)
    {
        Advancement advancement = this.advancement;

        if (p_194220_1_ != null && p_194220_1_.getAdvancement() == null && p_194220_1_.getDisplayInfo() != null)
        {
            this.advancement = p_194220_1_;
        }
        else
        {
            this.advancement = null;
        }

        if (advancement != this.advancement)
        {
            this.playerMP.connection.sendPacket(new SPacketSelectAdvancementsTab(this.advancement == null ? null : this.advancement.getLocation()));
        }
    }

    public AdvancementProgress func_192747_a(Advancement p_192747_1_)
    {
        AdvancementProgress advancementprogress = this.advancementProgressMap.get(p_192747_1_);

        if (advancementprogress == null)
        {
            advancementprogress = new AdvancementProgress();
            this.func_192743_a(p_192747_1_, advancementprogress);
        }

        return advancementprogress;
    }

    private void func_192743_a(Advancement p_192743_1_, AdvancementProgress p_192743_2_)
    {
        p_192743_2_.func_192099_a(p_192743_1_.getStringCriterionMap(), p_192743_1_.getRequirements());
        this.advancementProgressMap.put(p_192743_1_, p_192743_2_);
    }

    private void func_192742_b(Advancement p_192742_1_)
    {
        boolean flag = this.func_192738_c(p_192742_1_);
        boolean flag1 = this.advancementSet1.contains(p_192742_1_);

        if (flag && !flag1)
        {
            this.advancementSet1.add(p_192742_1_);
            this.advancementSet2.add(p_192742_1_);

            if (this.advancementProgressMap.containsKey(p_192742_1_))
            {
                this.advancementSet3.add(p_192742_1_);
            }
        }
        else if (!flag && flag1)
        {
            this.advancementSet1.remove(p_192742_1_);
            this.advancementSet2.add(p_192742_1_);
        }

        if (flag != flag1 && p_192742_1_.getAdvancement() != null)
        {
            this.func_192742_b(p_192742_1_.getAdvancement());
        }

        for (Advancement advancement : p_192742_1_.getAdvancementSet())
        {
            this.func_192742_b(advancement);
        }
    }

    private boolean func_192738_c(Advancement p_192738_1_)
    {
        for (int i = 0; p_192738_1_ != null && i <= 2; ++i)
        {
            if (i == 0 && this.func_192746_d(p_192738_1_))
            {
                return true;
            }

            if (p_192738_1_.getDisplayInfo() == null)
            {
                return false;
            }

            AdvancementProgress advancementprogress = this.func_192747_a(p_192738_1_);

            if (advancementprogress.func_192105_a())
            {
                return true;
            }

            if (p_192738_1_.getDisplayInfo().func_193224_j())
            {
                return false;
            }

            p_192738_1_ = p_192738_1_.getAdvancement();
        }

        return false;
    }

    private boolean func_192746_d(Advancement p_192746_1_)
    {
        AdvancementProgress advancementprogress = this.func_192747_a(p_192746_1_);

        if (advancementprogress.func_192105_a())
        {
            return true;
        }
        else
        {
            for (Advancement advancement : p_192746_1_.getAdvancementSet())
            {
                if (this.func_192746_d(advancement))
                {
                    return true;
                }
            }

            return false;
        }
    }
}
