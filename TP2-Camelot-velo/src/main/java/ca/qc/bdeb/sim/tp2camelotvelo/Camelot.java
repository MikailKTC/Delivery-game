package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Camelot extends ObjetDuJeu {

    private int i = 0;
    private double tempsEcoule = 0;
    protected boolean toucheLeSol;
    private final ArrayList<Journal> journauxLances = new ArrayList<>();
    private double tempsEcouleLance = 0;


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
    public void update(double deltaTemps) {

        tempsEcoule += deltaTemps;

        i = (int) Math.floor(tempsEcoule * 4) % images.length;

        double vx = accelerer(deltaTemps);

        velocite = new Point2D(vx, velocite.getY());

        super.updatePhysique(deltaTemps);
        sauter();
        updateJournaux(deltaTemps);

    }

    //Accélerer vers la droite/gauche/ralentir
    public double accelerer(double deltaTemps) {

        double vx = velocite.getX();
        double accel = 300;

        if (Input.isKeyPressed(KeyCode.LEFT)) {
            vx -= accel * deltaTemps;
            if (vx < 200) {
                vx = 200;
            }
        } else if (Input.isKeyPressed(KeyCode.RIGHT)) {
            vx += accel * deltaTemps;
            if (vx > 600) vx = 600;
        } else {
            if (vx < 400) {
                vx += accel * deltaTemps;
                if (vx > 400) vx = 400;
            } else if (vx > 400) {
                vx -= accel * deltaTemps;
                if (vx < 400) vx = 400;
            }
        }

        return vx;
    }

    public void sauter() {

        if (position.getY() + taille.getY() >= MainJavaFX.HEIGHT) {
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - taille.getY());
            velocite = new Point2D(velocite.getX(), 0);
            toucheLeSol = true;
        }

        boolean jump = Input.isKeyPressed(KeyCode.SPACE)
                || Input.isKeyPressed(KeyCode.UP);

        if (toucheLeSol && jump) {
            velocite = new Point2D(velocite.getX(), -500);
            toucheLeSol = false;
        }
    }

    public void lancerJournal() {

        boolean zPressed = Input.isKeyPressed(KeyCode.Z);
        boolean xPressed = Input.isKeyPressed(KeyCode.X);

        if (zPressed || xPressed) {

            Point2D impulsion = new Point2D(0, 0);

            if (zPressed) {
                impulsion = new Point2D(900, -900);
            }
            if (xPressed) {
                impulsion = new Point2D(150, -1100);
            }

            if (Input.isKeyPressed(KeyCode.SHIFT)) {
                impulsion = impulsion.multiply(1.5);
            }

            Point2D nouvVelocite = velocite.add(impulsion.multiply(1 / Journal.masse));
            double max = 1500;

            //Maximum vélocité = 1500px/sec
            if (nouvVelocite.magnitude() >= max) {
                nouvVelocite = nouvVelocite.multiply(max / nouvVelocite.magnitude());
            }

            Journal journal = new Journal(nouvVelocite, this.getCentre());
            journauxLances.add(journal);
        }

    }

    public void updateJournaux(double deltaTemps) {

        boolean zPressed = Input.isKeyPressed(KeyCode.Z);
        boolean xPressed = Input.isKeyPressed(KeyCode.X);

        //Lancer un journal toutes les 0,5sec
        tempsEcouleLance += deltaTemps;
        if (tempsEcouleLance >= 0.5 && (zPressed || xPressed)) {
            lancerJournal();
            tempsEcouleLance = 0;
        }

        for (Journal j : journauxLances) {
            j.update(deltaTemps);
        }

    }

    //Supprimer journaux en dehors de la scène
    public void supprimerJournaux(Camera camera){

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

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        Point2D posEcran = camera.coordoEcran(position);

        context.drawImage(images[i], posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());

        for (Journal j : journauxLances) {
            j.draw(context, camera);
        }
    }

}


