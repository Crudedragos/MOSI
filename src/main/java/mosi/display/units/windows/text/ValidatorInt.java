package mosi.display.units.windows.text;

import net.minecraft.util.ChatAllowedCharacters;
import mosi.display.units.windows.DisplayUnitTextField.Validator;
import mosi.utilities.StringHelper;

public abstract class ValidatorInt implements Validator {

    @Override
    public boolean isCharacterValid(char eventCharacter) {
        return ('-' == eventCharacter || Character.isDigit(eventCharacter))
                && ChatAllowedCharacters.isAllowedCharacter(eventCharacter);
    }

    @Override
    public boolean isStringValid(String text) {
        return StringHelper.isInteger(text);
    }
}