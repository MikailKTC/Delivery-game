package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

        //Gérer les touches enfoncées
        scene.setOnKeyPressed(e -> {
            Input.setKeyPressed(e.getCode(), true);

            playEvents(e, stage);

        });

        //Gérer les touches relâchées
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

                //Ne pas dessiner si on est en changement de niveau ou si la partie est fini
                if (!partie.isEnTransitionNiveau() && !partie.isPartieFinie()) {
                    partie.draw(context);
                }
                dernierTemps = temps;
            }
        };
        timer.start();

        //Taille de la fenêtre fixe
        stage.setResizable(false);

        //Logo du jeu
        Image icon = new Image("journal.png");
        stage.getIcons().add(icon);
        stage.show();
    }


    //Méthode qui gère les touches debugage ainsi que fermer le jeu si on appuie sur "esc"
    private void playEvents(KeyEvent e, Stage stage) {

        //Quitter le jeu
        if (e.getCode() == KeyCode.ESCAPE) {
            stage.close();
        }

        //Ajouter 10 journaux
        if (e.getCode() == KeyCode.Q) {
            partie.ajouterJournauxDebug();
        }

        //On set le nombre de journaux à 0
        if (e.getCode() == KeyCode.K) {
            partie.setJournauxZeroDebug();
        }

        //Passer au prochain niveau
        if (e.getCode() == KeyCode.L) {
            partie.prochainNiveauDebug();
        }

        //Mode debug pour le camelot et les hitboxes
        if (e.getCode() == KeyCode.D) {
            partie.activerDebugD();
        }

        //Mode debug pour les particules (les vecteurs)
        if (e.getCode() == KeyCode.F) {
            partie.activerDebugF();
        }

        //Mode particules en haut et en bas de l'écran
        if (e.getCode() == KeyCode.I) {
            partie.activerDebugI();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}