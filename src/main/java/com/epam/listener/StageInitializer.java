package com.epam.listener;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.epam.FxApplication.StageReadyEvent;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("classpath:/static/view.fxml")
    private Resource fxResource;

    @Autowired
    private ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(fxResource.getURL());
        fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
        Parent parent = fxmlLoader.load();

        Stage stage = event.getStage();
        Scene scene = new Scene(parent, 400, 500);
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }
}
