package mosi.display.units;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import mosi.DefaultProps;
import mosi.display.DisplayRenderHelper;
import mosi.utilities.Coord;

public abstract class DisplayUnitCounter extends DisplayUnitMoveable implements DisplayUnitCountable {
    private static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
    private static final ResourceLocation countdown = new ResourceLocation(DefaultProps.mosiKey, "countdown.png");

    private boolean displayAnalogBar;
    private boolean displayNumericCounter;
    private Coord analogOffset;
    private Coord digitalOffset;
    private int textDisplayColor;

    public DisplayUnitCounter(Coord offset, boolean displayAnalogBar, boolean displayNumericCounter) {
        super(offset);
        this.displayAnalogBar = displayAnalogBar;
        this.displayNumericCounter = displayNumericCounter;
        this.analogOffset = new Coord(1, 18);
        this.digitalOffset = new Coord(1, 18);
        this.textDisplayColor = 1030655;
    }

    @Override
    public boolean isAnalogEnabled() {
        return displayAnalogBar;
    }

    @Override
    public void enableAnalogDisplay(boolean enable) {
        displayAnalogBar = enable;
    }

    @Override
    public void setAnalogOffset(Coord coord) {
        analogOffset = coord;
    }

    @Override
    public Coord getAnalogOffset() {
        return analogOffset;
    }

    @Override
    public boolean isDigitalEnabled() {
        return displayNumericCounter;
    }

    @Override
    public void enableDigitalCounter(boolean enable) {
        displayNumericCounter = enable;
    }

    @Override
    public void setDigitalOffset(Coord coord) {
        digitalOffset = coord;
    }

    @Override
    public Coord getDigitalOffset() {
        return digitalOffset;
    }

    /**
     * Used to Draw Analog Bar.
     * 
     * @param mc The Minecraft Instance
     * @param centerOfDisplay The Center Position where the bar needs to be offset From.
     * @param analogValue The value representing how full the Bar is
     * @param analogMax The value that represents the width of the full bar.
     */
    protected void renderAnalogBar(Minecraft mc, Coord centerOfDisplay, Coord offSet, int analogValue, int analogMax) {
        mc.renderEngine.bindTexture(countdown);
        int scaledValue = scaleAnalogizeValue(analogValue, analogMax);
        DisplayRenderHelper.drawTexturedModalRect(Tessellator.instance, 10.0f, centerOfDisplay.x + offSet.x,
                centerOfDisplay.z + offSet.z, 0, 0, 16, 3);
        if (scaledValue > 9) {
            DisplayRenderHelper.drawTexturedModalRect(Tessellator.instance, 10.0f, centerOfDisplay.x + offSet.x,
                    centerOfDisplay.z + offSet.z, 0, 3, scaledValue, 3);
        } else if (scaledValue > 4) {
            DisplayRenderHelper.drawTexturedModalRect(Tessellator.instance, 10.0f, centerOfDisplay.x + offSet.x,
                    centerOfDisplay.z + offSet.z, 0, 6, scaledValue, 3);
        } else {
            DisplayRenderHelper.drawTexturedModalRect(Tessellator.instance, 10.0f, centerOfDisplay.x + offSet.x,
                    centerOfDisplay.z + offSet.z, 0, 9, scaledValue, 3);
        }
    }

    /**
     * Scale a tracked value from range [0-analogMax] to fit the display bars resolution of [0-16]
     */
    private int scaleAnalogizeValue(int analogValue, int analogMax) {
        if (analogValue > analogMax) {
            analogValue = analogMax;
        }
        if (analogValue < 0) {
            analogValue = 0;
        }
        return (int) ((float) (analogValue) / (float) (analogMax) * 18);
    }

    /**
     * Used to Draw Analog Bar.
     * 
     * @param mc The Minecraft Instance
     * @param fontRenderer The fontRenderer
     * @param centerOfDisplay The Center Position where the bar is offset From.
     * @param analogValue The value representing how full the Bar is
     * @param analogMax The value that represents the width of the full bar.
     */
    protected void renderCounterBar(Minecraft mc, Coord centerOfDisplay, Coord offSet, int counterAmount) {
        int totalSeconds = counterAmount / 20;

        /* Get Duration in Seconds */
        int seconds = totalSeconds % 60;
        /* Get Duration in Minutes */
        int minutes = (totalSeconds / 60) % 60;
        String formattedTime;
        if (seconds < 10) {
            formattedTime = Integer.toString(minutes);
        } else if (minutes == 0) {
            formattedTime = String.format("%02d", seconds);
        } else {
            formattedTime = minutes + ":" + String.format("%02d", seconds);
        }

        String displayAmount = Integer.toString(counterAmount);
        // 8 is constant chosen by testing to keep the displaystring roughly center. It just works.
        mc.fontRenderer.drawString(formattedTime,
                centerOfDisplay.x + (8 - mc.fontRenderer.getStringWidth(formattedTime) / 2) + offSet.x,
                centerOfDisplay.z + offSet.z, textDisplayColor);
    }
}