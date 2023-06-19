package de.spc.installer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.util.Optional;

public class Installer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 100);
        scene.getStylesheets().add(getClass().getResource("progress.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Installing the Space Client...");

        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warung!");
            alert.setHeaderText("Es sieht so aus, als würdest du das Fenster schließen wollen...");
            alert.setContentText("Wenn du auf \"Ok\" klickst, wird die Installation des Space Client abgebochen und das Fenster schließt sich. Wenn du dies nicht beabsichtigt hast, klick auf \"Abbrechen\".");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.close();
                System.out.println("Quitet SPC installer");
                System.exit(0);
                return;
            }

            event.consume();
        });

        ColoredProgressBar progressBar = new ColoredProgressBar("green-bar", -1);
        progressBar.setMinWidth(scene.getWidth() - 50);
        progressBar.setMinHeight(scene.getHeight() - 40);
        progressBar.setTranslateX(25);
        progressBar.setTranslateY(20);

        root.getChildren().add(progressBar);

        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted())
                progressBar.setProgress(Downloader.percentage / 100.0D);
        });
        t1.setName("Progressbar Update Thread");
        t1.start();

        Thread t2 = new Thread(() -> {
            Downloader.downloadJar();
            Downloader.downloadJson();
            MojangProfile.install();
            t1.interrupt();
            Thread.currentThread().interrupt();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Download Abgeschlossen");
                alert.setHeaderText("Der Space Client wurde erfolgreich installiert.");
                alert.setContentText("Du kannst nu den Offiziellen Minecraft Launcher öffnen und unseren Client spielen, Viel Spaß! (Du musst die installation hinzufügen)\n~ Space Client Development");

                alert.showAndWait();
                stage.close();
                System.exit(0);
            });
        });
        t2.setName("Download Thread");
        t2.start();

        stage.setResizable(false);
        stage.show();
    }

    private static class ColoredProgressBar extends ProgressBar {

        ColoredProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
