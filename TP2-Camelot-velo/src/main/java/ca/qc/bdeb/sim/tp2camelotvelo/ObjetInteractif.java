package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class ObjetInteractif extends Objet{

    protected Point2D velocite;
    protected Point2D acceleration;

    public ObjetInteractif(Point2D position,double largeur, double hauteur) {
        super(position,largeur,hauteur);
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
    }

    public void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    public boolean collisionAvecJournal(Journal j) {

        boolean overlapX =
                (j.getDroite() > this.getGauche()) &&
                        (j.getGauche() < this.getDroite());

        boolean overlapY =
                (j.getBas() > this.getHaut()) &&
                        (j.getHaut() < this.getBas());

        return overlapX && overlapY;

    }

    public abstract void update(double deltaTemps);

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

    public double getBasScene(){
        return MainJavaFX.HEIGHT;
    }

    public double getDroitScene(){
        return MainJavaFX.WIDTH;
    }

    public double getGaucheScene(){
        return 0;
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

}
