package space.ahaha.project.Utils;

/**
 * Created by lf729 on 2016/12/4.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PasswordFormatCheckUtils {
    public static boolean isPwdLegal(String str) throws PatternSyntaxException {
        String regExp = "^[0-9a-zA-Z]{6,12}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


}