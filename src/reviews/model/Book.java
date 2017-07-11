package reviews.model;

import com.sun.deploy.util.StringUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import reviews.util.StringUtil;

import java.net.URL;


/**
 * Class-model Book.
 */
public class Book{
    private StringProperty title;
    private StringProperty author;
    private URL url;

    public Book(String title, String author, URL url) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.url = url;
    }

    //Careful methods.
    @Override
    public boolean equals(Object obj) {
        Book that = (Book)obj;
        return StringUtil.equalString(this.getTitle(), that.getTitle()) && StringUtil.equalAuthors(this.getAuthor(), that.getAuthor());
    }
    @Override
    public int hashCode() {
        return StringUtil.hash(title.get()) + 7 * StringUtil.authorHash(author.get());
    }

    //Getters and setters.

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String name) {
        this.title.set(name);
    }

    public StringProperty titleProperty() { return title;}

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() { return author;}

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
