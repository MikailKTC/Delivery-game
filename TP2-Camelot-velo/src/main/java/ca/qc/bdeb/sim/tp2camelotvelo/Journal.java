package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Journal extends ObjetDuJeu {

    //Variable globale, vérifier si on a le droit
    public static double masse = 1 + Math.random();

    public Journal(Point2D velocite, Point2D position) {

        super(new Point2D(0, 0), 52, 31);

        this.velocite = velocite;
        this.position = position;

        images = new Image[]{new Image("journal.png")};

        acceleration = new Point2D(0, 1500);

    }

    @Override
    public void update(double deltaTemps) {

        super.updatePhysique(deltaTemps);

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        Point2D posEcran = camera.coordoEcran(position);

        context.drawImage(images[0], posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }

}
