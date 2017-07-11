package reviews;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;


import javafx.stage.Modality;
import reviews.Exceptions.BookNotSelectedException;
import reviews.Exceptions.BooksNotFoundException;
import reviews.Exceptions.EmptySetException;
import reviews.model.Book;
import reviews.model.Review;
import reviews.util.DateUtil;
import reviews.util.WebDriverUtil;
import reviews.view.ChooseBookController;
import reviews.view.OverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private BorderPane rootLayout;
    private Stage primaryStage;
    /**
     * Book reviews - list(observable).
     */
    private ObservableList<Review> reviewData = FXCollections.observableArrayList();
    /**
     * Getter for review list.
     * @return
     */
    public ObservableList<Review> getReviewData() {
        return reviewData;
    }

    /**
     * Constructor.
     */
    public MainApp() {
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BOOK REVIEWS 3000");

        primaryStage.getIcons().add(new Image(("resources/images/icon.png")));

        initRootLayout();

        showOverview();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Giving opportunity to choose the book
     * @param books - books to choose.
     * @return selected book.
     */
    public Book showChooseBookDialog(ObservableList<Book> books) {
        try {
            // Loading fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ChooseBookDialog.fxml"));
            BorderPane page = loader.load();

            // Dialog window Stage.
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image(("resources/images/choice.png")));
            dialogStage.setTitle("Choose book");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ChooseBookController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBooks(books);

            dialogStage.showAndWait();

            return controller.getSelectedBook();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Loading from fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Display the scene containing the root layout.
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows information about the reviews in the root layout.
     */
    public void showOverview() {
        try {
            // Loading from fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Overview.fxml"));
            BorderPane personOverview = loader.load();

            rootLayout.setCenter(personOverview);


            // Giving the controller access to the main application.
            OverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Main search method.
     *
     * @param title
     * @param author
     * @return book title.
     * @throws EmptySetException if there are no such books in the Internet.
     * @throws BookNotSelectedException if user did not select any book.
     */
    public String Search(String title, String author) throws EmptySetException, BookNotSelectedException, BooksNotFoundException {

        HashSet<Book> bookSet;
        Book selectedBook;


        bookSet = WebDriverUtil.findBooks(title, author);
        //TODO: 1 empty string from nowhere sometimes



        /*//test data
        try {
            bookSet.add(new Book("Философия Java", "Брюс Эккель", new URL("http://www.ozon.ru/context/detail/id/30425811/")));
            bookSet.add(new Book("Изучаем Java", "Кэти Сиерра, Берт Бейтс", new URL("http://www.ozon.ru/context/detail/id/7821666/")));
            bookSet.add(new Book("Java. Эффективное программирование", "Джошуа Блох", new URL("http://www.ozon.ru/context/detail/id/21724143/")));
            bookSet.add(new Book("Алгоритмы и структуры данных. Новая версия для Оберона", "Никлаус Вирт", new URL("http://www.ozon.ru/context/detail/id/135854372/")));
            bookSet.add(new Book("Программирование для детей", "Кэрол Вордерман, Джон Вудкок", new URL("http://www.ozon.ru/context/detail/id/32092949/")));
            bookSet.add(new Book("Программирование для детей", "Кэрол Вордерман, джон вудкок", new URL("http://www.ozon.ru/context/detail/id/32092949/")));
        }
        catch (MalformedURLException e) {
            //NOP****************************************************************************************************
        }*/

        switch(bookSet.size())
        {
            case 0:
                throw new EmptySetException("Set is empty");
            case 1:
                selectedBook = bookSet.iterator().next();
                //TODO:selenium****************************************************************
                //test data
                reviewData.clear();
                reviewData.add(new Review("Класс! Супер!", "Петров Вася", "ozon.ru", DateUtil.parse("16.06.2008")));
                reviewData.add(new Review("Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(", "Петров Вася", "ozon.ru", DateUtil.parse("15.06.1919")));
                reviewData.add(new Review("ЫВАРывапрфывдпрмдфыкирамщфывамофолкыук", "Петров Вася", "ozon.ru", DateUtil.parse("17.06.1997")));
                reviewData.add(new Review("Класс! Супер!", "Парень", "chitai-gorod.ru", DateUtil.parse("16.09.1997")));
                reviewData.add(new Review("Класс! Супер!", "seriy2004", "chitai-gorod.ru", DateUtil.parse("22.10.2017")));
                reviewData.add(new Review("Завтра будет жарко", "Михаил Кутузов", "ozon.ru", DateUtil.parse("06.09.1812")));
                reviewData.add(new Review("Класс! Супер!", "Какой-то парень", "ozon.ru", DateUtil.parse("02.12.2016")));
                reviewData.add(new Review("Класс! Супер!", "Даша", "chitai-gorod.ru", DateUtil.parse("11.01.1997")));
                reviewData.add(new Review("Класс! Супер!", "Иван", "ozon.ru", DateUtil.parse("22.11.2017")));
                reviewData.add(new Review("Класс! Супер!", "Санек", "chitai-gorod.ru", DateUtil.parse("16.06.1997")));
                reviewData.add(new Review("Класс! Супер!", "я устал придумывать имена", "ozon.ru", DateUtil.parse("04.06.2000")));
                break;
            default:
                selectedBook = showChooseBookDialog(FXCollections.observableArrayList(bookSet));
                if (selectedBook == null) throw new BookNotSelectedException("Book was not selected.");
                //TODO:selenium****************************************************************
                //test data
                reviewData.clear();
                reviewData.add(new Review("Класс! Супер!", "Петров Вася", "ozon.ru", DateUtil.parse("16.06.2008")));
                reviewData.add(new Review("Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(Не понравилось" +
                        "(Не понравилось(Не понравилось(Не понравилось(", "Петров Вася", "ozon.ru", DateUtil.parse("15.06.1919")));
                reviewData.add(new Review("ЫВАРывапрфывдпрмдфыкирамщфывамофолкыук", "Петров Вася", "ozon.ru", DateUtil.parse("17.06.1997")));
                reviewData.add(new Review("Класс! Супер!", "Парень", "chitai-gorod.ru", DateUtil.parse("16.09.1997")));
                reviewData.add(new Review("Класс! Супер!", "seriy2004", "chitai-gorod.ru", DateUtil.parse("22.10.2017")));
                reviewData.add(new Review("Завтра будет жарко", "Михаил Кутузов", "ozon.ru", DateUtil.parse("06.09.1812")));
                reviewData.add(new Review("Класс! Супер!", "Какой-то парень", "ozon.ru", DateUtil.parse("02.12.2016")));
                reviewData.add(new Review("Класс! Супер!", "Даша", "chitai-gorod.ru", DateUtil.parse("11.01.1997")));
                reviewData.add(new Review("Класс! Супер!", "Иван", "ozon.ru", DateUtil.parse("22.11.2017")));
                reviewData.add(new Review("Класс! Супер!", "Санек", "chitai-gorod.ru", DateUtil.parse("16.06.1997")));
                reviewData.add(new Review("Класс! Супер!", "я устал придумывать имена", "ozon.ru", DateUtil.parse("04.06.2000")));
                break;
        }

        return selectedBook.getTitle();
    }
}




