package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Fenetre extends ObjetCollisionnable {

    private boolean abonne;
    private Image imageNormale;
    private Image imageBriseeRouge;
    private Image imageBriseeVerte;
    private boolean estBrisee;

    public Fenetre(Point2D position, boolean abonne) {
        super(position, 150, 120);

        imageNormale = new Image("fenetre.png");
        imageBriseeRouge = new Image("fenetre-brisee-rouge.png");
        imageBriseeVerte = new Image("fenetre-brisee-vert.png");

        this.abonne = abonne;

        //Au début la fenêtre est intacte
        images = new Image[]{ imageNormale };

        estBrisee = false;
    }


    @Override
    protected void update(double deltaTemps) {
        //les fenêtres ne bougent pas donc méthode vide
    }

    public void changerCouleurFenetre(){
        //Changer couleur des fenetres seulement si elles n'ont pas encore été brisées
        if (!estBrisee) {
            if (!abonne) {
                images[0] = imageBriseeVerte;
            } else {
                images[0] = imageBriseeRouge;
            }
        }
    }

    // Indique si la fenêtre est brisée
    public boolean isBrisee() {
        return estBrisee;
    }

    //Permet de marquer la fenêtre comme brisée
    public void setIsBrisee(boolean estBrisee) {
        this.estBrisee = estBrisee;
    }
}
