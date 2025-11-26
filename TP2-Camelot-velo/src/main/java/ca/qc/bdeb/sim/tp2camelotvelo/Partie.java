package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Partie {
    private Camelot camelot = new Camelot();
    private ArrayList<Maison> maisons = new ArrayList<>();
    private Camera camera = new Camera();
    private Image background = new Image("brique.png");
    private int niveauActuel = 1;
    private int argent = 0;
    private int journauxRestants = 0;

    public Partie() {
        chargerNiveau(1);
    }

    public void update(double deltaTemps) {

        camelot.supprimerJournaux(camera);
        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);

        if (journauxRestants <= 0 && camelot.getJournauxLances().isEmpty()) {
            chargerNiveau(niveauActuel + 1);
        }

        ArrayList<Journal> journaux = camelot.getJournauxLances();
        ArrayList<Journal> journauxASupprimer = new ArrayList<>();

        for (Journal j : journaux) {

            boolean touche = false;

            for (Maison m : maisons) {
                if (m.boite.collisionAvecJournal(j)) {
                    if (m.boite.abonne && !m.boite.dejaTouchee) {
                        argent += 1;
                    }
                    touche = true;
                }

                for (Fenetre f : m.fenetres) {
                    if (f.collisionAvecJournal(j)) {
                        if (!f.estBrisee) {
                            if (m.abonnee) {
                                argent -= 2;
                            } else {
                                argent += 2;
                            }
                        }
                        touche = true;

                    }
                }
            }

            if (touche) {
                journauxASupprimer.add(j);
                journauxRestants--;
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

    /*public void ajouterMaisons() {
        int posX = 1300;
        for (int i = 0; i < 12; i++) {
            maisons.add(new Maison(posX));
            posX += 1300;

        }


    }*/

    public void chargerNiveau(int numero) {
        niveauActuel = numero;
        maisons.clear();

        journauxRestants += 12;
        int adresse = 100 + (int) (Math.random() * 850);

        int posX = 1300;

        for (int i = 0; i < 12; i++) {
            Maison m = new Maison(posX);
            boolean estAbonnee = m.abonnee;
            double hauteurMin = 0.2 * MainJavaFX.HEIGHT;
            double hauteurMax = 0.7 * MainJavaFX.HEIGHT;
            double yAleatoire = hauteurMin + Math.random() * (hauteurMax - hauteurMin);

            m.boite = new BoiteAuxLettres(new Point2D(posX + 200, yAleatoire), estAbonnee);

            int nbFenetres = (int) (Math.random() * 3);

            for (int f = 0; f < nbFenetres; f++) {
                Fenetre fenetre = new Fenetre(
                        new Point2D(posX + 300 + 300 * f, 50), estAbonnee);

                m.fenetres.add(fenetre);
            }

            maisons.add(m);
            adresse += 2;
            posX += 1300;

        }
    }

}
