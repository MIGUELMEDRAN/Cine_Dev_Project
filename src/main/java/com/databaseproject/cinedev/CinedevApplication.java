package com.databaseproject.cinedev;

import com.databaseproject.cinedev.stages.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CinedevApplication extends Application {
	@Getter
    private static ConfigurableApplicationContext springContext;

	public static void main(String[] args) {
		Application.launch(CinedevApplication.class, args);
	}

	@Override
	public void init() {
		springContext = new SpringApplicationBuilder(CinedevApplication.class).run();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginPage login = new LoginPage();
		Scene scene = login.showWindow(primaryStage);

		primaryStage.setTitle("CineDev");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() {
		springContext.close();
	}

}
