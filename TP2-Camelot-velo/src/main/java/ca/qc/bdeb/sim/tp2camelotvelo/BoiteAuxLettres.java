package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class BoiteAuxLettres extends ObjetCollisionnable {

    private boolean abonne;
    private Image imgBoiteNormal;
    private Image imgBoiteVerte;
    private Image imgBoiteRouge;

    private boolean dejaTouchee = false;

    public BoiteAuxLettres(Point2D position, Boolean abonne) {
        super(position, 81, 76);
        imgBoiteNormal = new Image("boite-aux-lettres.png");
        imgBoiteVerte = new Image("boite-aux-lettres-vert.png");
        imgBoiteRouge = new Image("boite-aux-lettres-rouge.png");

        images = new Image[]{imgBoiteNormal};
        this.abonne = abonne;
    }

    @Override
    protected void update(double deltaTemps) {

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

    public boolean isAbonne() {
        return abonne;
    }

    public boolean isDejaTouchee() {
        return dejaTouchee;
    }

    public void setDejaTouchee(boolean dejaTouchee) {
        this.dejaTouchee = dejaTouchee;
    }
}

