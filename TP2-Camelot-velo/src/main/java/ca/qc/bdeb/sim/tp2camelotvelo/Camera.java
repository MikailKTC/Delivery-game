package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public class Camera {

    private Point2D positionCamera;

    public Point2D coordoEcran(Point2D positionMonde) {
        return positionMonde.subtract(positionCamera);
    }
}
