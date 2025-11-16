package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Partie {
    private Camelot camelot = new Camelot();
    private Camera camera = new Camera();
    private Image background = new Image("brique.png");


    public Partie() {
    }

    public void update(double deltaTemps) {

        camelot.supprimerJournaux(camera);
        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);

    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        drawBrique(context);

        camelot.draw(context, camera);
    }

   public void drawBrique(GraphicsContext context) {

       double largeurBrique = 192;
       double hauteurBrique = 96;

       double debut = camera.getPositionCamera().getX();
       double fin   = debut + MainJavaFX.WIDTH;

       int indexDebut = (int)Math.floor(debut / largeurBrique) - 1;
       int indexFin   = (int)Math.ceil(fin / largeurBrique) + 1;


       for (int j = 0; j < MainJavaFX.HEIGHT; j += (int) hauteurBrique) {
            for (int i = indexDebut; i < indexFin; i ++) {

                double x = i * largeurBrique;
                double xEcran = x - debut;

                context.drawImage(background, xEcran, j, largeurBrique, hauteurBrique);
            }

        }
    }


}
