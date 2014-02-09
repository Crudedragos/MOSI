package mosi.display.units;

import java.util.Collections;
import java.util.List;

import mosi.display.DisplayUnitFactory;
import mosi.display.units.DisplayUnit.ActionResult.SimpleAction;
import mosi.utilities.Coord;
import net.minecraft.client.Minecraft;

import com.google.gson.JsonObject;

/**
 * Base Interface for all display
 * 
 * TODO technically it seems not all of these are required for all display, Displayunit and Displaywindow extending from
 * a common interface DisplayBase may be better encapsulation/OOP. To be revisited once more test cases are established.
 */
public interface DisplayUnit {
    /**
     * String type registered to Class object. IMPORTANT: Type is used for Deserialization
     */
    public abstract String getType();

    public abstract Coord getOffset();

    public abstract Coord getSize();

    public enum VerticalAlignment {
        TOP_ABSO, BOTTOM_ABSO, CENTER_ABSO;
    }

    public abstract VerticalAlignment getVerticalAlignment();

    public enum HorizontalAlignment {
        LEFT_ABSO, RIGHT_ABSO, CENTER_ABSO;
    }

    public abstract HorizontalAlignment getHorizontalAlignment();

    public void onUpdate(Minecraft mc, int ticks);

    public boolean shouldRender(Minecraft mc);

    /**
     * @param Position the location this display should render at. Already includes Alignment and Offset which is done
     *            by parent display
     */
    public void renderDisplay(Minecraft mc, Coord position);

    // TODO: Change from mousePOsition to MouseOver similar to MouseAction with enum ENTER, HOVER_MOVE, EXIT
    public SimpleAction mousePosition(Coord localMouse);

    public enum MouseAction {
        /* vararg 0: int EventButton */
        CLICK,
        /* vararg 0: int lastButtonClicked */
        CLICK_MOVE,
        /* No arguments */
        RELEASE;
    }

    /**
     * ActionResults are how DisplayUnits interact with other windows.
     */
    public static interface ActionResult {

        /**
         * Determines when the display hierarchy should stop being processed and ActionResult should be returned to
         * parent.
         */
        public abstract boolean shouldStop();

        /**
         * This ActionResult to passed to the Parent, if it exists. Default implementations return a SimpleAction based
         * on shouldStop(). This is used to tell the parent to stop processing. Advanced uses include having parents
         * close a parent window.
         * 
         * Note: ParentResult is only used when shouldStop returns true.
         */
        public abstract ActionResult parentResult();

        public boolean closeAll();

        public List<DisplayUnit> screensToClose();

        public List<DisplayUnit> screensToOpen();

        public static final SimpleAction NOACTION = new SimpleAction(false);
        public static final SimpleAction SIMPLEACTION = new SimpleAction(true);

        public static final class SimpleAction implements ActionResult {
            public final boolean stopActing;

            private SimpleAction(boolean stopActing) {
                this.stopActing = stopActing;
            }

            @Override
            public final boolean shouldStop() {
                return stopActing;
            }

            @Override
            public final List<DisplayUnit> screensToClose() {
                return Collections.EMPTY_LIST;
            }

            @Override
            public final boolean closeAll() {
                return false;
            }

            @Override
            public final List<DisplayUnit> screensToOpen() {
                return Collections.EMPTY_LIST;
            }

            @Override
            public final SimpleAction parentResult() {
                return this;
            }
        }
    }

    public ActionResult mouseAction(Coord localMouse, MouseAction action, int... actionData);

    public ActionResult keyTyped(char eventCharacter, int eventKey);

    public abstract JsonObject saveCustomData(JsonObject jsonObject);

    public abstract void loadCustomData(DisplayUnitFactory factory, JsonObject customData);
}
