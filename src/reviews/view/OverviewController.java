package reviews.view;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import reviews.Exceptions.BookNotSelectedException;
import reviews.Exceptions.BooksNotFoundException;
import reviews.Exceptions.ReviewsNotFoundException;
import reviews.model.Review;
import reviews.util.DateUtil;
import javafx.fxml.FXML;
import reviews.MainApp;

import java.time.LocalDate;

public class OverviewController {

    //Table items.
    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, String> textColumn;
    @FXML
    private TableColumn<Review, LocalDate> dateColumn;
    @FXML
    private TableColumn<Review, String> resourceColumn;

    //Chosen review items.
    @FXML
    private Label chosenDateLabel;
    @FXML
    private Label chosenSourceLabel;
    @FXML
    private Label chosenAuthorLabel;
    @FXML
    private TextArea chosenText;

    //Search items.
    //***********************************************************************
    @FXML
    private Label bookName;
    @FXML
    private TextField nameField;
    @FXML
    private TextField authorField;
    @FXML
    private Button btnFind;

    // Main app ref.
    private MainApp mainApp;
    /**
     * Running by main app.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Connecting data to review table
        reviewTable.setItems(mainApp.getReviewData());
    }

    /**
     * Constructor.
     */
    public OverviewController() {
    }

    /**
     * Initialization.
     * Running after fxml loading.
     */
    @FXML
    private void initialize() {
        textColumn.setCellValueFactory(
                cellData -> cellData.getValue().textProperty());

        dateColumn.setCellValueFactory(
                cellData -> cellData.getValue().dateProperty());

        resourceColumn.setCellValueFactory(
                cellData -> cellData.getValue().resourceProperty());

        // Cleaning extra review info
        showReviewDetails(null);

        // Adding listener to row choosing.
        reviewTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showReviewDetails(newValue));

        nameField.textProperty().addListener((observable, oldValue, newValue) -> disable());
        authorField.textProperty().addListener((observable, oldValue, newValue) -> disable());
    }

    /**Checking if it is necessary to disable find button.
     *
     * @return
     */
    private void disable() {
        btnFind.setDisable(nameField.getText().isEmpty() && authorField.getText().isEmpty());
    }

    /**
     * Filling fields from review parameter.
     * Review = null - cleaning all fields.
     *
     * @param review — review or null
     */
    private void showReviewDetails(Review review) {
        if (review != null) {
            // Filling.
            chosenAuthorLabel.setText(review.getAuthor());
            chosenSourceLabel.setText(review.getResource());
            chosenText.setVisible(true);
            chosenText.setText(review.getText());
            chosenDateLabel.setText(DateUtil.format(review.getDate()));
        } else {
            // Cleaning.
            chosenDateLabel.setText("");
            chosenSourceLabel.setText("");
            chosenAuthorLabel.setText("");
            chosenText.setVisible(false);
        }
    }

    /**
    * Searching button pressed.
    */
    @FXML
    private void handleSearch() {
        try{
            bookName.setText(mainApp.Search(nameField.getText(), authorField.getText()));
        }
        catch (ReviewsNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ooops");
            alert.setHeaderText("There are no reviews for this book");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("resources/images/alert.png"));

            alert.showAndWait();
        }
        catch(BookNotSelectedException e) {
            //NOP
        }
        catch(BooksNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ooops");
            alert.setHeaderText("There are no such books");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("resources/images/alert.png"));

            alert.showAndWait();
        }

    }


}