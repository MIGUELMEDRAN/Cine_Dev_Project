package com.databaseproject.cinedev.utils;

import com.databaseproject.cinedev.stages.IWindowScene;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Utils {
    public static void sendMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CineDev - INFORMATION");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void sendMessageError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CineDev - ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String recognizeTypeOfEmail(String email) {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "invalid";
        }

        String lowerMail = email.toLowerCase();
        if(lowerMail.endsWith("@gmail.com")) {
            return "Gmail";
        } else if (lowerMail.endsWith("@outlook.com")) {
            return "Outlook";
        } else if (lowerMail.endsWith("@hotmail.com") || lowerMail.endsWith("@hotmail.es")) {
            return "Hotmail";
        } else {
            return "unknown";
        }
    }

    public static boolean isEmailValidAndRecognized(String email) {
        String emailType = recognizeTypeOfEmail(email);
        if(emailType.equals("invalid")) {
            sendMessage("The entered email is not valid.");
            return false;
        } else if(email.equals("unknown")) {
            sendMessage("Unrecognized email domain. Only Gmail, Outlook or Hotmail is allowed.");
            return false;
        } else {
            return true;
        }
    }

    public static StackPane logoCine(double width) {
        Image logo = new Image(Utils.class.getResourceAsStream("/images/cineDevLogo.png"));
        ImageView logoView = new ImageView(logo);

        logoView.setFitWidth(width);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(false);
        logoView.setCache(true);

        return new StackPane(logoView);
    }

    public static Background backgroundImageWindows() {
        return new Background(
                new BackgroundImage(
                    new Image(Utils.class.getResource("/images/background.png").toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )
        );
    }

    public static void loadWindowsToShow(IWindowScene windowScene, Stage primaryStage) {
        Scene scene = windowScene.showWindow(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void removeErrorStyleOnTyping(TextField field) {
        field.textProperty().addListener((obs, oldText, newText) -> {
            field.setStyle(null);
        });
    }

    public static boolean confirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("CineDev - Confirmation");
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    public static String formatDate(LocalDateTime date) {
        if (date == null) return "No definido";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
