package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Camelot extends ObjetInteractif {

    private int indexImage = 0;
    private double tempsEcoule = 0;
    protected boolean toucheLeSol;
    private ArrayList<Journal> journauxLances = new ArrayList<>();

    public Camelot() {

        // Position horizontale = 20% de la largeur de l’écran, Position verticale = sur le sol

        super(new Point2D(MainJavaFX.WIDTH * 0.2, MainJavaFX.HEIGHT - 144), 172, 144);

        images = new Image[]{
                new Image("camelot1.png"),
                new Image("camelot2.png")
        };

        velocite = new Point2D(400, 0);
        toucheLeSol = true;
        acceleration = new Point2D(0, 1500);
    }

    @Override
    protected void update(double deltaTemps) {

        tempsEcoule += deltaTemps;

        indexImage = (int) Math.floor(tempsEcoule * 4) % images.length;

        double vx = mettreAJourVitesseX(deltaTemps);

        velocite = new Point2D(vx, velocite.getY());

        super.updatePhysique(deltaTemps);
        sauter();
        updateJournaux(deltaTemps);

    }


    // ---- Accélerer vers la droite/gauche/ralentir ----
    private double mettreAJourVitesseX(double deltaTemps) {

        double vx = velocite.getX();
        double accel = 300;
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);

        if (gauche)
            vx = ralentir(deltaTemps, vx, accel);

        else if (droite)
            vx = accelerer(deltaTemps, vx, accel);

        else
            vx = recupererVitesseParDefaut(deltaTemps, vx, accel);

        return vx;
    }

    private double ralentir(double deltaTemps, double vx, double accel) {

        int vitesseMinimale = 200;

        vx -= accel * deltaTemps;
        if (vx < vitesseMinimale) {
            vx = vitesseMinimale;
        }

        return vx;
    }

    private double accelerer(double deltaTemps, double vx, double accel) {

        int vitesseMaximale = 600;

        vx += accel * deltaTemps;
        if (vx > vitesseMaximale) {
            vx = vitesseMaximale;
        }

        return vx;
    }

    private double recupererVitesseParDefaut(double deltaTemps, double vx, double accel) {

        int vitesseParDefaut = 400;

        if (vx < vitesseParDefaut) {
            vx += accel * deltaTemps;
            if (vx > vitesseParDefaut) {
                vx = vitesseParDefaut;
            }
        } else if (vx > vitesseParDefaut) {
            vx -= accel * deltaTemps;
            if (vx < vitesseParDefaut) {
                vx = vitesseParDefaut;
            }
        }
        return vx;

    }

    //-----LOGIQUE DU SAUT-----

    private void sauter() {

        boolean aAtteri = getBas() >= MainJavaFX.HEIGHT;
        boolean jump = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);

        if (aAtteri) {
            mettreAJourContactSol();
        }

        if (toucheLeSol && jump) {
            declencherSaut();
        }
    }

    private void mettreAJourContactSol() {
        // Logique qui met fin à la chute
        position = new Point2D(position.getX(), MainJavaFX.HEIGHT - taille.getY());
        velocite = new Point2D(velocite.getX(), 0);
        toucheLeSol = true;
    }

    private void declencherSaut() {
        // Logique pour le saut
        velocite = new Point2D(velocite.getX(), -500);
        toucheLeSol = false;
    }

    public void lancerJournal(double masse) {

        boolean zPressed = Input.isKeyPressed(KeyCode.Z);
        boolean xPressed = Input.isKeyPressed(KeyCode.X);

        if (zPressed || xPressed) {

            Point2D impulsion = new Point2D(0, 0);
            impulsion = calculImpulsion(impulsion, zPressed, xPressed);

            Journal journal = new Journal(new Point2D(0, 0), new Point2D(0, 0), masse);
            Point2D nouvVelocite = velocite.add(impulsion.multiply(1 / journal.getMasse()));

            double max = 1500;

            //Maximum vélocité = 1500px/sec
            if (nouvVelocite.magnitude() >= max) {
                nouvVelocite = nouvVelocite.multiply(max / nouvVelocite.magnitude());
            }

            journal = new Journal(nouvVelocite, this.getCentre(), masse);
            journauxLances.add(journal);
        }

    }

    private Point2D calculImpulsion(Point2D impulsion, boolean zPressed, boolean xPressed) {

        boolean shiftPressed = Input.isKeyPressed(KeyCode.SHIFT);

        if (zPressed) {
            impulsion = new Point2D(900, -900);
        }
        if (xPressed) {
            impulsion = new Point2D(150, -1100);
        }
        if (shiftPressed) {
            impulsion = impulsion.multiply(1.5);
        }

        return impulsion;
    }

    private void updateJournaux(double deltaTemps) {

        for (Journal j : journauxLances) {
            j.update(deltaTemps);
        }

    }

    //Supprimer journaux en dehors de la scène
    public void supprimerJournaux(Camera camera) {

        Point2D posCam = camera.getPositionCamera();
        double limiteGauche = posCam.getX();
        double limiteDroite = posCam.getX() + MainJavaFX.WIDTH;
        double limiteBas = MainJavaFX.HEIGHT;

        journauxLances.removeIf(j ->
                j.position.getX() > limiteDroite ||
                        j.position.getX() < limiteGauche ||
                        j.position.getY() > limiteBas
        );
    }

    public ArrayList<Journal> getJournauxLances() {
        return journauxLances;
    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {
        for (Journal j : journauxLances) {
            j.draw(context, camera);
        }
    }

    @Override
    protected Image getImageToDraw() {
        return images[indexImage];
    }


}


