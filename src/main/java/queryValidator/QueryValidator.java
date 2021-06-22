package queryValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {
    public boolean QueryValidator() {
        String regex = "";
        String text = "";
        boolean found = false;
        Pattern pattern = Pattern.compile(regex);
        System.out.println("Enter text:");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            found = true;
        }
        return found;
    }
}
