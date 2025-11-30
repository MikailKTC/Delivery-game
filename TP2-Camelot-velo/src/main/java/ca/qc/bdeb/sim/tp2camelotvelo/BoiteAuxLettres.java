package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class BoiteAuxLettres extends ObjetInteractif {

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

    public void changerCouleurBoite(){

        if (!dejaTouchee) {

            if (abonne) {
                images[0] = imgBoiteVerte;
            } else {
                images[0] = imgBoiteRouge;

            }
        }
    }

}

