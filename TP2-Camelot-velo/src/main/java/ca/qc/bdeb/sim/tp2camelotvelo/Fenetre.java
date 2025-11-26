package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Fenetre extends ObjetInteractif {

    private boolean abonne;
    private Image imageNormale;
    private Image imageBriseeRouge;
    private Image imageBriseeVerte;
    private boolean estBrisee;

    public Fenetre(Point2D position, boolean abonnee) {
        super(position, 150, 120);

        imageNormale = new Image("fenetre.png");
        imageBriseeRouge = new Image("fenetre-brisee-rouge.png");
        imageBriseeVerte = new Image("fenetre-brisee-vert.png");

        this.abonne = abonne;
        images = new Image[]{ imageNormale };

        estBrisee = false;
    }


    @Override
    public void update(double deltaTemps) {

    }

    public boolean collisionAvecJournal(Journal j) {

        boolean overlapX =
                (j.getDroite() > this.getGauche()) &&
                        (j.getGauche() < this.getDroite());

        boolean overlapY =
                (j.getBas() > this.getHaut()) &&
                        (j.getHaut() < this.getBas());

        boolean collision = overlapX && overlapY;

        if (!collision) {
            return false;
        }

        if (!estBrisee) {
            estBrisee = true;
            if (abonne) {
                images[0] = imageBriseeVerte;
            } else {
                images[0] = imageBriseeRouge;
            }
        }
            return true; //tjrs supprimer journal


    }
}
