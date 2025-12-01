package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class ObjetDuJeu {
    protected Point2D taille;
    protected Image[] images;
    protected Point2D position;

    public ObjetDuJeu(Point2D position, double largeur, double hauteur) {
        this.position = position;
        this.taille = new Point2D(largeur, hauteur);
    }

    //Draw commun à tous les objets
    protected final void draw(GraphicsContext context, Camera camera) {

        Point2D posEcran = camera.coordoEcran(position);
        Image img = getImageToDraw();

        context.drawImage(img, posEcran.getX(), posEcran.getY(),
                taille.getX(), taille.getY());

        drawExtras(context, camera);
    }

    //Par défaut : première image (images[0])
    protected Image getImageToDraw() {
        return images[0];
    }

    //Draw spécifiques à certaines classes
    protected void drawExtras(GraphicsContext context, Camera camera) { }

}
