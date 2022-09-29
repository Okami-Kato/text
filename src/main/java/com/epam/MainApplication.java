package com.epam;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.epam.config.TextProperties;

@SpringBootApplication
@EnableConfigurationProperties(TextProperties.class)
public class MainApplication {

	public static void main(String[] args) {
		Application.launch(FxApplication.class, args);
	}
}
