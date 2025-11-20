package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BoiteAuxLettres extends ObjetDuJeu {

    boolean abonne;
    Image imgBoiteNormal;
    Image imgBoiteVerte;
    Image imgBoiteRouge;

    Boolean dejaTouchee = false;

    public BoiteAuxLettres(Point2D position, Boolean abonne) {
        super(position, 81, 76);
        imgBoiteNormal = new Image("boite-aux-lettres.png");
        imgBoiteVerte = new Image("boite-aux-lettres-vert.png");
        imgBoiteRouge = new Image("boite-aux-lettres-rouge.png");

        images = new Image[]{imgBoiteNormal};
        this.abonne = abonne;
    }

    @Override
    public void update(double deltaTemps) {

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {

        Point2D posEcran = camera.coordoEcran(position);

        context.drawImage(images[0], posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
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

        if (!dejaTouchee) {
            dejaTouchee = true;

            if (abonne) {
                images[0] = imgBoiteVerte;
            } else {
                images[0] = imgBoiteRouge;

            }
        }
        return true; //tjrs supprimer journal
    }
}

