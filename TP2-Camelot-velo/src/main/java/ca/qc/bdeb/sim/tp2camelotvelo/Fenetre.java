package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fenetre extends ObjetDuJeu{

    private Image imageNormale;
    private Image imageBrisee;
    private boolean estBrisee;

    public Fenetre(Point2D position) {
        super(position, 41, 41);

        imageNormale = new Image("fenetre.png");
        imageBrisee = new Image("fenetre-brisee.png");

        images = new Image[]{ imageNormale };

        estBrisee = false;
    }

    @Override
    public void update(double deltaTemps) {

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {

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
            images[0] = imageBrisee;
        }
        return true; //tjrs supprimer journal
    }
}
