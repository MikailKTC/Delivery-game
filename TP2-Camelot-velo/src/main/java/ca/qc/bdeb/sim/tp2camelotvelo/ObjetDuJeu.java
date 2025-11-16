package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class ObjetDuJeu {

    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected Point2D taille;
    protected Image[] images;


    public ObjetDuJeu(Point2D position, double largeur, double hauteur) {
        this.position = position;
        this.taille = new Point2D(largeur, hauteur);
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
    }

    public void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    public abstract void update(double deltaTemps);

    public abstract void draw(GraphicsContext context, Camera camera);

    public double getHaut() {
        return position.getY();
    }

    public double getBas() {
        return position.getY() + taille.getY();
    }

    public double getGauche() {
        return position.getX();
    }

    public double getDroite() {
        return position.getX() + taille.getX();
    }

    public Point2D getCentre() {
        return position.add(taille.multiply(1 / 2.0));
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getTaille() {
        return taille;
    }

    public double getHauteur() {
        return taille.getY();
    }

    public double getLargeur() {
        return taille.getX();
    }

}
