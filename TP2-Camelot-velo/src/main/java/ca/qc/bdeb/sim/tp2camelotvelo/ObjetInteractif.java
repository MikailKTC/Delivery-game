package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public abstract class ObjetInteractif extends ObjetDuJeu {

    protected Point2D velocite;
    protected Point2D acceleration;

    public ObjetInteractif(Point2D position,double largeur, double hauteur) {
        super(position,largeur,hauteur);
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
    }

    protected void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    protected abstract void update(double deltaTemps);

    protected double getHaut() {
        return position.getY();
    }

    protected double getBas() {
        return position.getY() + taille.getY();
    }

    protected double getGauche() {
        return position.getX();
    }

    protected double getDroite() {
        return position.getX() + taille.getX();
    }

    protected Point2D getCentre() {
        return position.add(taille.multiply(1 / 2.0));
    }

    protected Point2D getPosition() {
        return position;
    }



    protected Point2D getTaille() {
        return taille;
    }

    protected double getHauteur() {
        return taille.getY();
    }

    protected double getLargeur() {
        return taille.getX();
    }

    protected double getBasScene(){
        return MainJavaFX.HEIGHT;
    }

    protected double getDroitScene(){
        return MainJavaFX.WIDTH;
    }

    protected double getGaucheScene(){
        return 0;
    }

    protected Point2D getAcceleration() {
        return acceleration;
    }

    protected void setAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

}
