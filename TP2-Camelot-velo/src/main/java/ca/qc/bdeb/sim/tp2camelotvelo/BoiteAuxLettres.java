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

    public boolean collisionAvecJournal() {
        if (!dejaTouchee) {
            dejaTouchee = true;

            if (abonne) {
                images[0] = imgBoiteVerte;
                return true; //donc si la maison est abonne alors l utilisateur gagne 1$
            } else {
                images[0] = imgBoiteRouge;
                return false; //utilisateur gagne rien

            }
        }
        return false;// puisque boite est deja toucher utilisateur gagne rien
    }
}

