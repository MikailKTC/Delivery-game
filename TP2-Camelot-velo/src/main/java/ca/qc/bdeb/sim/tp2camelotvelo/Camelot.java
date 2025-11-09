package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Camelot extends ObjetDuJeu {

    private final Image[] images;
    private int i = 0;
    private double tempsEcoule = 0;

    public Camelot() {

        // Position horizontale = 20% de la largeur de l’écran, Position verticale = sur le sol

        super(new Point2D(MainJavaFX.WIDTH * 0.2, MainJavaFX.HEIGHT - 144), 172, 144);

        images = new Image[]{
                new Image("camelot1.png"),
                new Image("camelot2.png")
        };

        velocite = new Point2D(400, 0);
    }

    @Override
    public void update(double deltaTemps) {
        tempsEcoule += deltaTemps;

        if (tempsEcoule >= 0.25) {
            i = (i + 1) % images.length;
            tempsEcoule = 0;
        }
        super.updatePhysique(deltaTemps);
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        Point2D posEcran = camera.coordoEcran(position);

        context.drawImage(images[i], posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }
}
