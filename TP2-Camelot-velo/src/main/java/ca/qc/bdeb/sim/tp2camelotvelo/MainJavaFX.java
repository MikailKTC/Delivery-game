package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFX extends Application {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 580;
    private Partie partie = new Partie();

    @Override
    public void start(Stage stage) throws IOException {

        var root = new Pane();

        var scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        scene.setOnKeyPressed(e -> {
            Input.setKeyPressed(e.getCode(), true);

            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }

            if (e.getCode() == KeyCode.Q) {
                partie.ajouterJournauxDebug();
            }

            if (e.getCode() == KeyCode.K) {
                partie.setJournauxZeroDebug();
            }

            if (e.getCode() == KeyCode.I) {
                partie.prochainNiveauDebug();
            }

            if (e.getCode() == KeyCode.D) {
                partie.ActiverDebug();
            }
        });

        scene.setOnKeyReleased(e -> Input.setKeyPressed(e.getCode(), false));

        var canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        var context = canvas.getGraphicsContext2D();

        stage.setTitle("Camelot à vélo");
        stage.setScene(scene);

        partie.chargerNiveau(1);
        partie.demarrerTransition();
        var timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;
                partie.update(deltaTemps, context);
                //Ne pas dessiner si on est en changement de niveau
                if (!partie.isEnTransitionNiveau() && !partie.isPartieFinie()) {
                    partie.draw(context);
                }
                dernierTemps = temps;
            }
        };
        timer.start();

        stage.setResizable(false);

        //Logo du jeu
        Image icon = new Image("journal.png");
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}