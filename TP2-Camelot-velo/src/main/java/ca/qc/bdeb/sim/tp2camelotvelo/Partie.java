package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Partie {
    private Camelot camelot = new Camelot();
    private ArrayList<Maison> maisons = new ArrayList<>();
    private Camera camera = new Camera();
    private Image background = new Image("brique.png");

    public Partie() {
        ajouterMaisons();
    }

    public void update(double deltaTemps) {

        camelot.supprimerJournaux(camera);
        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);

//        if(camelot.position.getX()<=16900){
//            Platform.exit();
//        }
//
        ArrayList<Journal> journaux = camelot.getJournauxLances();
        ArrayList<Journal> journauxASupprimer = new ArrayList<>();

        for (Journal j : journaux) {

            boolean touche = false;

            for (Maison m : maisons) {
                if (m.boite.collisionAvecJournal(j)) {
                    touche = true;
                }

                for (Fenetre f : m.fenetres) {
                    if (f.collisionAvecJournal(j)) {
                        touche = true;

                    }
                }
            }

            if (touche) {
                journauxASupprimer.add(j);
            }

        }
        journaux.removeAll(journauxASupprimer);

    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        drawBrique(context);

        drawMaisons(context, camera);

        camelot.draw(context, camera);
    }

    public void drawBrique(GraphicsContext context) {

        double largeurBrique = 192;
        double hauteurBrique = 96;

        double debut = camera.getPositionCamera().getX();
        double fin = debut + MainJavaFX.WIDTH;

        int indexDebut = (int) Math.floor(debut / largeurBrique) - 1;
        int indexFin = (int) Math.ceil(fin / largeurBrique) + 1;


        for (int j = 0; j < MainJavaFX.HEIGHT; j += (int) hauteurBrique) {
            for (int i = indexDebut; i < indexFin; i++) {

                double x = i * largeurBrique;
                double xEcran = x - debut;

                context.drawImage(background, xEcran, j, largeurBrique, hauteurBrique);
            }

        }
    }

    public void drawMaisons(GraphicsContext context, Camera camera) {
        for (Maison maison : maisons) {
            maison.draw(context, camera);
        }
    }

    public void ajouterMaisons() {
        int posX = 1300;
        for (int i = 0; i < 12; i++) {
            maisons.add(new Maison(posX));
            posX += 1300;

        }


    }

}
