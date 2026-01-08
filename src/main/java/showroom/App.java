package showroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icon/logo.jpg")));
            primaryStage.setTitle("Showroom Management System");
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        showroom.util.HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
