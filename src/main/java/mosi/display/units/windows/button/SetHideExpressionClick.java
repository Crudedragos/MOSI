package mosi.display.units.windows.button;

import mosi.display.hiderules.HideExpression;
import mosi.display.units.DisplayUnit.ActionResult;
import mosi.display.units.windows.DisplayUnitButton.Clicker;

public class SetHideExpressionClick implements Clicker {

    private HideExpression hideExpression;
    private String expression = "";

    public SetHideExpressionClick(HideExpression hideExpression, String expression) {
        this.hideExpression = hideExpression;
        if (!hideExpression.isExpressionValid(expression)) {
            throw new IllegalArgumentException(String.format("Invalid expressions %s for HE %s",
                    expression != null ? expression : "{NULL}", hideExpression));
        }
        this.expression = expression;
    }

    @Override
    public ActionResult onClick() {
        return ActionResult.SIMPLEACTION;
    }

    @Override
    public ActionResult onRelease() {
        hideExpression.setExpression(expression);
        return ActionResult.SIMPLEACTION;
    }
}
