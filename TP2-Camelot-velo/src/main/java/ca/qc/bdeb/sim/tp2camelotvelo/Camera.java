package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public class Camera {

    private Point2D positionCamera;

    public Camera() {
        positionCamera = new Point2D(0, 0);
    }

    public Point2D coordoEcran(Point2D positionMonde) {
        return positionMonde.subtract(positionCamera);
    }

    // Fait suivre la caméra en gardant le Camelot à 20% de l'écran
    public void suivreCamelot(Camelot camelot) {
        double nouveauX = camelot.getPosition().getX() -  MainJavaFX.WIDTH * 0.2;
        positionCamera = new Point2D(nouveauX, 0);
    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }
}
