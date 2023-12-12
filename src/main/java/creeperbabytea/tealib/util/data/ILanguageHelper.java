package creeperbabytea.tealib.util.data;

import creeperbabytea.tealib.util.IModResourceHelper;

public interface ILanguageHelper extends IModResourceHelper {
    default String format(String in) {
        char[] chars = in.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '_')
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            else
                result.append(chars[i]);
        }
        return result.toString();
    }
}
