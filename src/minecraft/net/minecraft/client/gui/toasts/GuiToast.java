package net.minecraft.client.gui.toasts;

import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Deque;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

public class GuiToast extends Gui
{
    private final Minecraft mcInstance;
    private final GuiToast.ToastInstance<?>[] toastArray = new GuiToast.ToastInstance[5];
    private final Deque<IToast> toastDeque = Queues.<IToast>newArrayDeque();

    public GuiToast(Minecraft mcInstance)
    {
        this.mcInstance = mcInstance;
    }

    public void showToast(ScaledResolution resolution)
    {
        if (!this.mcInstance.gameSettings.hideGUI)
        {
            RenderHelper.disableStandardItemLighting();

            for (int i = 0; i < this.toastArray.length; ++i)
            {
                GuiToast.ToastInstance<?> toastinstance = this.toastArray[i];

                if (toastinstance != null)
                {
                    if (toastinstance.func_193684_a(resolution.getScaledWidth(), i)) {
						this.toastArray[i] = null;
					}
                } else if (!this.toastDeque.isEmpty())
                {
                    this.toastArray[i] = new GuiToast.ToastInstance(this.toastDeque.removeFirst());
                }
            }
        }
    }

    @Nullable
    public <T extends IToast> T func_192990_a(Class <? extends T > p_192990_1_, Object p_192990_2_)
    {
        for (GuiToast.ToastInstance<?> toastinstance : this.toastArray)
        {
            if (toastinstance != null && p_192990_1_.isAssignableFrom(toastinstance.func_193685_a().getClass()) && toastinstance.func_193685_a().func_193652_b().equals(p_192990_2_))
            {
                return (T)toastinstance.func_193685_a();
            }
        }

        for (IToast itoast : this.toastDeque)
        {
            if (p_192990_1_.isAssignableFrom(itoast.getClass()) && itoast.func_193652_b().equals(p_192990_2_))
            {
                return (T)itoast;
            }
        }

        return (T)null;
    }

    public void clear()
    {
        Arrays.fill(this.toastArray, (Object)null);
        this.toastDeque.clear();
    }

    public void addToast(IToast toast)
    {
        this.toastDeque.add(toast);
    }

    public Minecraft getMCInstance()
    {
        return this.mcInstance;
    }

    class ToastInstance<T extends IToast>
    {
        private final T field_193688_b;
        private long field_193689_c;
        private long field_193690_d;
        private IToast.Visibility visibility;

        private ToastInstance(T p_i47483_2_)
        {
            this.field_193689_c = -1L;
            this.field_193690_d = -1L;
            this.visibility = IToast.Visibility.SHOW;
            this.field_193688_b = p_i47483_2_;
        }

        public T func_193685_a()
        {
            return this.field_193688_b;
        }

        private float func_193686_a(long p_193686_1_)
        {
            float f = MathHelper.clamp((float)(p_193686_1_ - this.field_193689_c) / 600.0F, 0.0F, 1.0F);
            f = f * f;
            return this.visibility == IToast.Visibility.HIDE ? 1.0F - f : f;
        }

        public boolean func_193684_a(int p_193684_1_, int p_193684_2_)
        {
            long i = Minecraft.getSystemTime();

            if (this.field_193689_c == -1L)
            {
                this.field_193689_c = i;
                this.visibility.func_194169_a(GuiToast.this.mcInstance.getSoundHandler());
            }

            if (this.visibility == IToast.Visibility.SHOW && i - this.field_193689_c <= 600L)
            {
                this.field_193690_d = i;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_193684_1_ - 160.0F * this.func_193686_a(i), (float)(p_193684_2_ * 32), (float)(500 + p_193684_2_));
            IToast.Visibility itoast$visibility = this.field_193688_b.func_193653_a(GuiToast.this, i - this.field_193690_d);
            GlStateManager.popMatrix();

            if (itoast$visibility != this.visibility)
            {
                this.field_193689_c = i - (long)((int)((1.0F - this.func_193686_a(i)) * 600.0F));
                this.visibility = itoast$visibility;
                this.visibility.func_194169_a(GuiToast.this.mcInstance.getSoundHandler());
            }

            return this.visibility == IToast.Visibility.HIDE && i - this.field_193689_c > 600L;
        }
    }
}
