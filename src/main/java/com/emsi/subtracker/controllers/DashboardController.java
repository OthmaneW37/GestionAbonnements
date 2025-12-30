package com.emsi.subtracker.controllers;

import com.emsi.subtracker.dao.SubscriptionDAO;
import com.emsi.subtracker.models.Subscription;
import com.emsi.subtracker.models.SubscriptionTemplate;
import com.emsi.subtracker.models.User;
import com.emsi.subtracker.services.CsvService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {

    @FXML
    private TableView<Subscription> subscriptionTable;
    @FXML
    private TableColumn<Subscription, String> colName;
    @FXML
    private TableColumn<Subscription, Double> colPrice;
    @FXML
    private TableColumn<Subscription, LocalDate> colDate;
    @FXML
    private TableColumn<Subscription, String> colCategory;
    @FXML
    private TableColumn<Subscription, String> colFrequency;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;

    private User currentUser;
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    private final CsvService csvService = new CsvService();
    private ObservableList<Subscription> masterData = FXCollections.observableArrayList();
    private List<SubscriptionTemplate> allTemplates;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();

        // Load templates
        allTemplates = csvService.loadTemplates();
        List<String> categories = csvService.getUniqueCategories(allTemplates);
        categories.add(0, "All Categories");
        categoryFilter.setItems(FXCollections.observableArrayList(categories));
        categoryFilter.getSelectionModel().selectFirst();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadData();
    }

    private void loadData() {
        if (currentUser == null)
            return;
        masterData.setAll(subscriptionDAO.getAllSubscriptions(currentUser.getId()));

        FilteredList<Subscription> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sub -> filter(sub, newValue, categoryFilter.getValue()));
        });

        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(sub -> filter(sub, searchField.getText(), newVal));
        });

        subscriptionTable.setItems(filteredData);
    }

    private boolean filter(Subscription sub, String searchText, String category) {
        boolean matchesSearch = true;
        if (searchText != null && !searchText.isEmpty()) {
            String lowerCaseFilter = searchText.toLowerCase();
            if (sub.getName().toLowerCase().contains(lowerCaseFilter))
                matchesSearch = true;
            else
                matchesSearch = sub.getCategory().toLowerCase().contains(lowerCaseFilter);
        }

        boolean matchesCategory = true;
        if (category != null && !category.equals("All Categories")) {
            matchesCategory = sub.getCategory().equals(category);
        }

        return matchesSearch && matchesCategory;
    }

    private void setupTableColumns() {
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrice()));
        colDate.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDateStart()));
        colCategory.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
        colFrequency.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFrequency()));
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) subscriptionTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stats.fxml"));
            Parent root = loader.load();
            StatsController controller = loader.getController();
            controller.initData(masterData, currentUser);
            Stage stage = (Stage) subscriptionTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        showPremiumAddDialog(null);
    }

    @FXML
    private void handleEdit() {
        Subscription selected = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a subscription first.");
            return;
        }
        showPremiumAddDialog(selected);
    }

    @FXML
    private void handleDelete() {
        Subscription selected = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a subscription to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Subscription");
        alert.setHeaderText("Delete " + selected.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            subscriptionDAO.deleteSubscription(selected.getId());
            loadData();
        }
    }

    private void showPremiumAddDialog(Subscription sub) {
        Dialog<Subscription> dialog = new Dialog<>();
        dialog.setTitle(sub == null ? "New Subscription" : "Edit Subscription");
        dialog.setHeaderText(null);
        dialog.setResizable(true); // Allow resizing
        dialog.getDialogPane().setMinHeight(600); // Force larger height
        dialog.getDialogPane().setMinWidth(500); // Force larger width

        // Inject Stylesheet
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        // UI Components
        ComboBox<String> categorySelector = new ComboBox<>();
        List<String> rawCats = csvService.getUniqueCategories(allTemplates);
        categorySelector.setItems(FXCollections.observableArrayList(rawCats));
        categorySelector.setPromptText("Select a Category first...");
        categorySelector.setMaxWidth(Double.MAX_VALUE);
        categorySelector.getStyleClass().add("combo-box");

        FlowPane templatesPane = new FlowPane();
        templatesPane.setHgap(10);
        templatesPane.setVgap(10);
        templatesPane.setPrefWrapLength(300);

        TextField nameField = new TextField();
        nameField.setPromptText("Subscription Name");
        nameField.getStyleClass().add("text-field");

        TextField priceField = new TextField();
        priceField.setPromptText("Price (€)");
        priceField.getStyleClass().add("text-field");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("text-field"); // Reuse text-field style for consistency

        ComboBox<String> freqBox = new ComboBox<>(FXCollections.observableArrayList("Monthly", "Yearly"));
        freqBox.setValue("Monthly");
        freqBox.getStyleClass().add("combo-box");

        // Logic for Template Selection
        categorySelector.setOnAction(e -> {
            templatesPane.getChildren().clear();
            String selectedCat = categorySelector.getValue();
            if (selectedCat != null) {
                List<SubscriptionTemplate> relevant = allTemplates.stream()
                        .filter(t -> t.getCategory().equals(selectedCat))
                        .collect(Collectors.toList());

                for (SubscriptionTemplate t : relevant) {
                    Button card = new Button(t.getName());
                    // Dynamic style for buttons
                    card.setStyle("-fx-background-color: " + t.getHexColor()
                            + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
                    card.setPrefWidth(120);
                    card.setMinHeight(40);
                    card.setOnAction(evt -> {
                        nameField.setText(t.getName());
                        priceField.setText(String.valueOf(t.getDefaultPrice()));
                    });
                    templatesPane.getChildren().add(card);
                }
            }
        });

        // Pre-fill if editing
        if (sub != null) {
            nameField.setText(sub.getName());
            priceField.setText(String.valueOf(sub.getPrice()));
            datePicker.setValue(sub.getDateStart());
            categorySelector.setValue(sub.getCategory());
            freqBox.setValue(sub.getFrequency());
        }

        // Layout
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #24243e;"); // Ensure background match

        Label step1 = new Label("1. Choose Category:");
        step1.getStyleClass().add("dialog-label");
        Label step2 = new Label("2. Pick Service (Optional):");
        step2.getStyleClass().add("dialog-label");
        Label step3 = new Label("3. Details:");
        step3.getStyleClass().add("dialog-label");

        form.getChildren().addAll(
                step1, categorySelector,
                step2, templatesPane,
                new Separator(),
                step3, nameField, priceField, datePicker, freqBox);

        dialog.getDialogPane().setContent(form);

        ButtonType saveType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        // Find buttons and style them
        Button saveBtn = (Button) dialog.getDialogPane().lookupButton(saveType);
        saveBtn.getStyleClass().add("action-button-primary");
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.getStyleClass().add("action-button-secondary");

        dialog.setResultConverter(b -> {
            if (b == saveType) {
                try {
                    return new Subscription(
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            datePicker.getValue(),
                            categorySelector.getValue(),
                            freqBox.getValue(),
                            currentUser.getId());
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        });

        // Save logic
        dialog.showAndWait().ifPresent(result -> {
            if (sub != null)
                result.setId(sub.getId());

            if (sub == null)
                subscriptionDAO.addSubscription(result);
            else
                subscriptionDAO.updateSubscription(result);

            loadData();
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
