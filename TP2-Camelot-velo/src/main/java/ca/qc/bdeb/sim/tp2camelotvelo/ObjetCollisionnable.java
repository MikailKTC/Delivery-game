package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

// Classe pour Fenetre et BoiteAuxLettres qui peuvent entrer en collision avec des journaux
public class ObjetCollisionnable extends ObjetInteractif {

    public ObjetCollisionnable(Point2D position, double largeur, double hauteur) {
        super(position, largeur, hauteur);
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
    }

    protected void update(double deltaTemps) {
    }

    protected boolean collisionAvecJournal(Journal j) {

        boolean overlapX =
                (j.getDroite() > this.getGauche()) &&
                        (j.getGauche() < this.getDroite());

        boolean overlapY =
                (j.getBas() > this.getHaut()) &&
                        (j.getHaut() < this.getBas());

        return overlapX && overlapY;

    }
}
