package otusqa;

import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

public class Product {

    private static final Logger log = Logger.getLogger(Product.class);
    private  WebDriverWait Wait;
    private WebDriver Driver;
    private String Name;
    private String Price;
    private String Sale;
    private String Link;
    private String Category;
    private String Available;
    private int IdOnPage;
    private WebElement ProductList;

    public Product()
    {
        Name = "Name";
        Price = "Price";
        Sale = "Sale";
        Link = "Link";
        Category = "Category";
        Available = "Available";
    }

    public Product(int id, WebDriver driver, WebElement productList) {
        Driver = driver;
        Wait = new WebDriverWait(Driver, 30);
        IdOnPage = id;
        ProductList = productList;
        Name = getName();
        Price = getPrice();
        Sale = getSale();
        Link = getLink();
        Available = getAvailable();
        By locProducts = By.xpath("(//ul[@class='productList']/li)");
        List<WebElement> products = productList.findElements(locProducts);
        Category = getCategory(products.get(IdOnPage-1));
    }

    private String getAvailable() {
        if (Available == null)
        {
            WebElement available = ProductList.findElement(By.xpath("(//ul[@class='productList']/li[" + IdOnPage + "]/div/div[@class='hr']/span/img)"));
            log.info("Available: " + available.getAttribute("alt"));
            return available.getAttribute("alt");
        }
        else
            return Available;
    }

    public int getIdOnPage() {
        return IdOnPage;
    }

    public String getName() {
        if (Name == null) {
            WebElement name = ProductList.findElement(By.xpath("(//ul[@class='productList']/li[" + IdOnPage + "]/div/a[@class='name'])"));
            log.info("Name: " + name.getAttribute("text"));
            return name.getAttribute("text");
        }
        else return Name;
    }

    public String getPrice() {
        if (Price == null) {
            WebElement price = ProductList.findElement(By.xpath("(//ul[@class='productList']/li[" + IdOnPage + "]/div/span[@class='price'])"));
            log.info("Price: " + price.getText().split("\n")[0]);
            return price.getText().split("\n")[0];
        }
        else
            return Price;
    }

    public String getSale() {
        if (Sale == null) {
            By locSale = By.xpath("(//ul[@class='productList']/li[" + IdOnPage + "]/div/span[@class='amount_sale_abs'])");

            WebElement sale;
            if (Helper.ElementExist(Wait, ProductList, locSale)) {
                sale = ProductList.findElement(locSale);
                log.info("Sale: " + sale.getText());
                return sale.getText();
            } else
            {
                log.info("No sale");
                return "";
            }
        }
        else
            return Sale;
    }

    public String getLink() {
        if (Link == null) {
            WebElement name = ProductList.findElement(By.xpath("(//ul[@class='productList']/li[" + IdOnPage + "]/div/a[@class='name'])"));
            log.info("Link: " + name.getAttribute("href"));
            return name.getAttribute("href");
        }
        else
            return Link;
    }

    public String getCategory(WebElement product)
    {
        if (Category == null) {
            product.click();
            By locCategories = By.xpath("(//ul[@id='breadcrumbs'])");
            Wait.until(ExpectedConditions.visibilityOfElementLocated(locCategories));
            WebElement breadcrumbs = Driver.findElement(locCategories);
            List<WebElement> categories = breadcrumbs.findElements(By.tagName("a"));
            String categoriesStr = "";
            for (WebElement category : categories) {
                categoriesStr += category.getAttribute("title") + "//";
            }
            Driver.navigate().back();
            By locProductList = By.xpath("(//ul[@class='productList'])");
            Wait.until(ExpectedConditions.visibilityOfElementLocated(locProductList));
            log.info("Category: " + categoriesStr);
            return categoriesStr;
        }
        else
            return Category;
    }

    public void writeToFile(String file)
    {
        String [] record = new String[] {Category, Name, Price, Sale, Link, Available};
        try {
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file,true), Charset.forName("Windows-1251")));
            writer.writeNext(record);
            writer.close();
            log.info("Write success");
        }
        catch (IOException e) {
            log.info(e.getMessage());
        }
    }
}
