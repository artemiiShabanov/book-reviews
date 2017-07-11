package reviews.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import reviews.Exceptions.BooksNotFoundException;
import reviews.model.Book;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

/**
 * Supporting functions for working with selenium web driver.
 */
public class WebDriverUtil {

    private static WebDriver driver;
    static{
        /*File file = new File("C:/Selenium/phantomjs.exe");
        System.setProperty("phantomjs.binary.path", file.getAbsolutePath());
        driver = new PhantomJSDriver();*/
        File file = new File("C:/Selenium/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        driver = new ChromeDriver();
    }

    /**Searching books on ozon.ru
     *
     * @param title
     * @param author
     * @return
     */
    public static HashSet<Book> findBooks(String title, String author) throws BooksNotFoundException {
        HashSet<Book> result = new HashSet<>();

        driver.get("http://www.ozon.ru/context/div_book/");
        WebElement searchField = driver.findElement(By.name("SearchText"));
        searchField.sendKeys(title+" "+author);
        driver.findElement(By.className("eMainSearchBlock_ButtonWrap")).click();

        try {
            List<WebElement> list = driver.findElements(By.cssSelector(".bOneTile.inline.jsUpdateLink.mRuble"));
            String tmpTitle;
            String tmpAuthor;
            for (WebElement we: list ) {
                try {
                    tmpTitle = we.findElement(By.cssSelector(".eOneTile_ItemName")).getText();
                }
                catch(Exception e) {
                    tmpTitle = "No title";
                }
                try {
                    tmpAuthor = we.findElement(By.cssSelector(".bOneTileProperty.mPerson")).getText();
                }
                catch(Exception e) {
                    tmpAuthor = "No author";
                }
                result.add(new Book(tmpTitle, tmpAuthor , new URL(we.findElement(By.cssSelector(".eOneTile_tileLink.jsUpdateLink")).getAttribute("href"))));
            }
        }
        catch (Exception e) {
            throw new BooksNotFoundException("There are no such books.");
        }

        //TODO: limit?, many pages, empty author, animation, phantomjs, bugs sometimes, searchField.sendKeys(title+" "+author) is bad


        return result;
    }


}
