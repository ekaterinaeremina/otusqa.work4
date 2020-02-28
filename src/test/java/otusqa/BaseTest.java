package otusqa;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static WebDriver driver;
    private static final Logger log = Logger.getLogger(BaseTest.class);
    protected static String browser = System.getProperty("browser").toUpperCase();

    @BeforeClass
    public static void setUp()
    {
        driver = WebDriverFactory.create(WebDriverName.valueOf(browser));
        driver.manage().window().maximize();
        System.out.println(System.getProperty("browser"));
        log.info("Setup driver");
    }

    protected WebElement findElementWithWaitVisibility(WebDriverWait wait, By by)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return driver.findElement(by);
    }

    protected WebElement findElementWithWaitVisibility(WebDriverWait wait, By byWait, By by)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(byWait));
        return driver.findElement(by);
    }

    protected WebElement findElementWithWaitVisibility(WebElement elem, WebDriverWait wait, By by)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return elem.findElement(by);
    }

    protected void inputTextToElementWithWaitVisibility(WebDriverWait wait, By by, String text)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }

    protected void inputTextToElementWithWaitVisibility(WebDriverWait wait, By byWait, By by, String text)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(byWait));
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }

    protected void inputTextToElementWithWaitVisibility(WebElement elem, WebDriverWait wait, By by, String text)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        elem.findElement(by).clear();
        elem.findElement(by).sendKeys(text);
    }

    @AfterClass
    public static void tearDown()
    {
        if (driver!=null) {
            driver.quit();
            driver = null;
        }
        log.info("Driver quit");
    }
}
