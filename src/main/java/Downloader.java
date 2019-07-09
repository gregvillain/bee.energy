import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//comment the above line and uncomment below line to use Chrome
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Downloader {


    public static void main(String[] args) {
        // declaration and instantiation of objects/variables
        //System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
        //WebDriver driver = new FirefoxDriver();
        //comment the above 2 lines and uncomment below 2 lines to use Chrome
        System.setProperty("webdriver.chrome.driver","/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();
        String baseUrl = "https://www.aemo.com.au/Electricity/National-Electricity-Market-NEM/Data-dashboard";
        driver.get(baseUrl);

        //WebDriverWait wait = new WebDriverWait(driver, new TimeSpan(0, 1, 0));
        //wait.Until(d => d.FindElement(By.Id("Id_Your_UIElement"));

        String expectedTitle = "Welcome: Mercury Tours";
        String actualTitle = "";

        // launch Fire fox and direct it to the Base URL

        // get the actual value of the title
        actualTitle = driver.getTitle();

        System.out.println("AT: " + actualTitle);

        //WebElement button = ((ChromeDriver) driver).findElementByCssSelector("[au-target-id=\"36\"]");

        List<WebElement> buttons = ((ChromeDriver) driver).findElementsByClassName("au-target");
        System.out.println(buttons.get(0).getText());
        //button.click();
        //i class="icon-download"

        /*
         * compare the actual title of the page with the expected one and print
         * the result as "Passed" or "Failed"
         */
        if (actualTitle.contentEquals(expectedTitle)){
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed");
        }

        //close Fire fox
        driver.close();

    }

}