package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.ResourceLocation;

public class AbstractCriterionInstance implements ICriterionInstance
{
    private final ResourceLocation ressourceLocation;

    public AbstractCriterionInstance(ResourceLocation ressourceLocation)
    {
        this.ressourceLocation = ressourceLocation;
    }

    public ResourceLocation getResourceLocation()
    {
        return this.ressourceLocation;
    }

    public String toString()
    {
        return "AbstractCriterionInstance{criterion=" + this.ressourceLocation + '}';
    }
}
