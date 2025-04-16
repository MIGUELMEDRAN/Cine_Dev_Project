package com.databaseproject.cinedev.stages.components;

import com.databaseproject.cinedev.CinedevApplication;
import com.databaseproject.cinedev.models.base.User;
import com.databaseproject.cinedev.services.tasks.task.ITaskService;
import com.databaseproject.cinedev.stages.LoginPage;
import com.databaseproject.cinedev.stages.TaskPage;
import com.databaseproject.cinedev.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class NavBar extends HBox {
    ITaskService taskService;

    public NavBar(User user, Stage primaryStage) {
        this.taskService = CinedevApplication.getSpringContext().getBean(ITaskService.class);

        setPadding(new Insets(0, 15, 0, 15));
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-background-color: transparent;");

        StackPane logo = Utils.logoCine(100);

        HBox space = new HBox();
        space.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(space, Priority.ALWAYS);

        Label welcomeLabel = new Label("Welcome, " + user.getFullName());
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button showTableTask = new Button("Tasks".toUpperCase());
        showTableTask.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        showTableTask.setMaxWidth(Double.MAX_VALUE);

        showTableTask.setOnAction(e -> {
            Utils.loadWindowsToShow(new TaskPage(user, taskService), primaryStage);
            Utils.sendMessage(user.getFullName() + "'s tasks loaded successfully!");
        });

        Button logOut = new Button("Log Out".toUpperCase());
        logOut.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
        logOut.setMaxWidth(Double.MAX_VALUE);

        logOut.setOnAction(e -> {
            Utils.loadWindowsToShow(new LoginPage(), primaryStage);
            Utils.sendMessage("Good Bye, " + user.getFullName() + "! Thanks you for use Cinedev. See you soon!");
        });

        getChildren().addAll(logo, space, welcomeLabel, showTableTask, logOut);
    }
}
