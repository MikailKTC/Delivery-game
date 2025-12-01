package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Journal extends ObjetInteractif {

    private static final double GRAVITE = 1500;
    private double masse;
    private boolean compteurDejaDecremente = false; //Compteur qui permet de supprimer un journal du joueur
    private final double charge = 900;

    public Journal(Point2D velocite, Point2D position, double masse) {

        super(new Point2D(0, 0), 52, 31);

        this.velocite = velocite;
        this.position = position;
        this.masse = masse;

        images = new Image[]{new Image("journal.png")};

        acceleration = new Point2D(0, GRAVITE);

    }

    @Override
    public void update(double deltaTemps) {

        double max = 1500;
        if (velocite.magnitude() > max) {
            velocite = velocite.multiply(max / velocite.magnitude());
        }

        super.updatePhysique(deltaTemps);

    }

    public double getMasse() {
        return masse;
    }

    public double getCharge() {
        return charge;
    }
}
