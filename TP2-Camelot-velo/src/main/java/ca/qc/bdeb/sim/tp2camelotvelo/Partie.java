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

        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);

    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        context.drawImage(background, 0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        camelot.draw(context, camera);
    }
}
