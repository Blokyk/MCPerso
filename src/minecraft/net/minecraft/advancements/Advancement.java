package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.ArrayUtils;

public class Advancement
{
    private final Advancement advancement;
    private final DisplayInfo displayInfo;
    private final AdvancementRewards reward;
    private final ResourceLocation location;
    private final Map<String, Criterion> stringCriterionMap;
    private final String[][] requirements;
    private final Set<Advancement> advancementSet = Sets.<Advancement>newLinkedHashSet();
    private final ITextComponent text;

    public Advancement(ResourceLocation location, @Nullable Advancement advancement, @Nullable DisplayInfo displayInfo, AdvancementRewards reward, Map<String, Criterion> stringCriterionMap, String[][] requirements)
    {
        this.location = location;
        this.displayInfo = displayInfo;
        this.stringCriterionMap = ImmutableMap.copyOf(stringCriterionMap);
        this.advancement = advancement;
        this.reward = reward;
        this.requirements = requirements;

        if (advancement != null)
        {
            advancement.addAdvancement(this);
        }

        if (displayInfo == null)
        {
            this.text = new TextComponentString(location.toString());
        }
        else
        {
            this.text = new TextComponentString("[");
            this.text.getStyle().setColor(displayInfo.func_192291_d().func_193229_c());
            ITextComponent itextcomponent = displayInfo.func_192297_a().createCopy();
            ITextComponent itextcomponent1 = new TextComponentString("");
            ITextComponent itextcomponent2 = itextcomponent.createCopy();
            itextcomponent2.getStyle().setColor(displayInfo.func_192291_d().func_193229_c());
            itextcomponent1.appendSibling(itextcomponent2);
            itextcomponent1.appendText("\n");
            itextcomponent1.appendSibling(displayInfo.func_193222_b());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, itextcomponent1));
            this.text.appendSibling(itextcomponent);
            this.text.appendText("]");
        }
    }

    public Advancement.Builder getBuilder()
    {
        return new Advancement.Builder(this.advancement == null ? null : this.advancement.getLocation(), this.displayInfo, this.reward, this.stringCriterionMap, this.requirements);
    }

    @Nullable
    public Advancement getAdvancement()
    {
        return this.advancement;
    }

    @Nullable
    public DisplayInfo getDisplayInfo()
    {
        return this.displayInfo;
    }

    public AdvancementRewards getReward()
    {
        return this.reward;
    }

    public String toString()
    {
        return "SimpleAdvancement{id=" + this.getLocation() + ", parent=" + (this.advancement == null ? "null" : this.advancement.getLocation()) + ", display=" + this.displayInfo + ", rewards=" + this.reward + ", criteria=" + this.stringCriterionMap + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
    }

    public Iterable<Advancement> getAdvancementSet()
    {
        return this.advancementSet;
    }

    public Map<String, Criterion> getStringCriterionMap()
    {
        return this.stringCriterionMap;
    }

    public int getRequirementsNumber()
    {
        return this.requirements.length;
    }

    public void addAdvancement(Advancement advancement)
    {
        this.advancementSet.add(advancement);
    }

    public ResourceLocation getLocation()
    {
        return this.location;
    }

    public boolean equals(Object otherObject)
    {
        if (this == otherObject)
        {
            return true;
        }
        else if (!(otherObject instanceof Advancement))
        {
            return false;
        }
        else
        {
            Advancement advancement = (Advancement)otherObject;
            return this.location.equals(advancement.location);
        }
    }

    public int hashCode()
    {
        return this.location.hashCode();
    }

    public String[][] getRequirements()
    {
        return this.requirements;
    }

    public ITextComponent getText()
    {
        return this.text;
    }

    public static class Builder
    {
        private final ResourceLocation field_192061_a;
        private Advancement field_192062_b;
        private final DisplayInfo field_192063_c;
        private final AdvancementRewards field_192064_d;
        private final Map<String, Criterion> field_192065_e;
        private final String[][] field_192066_f;

        Builder(@Nullable ResourceLocation p_i47414_1_, @Nullable DisplayInfo p_i47414_2_, AdvancementRewards p_i47414_3_, Map<String, Criterion> p_i47414_4_, String[][] p_i47414_5_)
        {
            this.field_192061_a = p_i47414_1_;
            this.field_192063_c = p_i47414_2_;
            this.field_192064_d = p_i47414_3_;
            this.field_192065_e = p_i47414_4_;
            this.field_192066_f = p_i47414_5_;
        }

        public boolean func_192058_a(Function<ResourceLocation, Advancement> p_192058_1_)
        {
            if (this.field_192061_a == null)
            {
                return true;
            }
            else
            {
                this.field_192062_b = p_192058_1_.apply(this.field_192061_a);
                return this.field_192062_b != null;
            }
        }

        public Advancement func_192056_a(ResourceLocation p_192056_1_)
        {
            return new Advancement(p_192056_1_, this.field_192062_b, this.field_192063_c, this.field_192064_d, this.field_192065_e, this.field_192066_f);
        }

        public void func_192057_a(PacketBuffer p_192057_1_)
        {
            if (this.field_192061_a == null)
            {
                p_192057_1_.writeBoolean(false);
            }
            else
            {
                p_192057_1_.writeBoolean(true);
                p_192057_1_.func_192572_a(this.field_192061_a);
            }

            if (this.field_192063_c == null)
            {
                p_192057_1_.writeBoolean(false);
            }
            else
            {
                p_192057_1_.writeBoolean(true);
                this.field_192063_c.func_192290_a(p_192057_1_);
            }

            Criterion.func_192141_a(this.field_192065_e, p_192057_1_);
            p_192057_1_.writeVarIntToBuffer(this.field_192066_f.length);

            for (String[] astring : this.field_192066_f)
            {
                p_192057_1_.writeVarIntToBuffer(astring.length);

                for (String s : astring)
                {
                    p_192057_1_.writeString(s);
                }
            }
        }

        public String toString()
        {
            return "Task Advancement{parentId=" + this.field_192061_a + ", display=" + this.field_192063_c + ", rewards=" + this.field_192064_d + ", criteria=" + this.field_192065_e + ", requirements=" + Arrays.deepToString(this.field_192066_f) + '}';
        }

        public static Advancement.Builder func_192059_a(JsonObject p_192059_0_, JsonDeserializationContext p_192059_1_)
        {
            ResourceLocation resourcelocation = p_192059_0_.has("parent") ? new ResourceLocation(JsonUtils.getString(p_192059_0_, "parent")) : null;
            DisplayInfo displayinfo = p_192059_0_.has("display") ? DisplayInfo.func_192294_a(JsonUtils.getJsonObject(p_192059_0_, "display"), p_192059_1_) : null;
            AdvancementRewards advancementrewards = (AdvancementRewards)JsonUtils.deserializeClass(p_192059_0_, "rewards", AdvancementRewards.nullReward, p_192059_1_, AdvancementRewards.class);
            Map<String, Criterion> map = Criterion.func_192144_b(JsonUtils.getJsonObject(p_192059_0_, "criteria"), p_192059_1_);

            if (map.isEmpty())
            {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            }
            else
            {
                JsonArray jsonarray = JsonUtils.getJsonArray(p_192059_0_, "requirements", new JsonArray());
                String[][] astring = new String[jsonarray.size()][];

                for (int i = 0; i < jsonarray.size(); ++i)
                {
                    JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonarray.get(i), "requirements[" + i + "]");
                    astring[i] = new String[jsonarray1.size()];

                    for (int j = 0; j < jsonarray1.size(); ++j)
                    {
                        astring[i][j] = JsonUtils.getString(jsonarray1.get(j), "requirements[" + i + "][" + j + "]");
                    }
                }

                if (astring.length == 0)
                {
                    astring = new String[map.size()][];
                    int k = 0;

                    for (String s2 : map.keySet())
                    {
                        astring[k++] = new String[] {s2};
                    }
                }

                for (String[] astring1 : astring)
                {
                    if (astring1.length == 0 && map.isEmpty())
                    {
                        throw new JsonSyntaxException("Requirement entry cannot be empty");
                    }

                    for (String s : astring1)
                    {
                        if (!map.containsKey(s))
                        {
                            throw new JsonSyntaxException("Unknown required criterion '" + s + "'");
                        }
                    }
                }

                for (String s1 : map.keySet())
                {
                    boolean flag = false;

                    for (String[] astring2 : astring)
                    {
                        if (ArrayUtils.contains(astring2, s1))
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag)
                    {
                        throw new JsonSyntaxException("Criterion '" + s1 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                    }
                }

                return new Advancement.Builder(resourcelocation, displayinfo, advancementrewards, map, astring);
            }
        }

        public static Advancement.Builder func_192060_b(PacketBuffer p_192060_0_) throws IOException
        {
            ResourceLocation resourcelocation = p_192060_0_.readBoolean() ? p_192060_0_.func_192575_l() : null;
            DisplayInfo displayinfo = p_192060_0_.readBoolean() ? DisplayInfo.func_192295_b(p_192060_0_) : null;
            Map<String, Criterion> map = Criterion.func_192142_c(p_192060_0_);
            String[][] astring = new String[p_192060_0_.readVarIntFromBuffer()][];

            for (int i = 0; i < astring.length; ++i)
            {
                astring[i] = new String[p_192060_0_.readVarIntFromBuffer()];

                for (int j = 0; j < astring[i].length; ++j)
                {
                    astring[i][j] = p_192060_0_.readStringFromBuffer(32767);
                }
            }

            return new Advancement.Builder(resourcelocation, displayinfo, AdvancementRewards.nullReward, map, astring);
        }
    }
}
