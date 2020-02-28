package otusqa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Helper {

    public static boolean ElementExist(WebDriverWait wait, WebElement parent, By by)
    {
        if (parent.findElements(by).size()>0)
            return  true;
        else
            return false;
    }
}
