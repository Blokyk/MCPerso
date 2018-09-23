package net.minecraft.util.text;

import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase
{
	/**
	 * Returns an other function that gives out the string you put in
	 */
    public static Function<String, Supplier<String>> getStringSupplier = (a) ->
    {
        return () -> {
            return a;
        };
    };
    private final String text;
    private Supplier<String> textSupplier;

    public TextComponentKeybind(String text)
    {
        this.text = text;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedComponentText()
    {
        if (this.textSupplier == null)
        {
            this.textSupplier = (Supplier)getStringSupplier.apply(this.text);
        }

        return this.textSupplier.get();
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public TextComponentKeybind createCopy()
    {
        TextComponentKeybind textcomponentkeybind = new TextComponentKeybind(this.text);
        textcomponentkeybind.setStyle(this.getStyle().createShallowCopy());

        for (ITextComponent itextcomponent : this.getSiblings())
        {
            textcomponentkeybind.appendSibling(itextcomponent.createCopy());
        }

        return textcomponentkeybind;
    }

    public boolean equals(Object obj1)
    {
        if (this == obj1)
        {
            return true;
        }
        else if (!(obj1 instanceof TextComponentKeybind))
        {
            return false;
        }
        else
        {
            TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)obj1;
            return this.text.equals(textcomponentkeybind.text) && super.equals(obj1);
        }
    }

    public String toString()
    {
        return "KeybindComponent{keybind='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String getText()
    {
        return this.text;
    }
}
