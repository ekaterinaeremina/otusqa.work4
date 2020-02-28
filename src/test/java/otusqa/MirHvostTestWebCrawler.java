package otusqa;

import com.opencsv.CSVWriter;
import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.*;
import java.text.*;
import java.util.*;

public class MirHvostTestWebCrawler extends BaseTest{

    private static final Logger log = Logger.getLogger(MirHvostTestWebCrawler.class);
    protected static String CatalogURL = System.getProperty("CatalogURL");
    @Test
    public void runWebCrawler() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
        Date date = new Date();

        driver.get(CatalogURL);
        log.info("Go to " + CatalogURL);

        String csv = "MirHvostCrawler "+dateFormat.format(date)+".csv";
        Product header = new Product();
        header.writeToFile(csv);

        By locShowBy90 = By.xpath("(//a[contains(text(), '90')])");
        findElementWithWaitVisibility(wait,locShowBy90).click();
        log.info("Click show by 90");

        By locCatalog = By.xpath("(//div[@data-type='catalog'])");
        By locNextPage = By.xpath("(//a[@title='Следующая страница'])");
        By locProductList = By.xpath("(//ul[@class='productList'])");
        By locProducts = By.xpath("(//ul[@class='productList']/li)");

        WebElement catalog = findElementWithWaitVisibility(wait, locCatalog);
        WebElement goToNextPage= null;
        WebElement productList;
        List<WebElement> products;
        log.info((Helper.ElementExist(wait,catalog,locNextPage)));
        while ((Helper.ElementExist(wait,catalog,locNextPage)))
        {
            log.info((Helper.ElementExist(wait,catalog,locNextPage)));
            log.info("start while");
            catalog = findElementWithWaitVisibility(wait, locCatalog);
            productList = findElementWithWaitVisibility(catalog, wait, locProductList);
            products = productList.findElements(locProducts);
            log.info("Size = " + products.size());
            goToNextPage = findElementWithWaitVisibility(catalog, wait, locNextPage);
            CrawlerByPage(wait,catalog,goToNextPage,productList,products,csv);
            catalog = findElementWithWaitVisibility(wait, locCatalog);
            goToNextPage = findElementWithWaitVisibility(catalog, wait, locNextPage);
            if (Helper.ElementExist(wait,catalog,locNextPage)) {
                goToNextPage.click();
                log.info("Go to next page");
                catalog = findElementWithWaitVisibility(wait, locCatalog);
            }
        }

        catalog = findElementWithWaitVisibility(wait, locCatalog);
        productList = findElementWithWaitVisibility(catalog, wait, locProductList);
        products = productList.findElements(locProducts);
        CrawlerByPage(wait,catalog,goToNextPage,productList,products,csv);
        log.info("DONE");
    }

    private void CrawlerByPage(WebDriverWait wait, WebElement catalog, WebElement goToNextPage, WebElement productList, List<WebElement> products, String csv)
    {
        By locCatalog = By.xpath("(//div[@data-type='catalog'])");
        By locNextPage = By.xpath("(//a[@title='Следующая страница'])");
        By locProductList = By.xpath("(//ul[@class='productList'])");
        By locProducts = By.xpath("(//ul[@class='productList']/li)");
        for (int i=1; i<=products.size();i++)
        {
            Product product = new Product(i,driver,productList);
            product.writeToFile(csv);
            catalog = findElementWithWaitVisibility(wait, locCatalog);
            if (Helper.ElementExist(wait,catalog,locNextPage)) {
                goToNextPage = findElementWithWaitVisibility(catalog, wait, locNextPage);
            }
            else
                log.info("This is last page");
            productList = findElementWithWaitVisibility(catalog,wait, locProductList);
            products = productList.findElements(locProducts);
        }
    }
}
