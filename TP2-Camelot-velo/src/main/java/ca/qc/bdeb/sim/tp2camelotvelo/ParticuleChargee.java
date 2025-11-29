package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ParticuleChargee extends ObjetInteractif {


    private static final double DIAMETRE = 20;
    private static final double CHARGE = 900;
    private Color couleur;

    public ParticuleChargee(Point2D position) {
        super(position, DIAMETRE, DIAMETRE);

        this.velocite = new Point2D(0,0);
        this.acceleration = new Point2D(0,0);

        double teinte = Math.random() * 360;
        this.couleur = Color.hsb(teinte, 1, 1);
    }

    @Override
    public void update(double deltaTemps) {

    }

    public Point2D champElectriqueAuPoint(Point2D point) {

        double K = 90;

        Point2D r = point.subtract(this.getCentre());
        double distance = r.magnitude();
        if(distance<10){
            distance =10;
        }
        Point2D direction = r.normalize();
        double magnitude = K * CHARGE / (distance * distance);
        return direction.multiply(magnitude);
    }

    @Override
    public void drawExtras(GraphicsContext context, Camera camera) {

        Point2D posEcran = camera.coordoEcran(position);

        context.setFill(couleur);
        context.fillOval(posEcran.getX(), posEcran.getY(), DIAMETRE, DIAMETRE);
    }

    public double getCharge() {
        return CHARGE;
    }


}
