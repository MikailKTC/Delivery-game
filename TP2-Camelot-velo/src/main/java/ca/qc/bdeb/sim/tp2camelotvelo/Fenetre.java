package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Fenetre extends ObjetInteractif {

    private boolean abonne;
    private Image imageNormale;
    private Image imageBriseeRouge;
    private Image imageBriseeVerte;
    protected boolean estBrisee;

    public Fenetre(Point2D position, boolean abonne) {
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

    public void changerCouleurFenetre(){
        //Changer couleur des fenetres
        if (!estBrisee) {
            if (!abonne) {
                images[0] = imageBriseeVerte;
            } else {
                images[0] = imageBriseeRouge;
            }
        }
    }
}
