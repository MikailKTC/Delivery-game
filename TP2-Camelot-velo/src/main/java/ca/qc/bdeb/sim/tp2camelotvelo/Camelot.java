package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

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

        i = (int)Math.floor(tempsEcoule * 4) % images.length;

        double vx = accelerer(deltaTemps);
        velocite = new Point2D(vx, velocite.getY());

        super.updatePhysique(deltaTemps);
    }

    //Accélerer vers la droite/gauche/ralentir
    public double accelerer(double deltaTemps){

        double vx = velocite.getX();
        double accel = 300;

        if (Input.isKeyPressed(KeyCode.LEFT)) {
            vx -= accel * deltaTemps;
            if (vx < 200) {
                vx = 200;
            }
        }

        else if (Input.isKeyPressed(KeyCode.RIGHT)) {
            vx += accel * deltaTemps;
            if (vx > 600) vx = 600;
        }

        else {
            if (vx < 400) {
                vx += accel * deltaTemps;
                if (vx > 400) vx = 400;
            } else if (vx > 400) {
                vx -= accel * deltaTemps;
                if (vx < 400) vx = 400;
            }
        }

        return vx;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        Point2D posEcran = camera.coordoEcran(position);

        context.drawImage(images[i], posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }
}

//boolean jump = Input.isKeyPressed(KeyCode.SPACE)
//        || Input.isKeyPressed(KeyCode.UP);
//
//// Sauter = donner une vitesse vers le haut
//        if (toucheLeSol && jump) {
//velocite = new Point2D(velocite.getX(), -300);
//toucheLeSol = false;
//        }
