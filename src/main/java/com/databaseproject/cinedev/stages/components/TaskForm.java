package com.databaseproject.cinedev.stages.components;

import com.databaseproject.cinedev.CinedevApplication;
import com.databaseproject.cinedev.enums.CategoryTask;
import com.databaseproject.cinedev.enums.StatusTask;
import com.databaseproject.cinedev.models.base.User;
import com.databaseproject.cinedev.models.task.Category;
import com.databaseproject.cinedev.models.task.State;
import com.databaseproject.cinedev.models.task.Task;
import com.databaseproject.cinedev.services.tasks.category.ICategoryService;
import com.databaseproject.cinedev.services.tasks.state.IStateService;
import com.databaseproject.cinedev.services.tasks.task.ITaskService;
import com.databaseproject.cinedev.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskForm {
    private ITaskService taskService;
    private IStateService stateService;
    private ICategoryService categoryService;

    private User user;
    private Task task;
    private Runnable taskAdded;

    public TaskForm(User user, Task task, Runnable taskAdded) {
        this.taskService = CinedevApplication.getSpringContext().getBean(ITaskService.class);
        this.stateService = CinedevApplication.getSpringContext().getBean(IStateService.class);
        this.categoryService = CinedevApplication.getSpringContext().getBean(ICategoryService.class);
        this.user = user;
        this.task = task;
        this.taskAdded = taskAdded;
    }

    public void showFormModal(Stage primaryStage) {
        TextField taskName = new TextField();
        taskName.setPromptText("Name of Task");

        TextArea taskDescription = new TextArea();
        taskDescription.setPromptText("Description Task");

        var defaultCategories = getCategories(CategoryTask.DEFAULT);
        var userCategories = getCategories(CategoryTask.USER);
        ComboBox<Category> comboCategory = new ComboBox<>();
        comboCategory.getItems().addAll(defaultCategories);
        comboCategory.getItems().addAll(userCategories);
        comboCategory.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category categoryItem, boolean empty) {
                super.updateItem(categoryItem, empty);
                setText(empty || categoryItem == null ? null : categoryItem.getName());
            }
        });
        comboCategory.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category categoryItem, boolean empty) {
                super.updateItem(categoryItem, empty);
                setText(empty || categoryItem == null ? null : categoryItem.getName());
            }
        });
        comboCategory.setPromptText("Category");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date of Expiration");

        TextField expirationTime = new TextField();
        expirationTime.setPromptText("Expiration Time");

        TextField assignmentsTask = new TextField();
        assignmentsTask.setPromptText("Assigned by (Separate by comas)");

        ComboBox<StatusTask> comboState = new ComboBox<>();
        comboState.getItems().addAll(StatusTask.values());
        comboState.setPromptText("Status of Task");

        Button addButton = new Button("Agregar Tarea");

        if(task != null) {
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            comboCategory.setValue(task.getCategory());
            datePicker.setValue(task.getExpirationDate());
            expirationTime.setText(task.getExpirationTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            assignmentsTask.setText(task.getAssignments());
            if (task.getState() != null) {
                comboState.setValue(StatusTask.valueOf(task.getState().getName()));
            }

            addButton.setText("Save Changes");
        }

        addButton.setOnAction(e -> {
            String name = taskName.getText();
            String description = taskDescription.getText();
            String expirationTimeText = expirationTime.getText();
            String assignmentsTaskText = assignmentsTask.getText();
            LocalDate expirationDateText = datePicker.getValue();
            Category category = comboCategory.getValue();

            StatusTask statusTask = comboState.getValue();
            State selectedState = stateService.getByName(statusTask.name());

            if (name.isEmpty() || description.isEmpty() || expirationTimeText.isEmpty() ||
                    expirationDateText == null || category == null || selectedState == null) {
                Utils.sendMessage("Please, complete all the fields.");
                return;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime timeExpiration = LocalTime.parse(expirationTimeText.trim(), formatter);

                Task newTask = (task != null) ? task : new Task();

                newTask.setName(name);
                newTask.setDescription(description);
                newTask.setCategory(category);
                newTask.setExpirationDate(expirationDateText);
                newTask.setExpirationTime(timeExpiration);
                newTask.setAssignments(assignmentsTaskText);
                newTask.setState(selectedState);
                newTask.setUser(user);

                taskService.saveTask(newTask);

                if(taskAdded != null) {
                    taskAdded.run();
                }

                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                Utils.sendMessage(task != null ? "Task Save Successfully!" : "Task Edit Successfully!");
            } catch (Exception ex) {
                Utils.sendMessage("Invalid format hour. Use HH:mm.");
            }
        });

        VBox view = new VBox(10, taskName, taskDescription, comboCategory, datePicker, expirationTime, assignmentsTask, comboState, addButton);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);

        Scene modalScene = new Scene(view, 400, 400);
        Stage modalStage = new Stage();
        modalStage.setScene(modalScene);
        modalStage.setTitle("CineDev");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.showAndWait();
    }

    private List<Category> getCategories(CategoryTask type) {
        var allCategories = categoryService.getAllCategories();

        return switch (type) {
            case DEFAULT -> allCategories.stream().filter(Category::isDefault).toList();
            case USER -> allCategories.stream().filter(category -> !category.isDefault() && category.getUserAdminId().getId().equals(user.getId())).toList();
            case ALL -> allCategories;
        };
    }
}
