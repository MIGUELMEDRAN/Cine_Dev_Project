package com.databaseproject.cinedev.stages.components.forms;

import com.databaseproject.cinedev.CinedevApplication;
import com.databaseproject.cinedev.models.base.User;
import com.databaseproject.cinedev.models.task.Category;
import com.databaseproject.cinedev.services.base.user.UserService;
import com.databaseproject.cinedev.services.base.userRole.UserRoleService;
import com.databaseproject.cinedev.services.tasks.category.CategoryService;
import com.databaseproject.cinedev.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CategoryForm {
    private User user;

    private final CategoryService categoryService;
    private final UserRoleService userRoleService;
    private final UserService userService;

    private final TextField nameField = new TextField();
    private final ComboBox<String> statusCombo = new ComboBox<>();
    private final CheckBox defaultCheck = new CheckBox();
    private final TextArea descriptionArea = new TextArea();
    private final DatePicker datePicker = new DatePicker(LocalDate.now());

    public CategoryForm(User user) {
        this.categoryService = CinedevApplication.getSpringContext().getBean(CategoryService.class);
        this.userService = CinedevApplication.getSpringContext().getBean(UserService.class);
        this.userRoleService = CinedevApplication.getSpringContext().getBean(UserRoleService.class);
        this.user = userService.getUserWithRolesById(user.getId());
    }

    public void showFormModal(Stage primaryStage) {
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-font-family: 'Arial';");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPromptText("Name of category");

        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-font-family: 'Arial';");
        statusCombo.getItems().addAll("Active", "Inactive");
        statusCombo.setValue("Active");
        statusCombo.setMaxWidth(Double.MAX_VALUE);

        Label defaultLabel = new Label("¿Is default?");
        defaultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-font-family: 'Arial';");
        descriptionArea.setPromptText("Description of the category");
        descriptionArea.setMaxWidth(Double.MAX_VALUE);

        Label dateLabel = new Label("Creation date:");
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-font-family: 'Arial';");
        datePicker.setMaxWidth(Double.MAX_VALUE);

        Button saveButton = new Button("Add Category");
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String status = statusCombo.getValue();
            boolean isDefault = defaultCheck.isSelected();
            String description = descriptionArea.getText().trim();
            LocalDate date = datePicker.getValue();

            if (name.isEmpty() || status == null || description.isEmpty() || date == null) {
                Utils.sendMessage("Please, complete all the fields.", Alert.AlertType.INFORMATION);
                return;
            }

            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setDefault(isDefault);
            category.setState(status);
            category.setUserAdminId(user);
            category.setCreatedAt(LocalDateTime.now());

            categoryService.addCustomCategory(category);

            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            Utils.sendMessage("Category saved successfully!", Alert.AlertType.INFORMATION);
        });

        Separator separator = new Separator();
        separator.setPadding(new Insets(20, 0, 10, 0));

        Label categoriesLabel = new Label("All Categories");
        categoriesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: 'Arial';");

        VBox categoryView = new VBox(5);
        categoryView.setPadding(new Insets(10, 15, 10 , 15));
        categoryView.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 1px 0 0 0;");

        for (Category category : categoryService.getAllCategories()) {
            HBox item = new HBox(10);
            item.setSpacing(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(5, 0, 5, 0));

            Label nameItem = new Label(category.getName().toUpperCase());
            nameItem.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
            HBox.setHgrow(nameItem, Priority.ALWAYS);
            nameItem.setMaxWidth(Double.MAX_VALUE);

            nameItem.setStyle("-fx-font-weight: bold;");
            HBox.setHgrow(nameItem, Priority.ALWAYS);

            Button deleteButton = new Button(category.isDefault() ? "Default" : "Delete");
            deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
            deleteButton.setDisable(category.isDefault());

            deleteButton.setOnAction(e -> {
                categoryService.removeCustomCategory(category);
                categoryView.getChildren().remove(item);

                Utils.sendMessage("Category " + category.getName().toUpperCase() + " deleted.", Alert.AlertType.INFORMATION);
            });

            item.getChildren().addAll(nameItem, deleteButton);
            categoryView.getChildren().add(item);
        }

        ScrollPane scrollPane = new ScrollPane(categoryView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(
                nameLabel, nameField,
                statusLabel, statusCombo
        );

        if(userRoleService.isAdmin(user.getId())) {
            view.getChildren().addAll(defaultLabel, defaultCheck);
        }

        view.getChildren().addAll(
                descriptionLabel, descriptionArea,
                dateLabel, datePicker,
                saveButton, separator,
                categoriesLabel, scrollPane
        );

        Scene modalScene = new Scene(view, 420, 600);
        Stage modalStage = new Stage();
        modalStage.setScene(modalScene);
        modalStage.setTitle("CineDev");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.showAndWait();
    }
}
