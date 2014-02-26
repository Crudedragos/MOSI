package mosi.display.units;

import java.util.ArrayList;
import java.util.List;

import mosi.display.DisplayUnitFactory;
import mosi.display.inventoryrules.ScrollableSubDisplays;
import mosi.display.resource.SimpleImageResource.GuiIconImageResource;
import mosi.display.units.DisplayUnit.ActionResult;
import mosi.display.units.DisplayUnit.HorizontalAlignment;
import mosi.display.units.DisplayUnit.VerticalAlignment;
import mosi.display.units.action.ReplaceAction;
import mosi.display.units.windows.DisplayUnitButton;
import mosi.display.units.windows.DisplayUnitToggle;
import mosi.display.units.windows.DisplayWindowScrollList;
import mosi.display.units.windows.DisplayUnitButton.Clicker;
import mosi.display.units.windows.button.AddScrollClick;
import mosi.display.units.windows.button.CloseClick;
import mosi.display.units.windows.button.MoveScrollElementToggle;
import mosi.display.units.windows.button.RemoveScrollToggle;
import mosi.utilities.Coord;
import net.minecraft.client.Minecraft;

import com.google.gson.JsonObject;

public class DisplayUnitUnsortedPanel extends DisplayUnitPanel {
    public static final String DISPLAY_ID = "DisplayUnitUnsortedPanel";
    private List<DisplayUnit> childDisplays;

    public DisplayUnitUnsortedPanel() {
        super();
        childDisplays = new ArrayList<DisplayUnit>();
        // MOSI.getDisplayFactory().createDisplay(type, jsonObject)
        childDisplays.add(new DisplayUnitPotion(20, 1));
        childDisplays.add(new DisplayUnitPotion(20, 4));
        childDisplays.add(new DisplayUnitPotion(20, 8));
        childDisplays.add(new DisplayUnitPotion(20, 11));
        childDisplays.add(new DisplayUnitItem());
        childDisplays.add(new DisplayUnitPotion(20, 14));
    }

    @Override
    public String getType() {
        return DISPLAY_ID;
    }

    @Override
    public void update(Minecraft mc, int ticks) {
        for (DisplayUnit displayUnit : childDisplays) {
            displayUnit.onUpdate(mc, ticks);
        }
    }

    @Override
    public List<? extends DisplayUnit> getDisplaysToRender() {
        return childDisplays;
    }

    @Override
    public DisplayUnit getPanelEditor() {
        return new DisplayUnitButton(new Coord(0, 122), new Coord(80, 15), VerticalAlignment.TOP_ABSO,
                HorizontalAlignment.CENTER_ABSO, new Clicker() {
                    private DisplayUnitPanel display;
                    private VerticalAlignment parentVert;
                    private HorizontalAlignment parentHorz;

                    private Clicker init(DisplayUnitPanel display, VerticalAlignment parentVert,
                            HorizontalAlignment parentHorz) {
                        this.display = display;
                        this.parentVert = parentVert;
                        this.parentHorz = parentHorz;
                        return this;
                    }

                    @Override
                    public ActionResult onClick() {
                        return ActionResult.SIMPLEACTION;
                    }

                    @Override
                    public ActionResult onRelease() {
                        ScrollableSubDisplays<DisplayUnit> scrollable = new ScrollableSubDisplays<DisplayUnit>(
                                childDisplays);
                        DisplayWindowScrollList<DisplayUnit> slider = new DisplayWindowScrollList<DisplayUnit>(
                                new Coord(90, 00), new Coord(135, 100), 25, parentVert, parentHorz, scrollable);
                        // Add Element Buttons
                        slider.addElement(new DisplayUnitButton(new Coord(2, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.LEFT_ABSO,
                                new AddScrollClick<DisplayUnit, ScrollableSubDisplays<DisplayUnit>>(scrollable) {

                                    @Override
                                    public void performScrollAddition(ScrollableSubDisplays<DisplayUnit> container) {
                                        container.addElement(new DisplayUnitPotion());
                                    }
                                })
                                .setIconImageResource(new GuiIconImageResource(new Coord(147, 44), new Coord(12, 16))));
                        slider.addElement(new DisplayUnitButton(new Coord(23, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.LEFT_ABSO,
                                new AddScrollClick<DisplayUnit, ScrollableSubDisplays<DisplayUnit>>(scrollable) {

                                    @Override
                                    public void performScrollAddition(ScrollableSubDisplays<DisplayUnit> container) {
                                        container.addElement(new DisplayUnitItem());
                                    }
                                })
                                .setIconImageResource(new GuiIconImageResource(new Coord(165, 44), new Coord(12, 16))));
                        slider.addElement(new DisplayUnitButton(new Coord(44, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.LEFT_ABSO,
                                new AddScrollClick<DisplayUnit, ScrollableSubDisplays<DisplayUnit>>(scrollable) {

                                    @Override
                                    public void performScrollAddition(ScrollableSubDisplays<DisplayUnit> container) {
                                        container.addElement(new DisplayUnitUnsortedPanel());
                                    }
                                })
                                .setIconImageResource(new GuiIconImageResource(new Coord(111, 66), new Coord(12, 15))));
                        // List interactive Buttons - Remove, MoveUp, MoveDown
                        slider.addElement(new DisplayUnitToggle(new Coord(-2, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.RIGHT_ABSO,
                                new RemoveScrollToggle<DisplayUnit>(scrollable))
                                .setIconImageResource(new GuiIconImageResource(new Coord(201, 44), new Coord(13, 16))));
                        slider.addElement(new DisplayUnitToggle(new Coord(-23, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.RIGHT_ABSO,
                                new MoveScrollElementToggle<DisplayUnit>(scrollable, 1))
                                .setIconImageResource(new GuiIconImageResource(new Coord(165, 66), new Coord(12, 15))));
                        slider.addElement(new DisplayUnitToggle(new Coord(-44, 2), new Coord(20, 20),
                                VerticalAlignment.TOP_ABSO, HorizontalAlignment.RIGHT_ABSO,
                                new MoveScrollElementToggle<DisplayUnit>(scrollable, -1))
                                .setIconImageResource(new GuiIconImageResource(new Coord(147, 66), new Coord(12, 15))));
                        slider.addElement(new DisplayUnitButton(new Coord(0, -2), new Coord(60, 20),
                                VerticalAlignment.BOTTOM_ABSO, HorizontalAlignment.CENTER_ABSO, new CloseClick(slider),
                                "Close"));
                        return new ReplaceAction(slider, true);
                    }
                }.init(this, getVerticalAlignment(), getHorizontalAlignment()), "Sub Displays");
    }

    @Override
    public void saveCustomData(JsonObject jsonObject) {
        super.saveCustomData(jsonObject);
    }

    @Override
    public void loadCustomData(DisplayUnitFactory factory, JsonObject customData) {
        super.loadCustomData(factory, customData);
    }
}
