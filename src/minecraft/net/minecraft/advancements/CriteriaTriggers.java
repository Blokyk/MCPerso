package net.minecraft.advancements;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.advancements.critereon.PositionTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.VillagerTradeTrigger;
import net.minecraft.util.ResourceLocation;

public class CriteriaTriggers
{
    private static final Map < ResourceLocation, ICriterionTrigger<? >> locationCriterionTriggerMap = Maps. < ResourceLocation, ICriterionTrigger<? >> newHashMap();
    public static final ImpossibleTrigger impossibleTrigger = (ImpossibleTrigger)addTrigger(new ImpossibleTrigger());
    public static final KilledTrigger playerKilledEntity = (KilledTrigger)addTrigger(new KilledTrigger(new ResourceLocation("player_killed_entity")));
    public static final KilledTrigger entityKilledPlayerTriger = (KilledTrigger)addTrigger(new KilledTrigger(new ResourceLocation("entity_killed_player")));
    public static final EnterBlockTrigger enterBlockTrigger = (EnterBlockTrigger)addTrigger(new EnterBlockTrigger());
    public static final InventoryChangeTrigger inventoryChangeTrigger = (InventoryChangeTrigger)addTrigger(new InventoryChangeTrigger());
    public static final RecipeUnlockedTrigger recipeUnlockedTrigger = (RecipeUnlockedTrigger)addTrigger(new RecipeUnlockedTrigger());
    public static final PlayerHurtEntityTrigger playerHurtEntityTrigger = (PlayerHurtEntityTrigger)addTrigger(new PlayerHurtEntityTrigger());
    public static final EntityHurtPlayerTrigger entityHurtPlayerTrigger = (EntityHurtPlayerTrigger)addTrigger(new EntityHurtPlayerTrigger());
    public static final EnchantedItemTrigger enchantedItemTrigger = (EnchantedItemTrigger)addTrigger(new EnchantedItemTrigger());
    public static final BrewedPotionTrigger brewedPotionTrigger = (BrewedPotionTrigger)addTrigger(new BrewedPotionTrigger());
    public static final ConstructBeaconTrigger constructBeaconTrigger = (ConstructBeaconTrigger)addTrigger(new ConstructBeaconTrigger());
    public static final UsedEnderEyeTrigger usedEnderEyeTrigger = (UsedEnderEyeTrigger)addTrigger(new UsedEnderEyeTrigger());
    public static final SummonedEntityTrigger summonedEntityTrigger = (SummonedEntityTrigger)addTrigger(new SummonedEntityTrigger());
    public static final BredAnimalsTrigger bredAnimalsTrigger = (BredAnimalsTrigger)addTrigger(new BredAnimalsTrigger());
    public static final PositionTrigger positionTrigger = (PositionTrigger)addTrigger(new PositionTrigger(new ResourceLocation("location")));
    public static final PositionTrigger sleptInBedTrigger = (PositionTrigger)addTrigger(new PositionTrigger(new ResourceLocation("slept_in_bed")));
    public static final CuredZombieVillagerTrigger curedZombieVillagerTrigger = (CuredZombieVillagerTrigger)addTrigger(new CuredZombieVillagerTrigger());
    public static final VillagerTradeTrigger villagerTradeTrigger = (VillagerTradeTrigger)addTrigger(new VillagerTradeTrigger());
    public static final ItemDurabilityTrigger itemDurabilityTrigger = (ItemDurabilityTrigger)addTrigger(new ItemDurabilityTrigger());
    public static final LevitationTrigger levitationTrigger = (LevitationTrigger)addTrigger(new LevitationTrigger());
    public static final ChangeDimensionTrigger changeDimensionTrigger = (ChangeDimensionTrigger)addTrigger(new ChangeDimensionTrigger());
    public static final TickTrigger tickTrigger = (TickTrigger)addTrigger(new TickTrigger());
    public static final TameAnimalTrigger tameAnimalTrigger = (TameAnimalTrigger)addTrigger(new TameAnimalTrigger());
    public static final PlacedBlockTrigger placedBlockTrigger = (PlacedBlockTrigger)addTrigger(new PlacedBlockTrigger());
    public static final ConsumeItemTrigger consumeItemTrigger = (ConsumeItemTrigger)addTrigger(new ConsumeItemTrigger());
    public static final EffectsChangedTrigger effectsChangedTrigger = (EffectsChangedTrigger)addTrigger(new EffectsChangedTrigger());
    public static final UsedTotemTrigger usedTotemTrigger = (UsedTotemTrigger)addTrigger(new UsedTotemTrigger());
    public static final NetherTravelTrigger netherTravelTrigger = (NetherTravelTrigger)addTrigger(new NetherTravelTrigger());

    private static <T extends ICriterionTrigger> T addTrigger(T criterionTrigger)
    {
        if (locationCriterionTriggerMap.containsKey(criterionTrigger.getRessourceLocation()))
        {
            throw new IllegalArgumentException("Duplicate criterion id " + criterionTrigger.getRessourceLocation());
        }
        else
        {
            locationCriterionTriggerMap.put(criterionTrigger.getRessourceLocation(), criterionTrigger);
            return criterionTrigger;
        }
    }

    @Nullable
    public static <T extends ICriterionInstance> ICriterionTrigger<T> getTriggerOf(ResourceLocation triggerKey)
    {
        return (ICriterionTrigger)locationCriterionTriggerMap.get(triggerKey);
    }

    public static Iterable <? extends ICriterionTrigger<? >> getAllTriggers()
    {
        return locationCriterionTriggerMap.values();
    }
}
