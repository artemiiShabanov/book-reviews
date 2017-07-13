package reviews.util;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import reviews.Exceptions.BooksNotFoundException;
import reviews.Exceptions.ReviewsNotFoundException;
import reviews.model.Book;
import reviews.model.Review;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
        List<WebElement> list;

        driver.get("http://www.ozon.ru/context/div_book/");
        WebElement searchField = driver.findElement(By.name("SearchText"));
        searchField.sendKeys(title+" "+author);
        driver.findElement(By.className("eMainSearchBlock_ButtonWrap")).click();

        try {
            driver.findElement(By.className("eZeroSearch_Top"));
            throw new BooksNotFoundException("There are no such books.");
        }
        catch (NoSuchElementException e) {
            scrollDown(driver);

            list = driver.findElements(By.cssSelector(".bOneTile.inline.jsUpdateLink"));

            for (WebElement we: list ) {
                Book b = createBookFromWE(we);
                if (b != null) {
                    result.add(b);
                }
            }
        }


        //TODO: limit?, animation, phantomjs, searchField.sendKeys(title+" "+author) is bad


        return result;
    }

    /**Supporting function to scroll down ang get all the books.
     *
     */
    private static void scrollDown(WebDriver driver) {
        try {
            List<WebElement> list;
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            for (int i = 0; i < 3; i++) {
                js.executeScript("window.scrollTo(0,document.body.scrollHeight);");
                Thread.sleep(150);
            }

            int total = Integer.parseInt(driver.findElement(By.className("eTileSeparator_Total")).getText());
            list = driver.findElements(By.cssSelector("div span.eTileSeparator_Text:last-child"));
            while (Integer.parseInt(list.get(list.size() - 1).getText().split(" ")[1]) != total) {
                list = driver.findElements(By.cssSelector("div span.eTileSeparator_Text:last-child"));
                js.executeScript("window.scrollTo(0,document.body.scrollHeight);");
                Thread.sleep(150);
            }
        }
        catch (Exception e) {
            //NOP
        }
    }

    /**Supporting function to build book
     *
     * @param we
     * @return
     */
    private static Book createBookFromWE(WebElement we) {
        String tmpTitle;
        String tmpAuthor;
        try {
            tmpTitle = we.findElement(By.cssSelector(".eOneTile_ItemName")).getText();
        }
        catch(Exception e) {
            tmpTitle = "No title";
        }
        if (tmpTitle == "") {
            tmpTitle = "No author";
        }
        try {
            tmpAuthor = we.findElement(By.cssSelector(".bOneTileProperty.mPerson")).getText();
        }
        catch(Exception e) {
            tmpAuthor = "No author";
        }
        if (tmpAuthor == "") {
            tmpAuthor = "No author";
        }
        try {
            return new Book(tmpTitle, tmpAuthor, new URL(we.findElement(By.cssSelector(".eOneTile_tileLink.jsUpdateLink")).getAttribute("href")));
        }
        catch (MalformedURLException e) {
            //impossible?
            return null;
        }
    }

    public static ArrayList<Review> loadReviewsOzon(URL url)/* throws ReviewsNotFoundException*/ {
        ArrayList<Review> result = new ArrayList<>();
        driver.get(url.toString());
        try{
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("window.scrollTo(0,document.body.scrollHeight);");
            Thread.sleep(150);
            driver.get(driver.findElement(By.cssSelector("a.eCommentsHeader_Title_Link")).getAttribute("href"));
            List<WebElement> list = driver.findElements(By.cssSelector(".bComment.jsComment"));
            for (WebElement we : list) {
                result.add(new Review(we.findElement(By.className("eComment_Text_Text")).getText() ,we.findElement(By.className("eComment_Info_Username_Link")).getText(), "ozon.ru", DateUtil.parseOzon(we.findElement(By.className("eComment_Info_Date")).getText())));
            }
        }
        catch (NoSuchElementException e) {
            /*throw new ReviewsNotFoundException();*/
            return result;
        }
        catch (InterruptedException e) {
            //NOP
        }

        return result;
    }

}
