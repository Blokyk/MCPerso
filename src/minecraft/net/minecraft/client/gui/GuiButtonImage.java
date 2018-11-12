package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton
{
    private final ResourceLocation resourceLocation;
    private final int field_191747_p;
    private final int field_191748_q;
    private final int field_191749_r;

    public GuiButtonImage(int buttonId, int xPos, int yPos, int widthIn, int heightIn, int p_i47392_6_, int p_i47392_7_, int p_i47392_8_, ResourceLocation resourceLocation)
    {
        super(buttonId, xPos, yPos, widthIn, heightIn, "");
        this.field_191747_p = p_i47392_6_;
        this.field_191748_q = p_i47392_7_;
        this.field_191749_r = p_i47392_8_;
        this.resourceLocation = resourceLocation;
    }

    public void func_191746_c(int p_191746_1_, int p_191746_2_)
    {
        this.xPosition = p_191746_1_;
        this.yPosition = p_191746_2_;
    }

    public void drawButton(Minecraft mcInstance, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.visible)
        {
            this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
            mcInstance.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();
            int i = this.field_191747_p;
            int j = this.field_191748_q;

            if (this.hovered)
            {
                j += this.field_191749_r;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, this.width, this.height);
            GlStateManager.enableDepth();
        }
    }
}
