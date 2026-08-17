package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ParticuleChargee extends ObjetInteractif {

    private static final double K = 90;
    private static final double DIAMETRE = 20;
    private static final double CHARGE = 900;

    private Color couleur;
    private double teinteActuelle; //
    private static final double VITESSE_TEINTE = 60;

    public ParticuleChargee(Point2D position) {
        super(position, DIAMETRE, DIAMETRE);

        this.velocite = new Point2D(0,0);
        this.acceleration = new Point2D(0,0);

        this.teinteActuelle = Math.random() * 360;
        this.couleur = Color.hsb(teinteActuelle, 1, 1);
    }

    @Override
    protected void update(double deltaTemps) {

        //La logique qui permet d'alterner la couleur des particules a été fourni par ChatGPT
        teinteActuelle += VITESSE_TEINTE * deltaTemps;
        teinteActuelle %= 360;
        this.couleur = Color.hsb(teinteActuelle, 1, 1);

    }

    //Calcul du champ généré par une particule à un point donné
    public Point2D champElectriqueAuPoint(Point2D point) {

        Point2D r = point.subtract(this.getCentre()); //Distance entre journal et la particule chargée
        double distance = r.magnitude();
        if(distance<1){ //Si la distance est plus petite que 1, pour évtier les bug, on garde 1.
            distance =1;
        }
        Point2D direction = r.normalize(); // Vecteur unitaire
        double moduleChamp = K * CHARGE / (distance * distance);
        return direction.multiply(moduleChamp); //Vecteur champ électrique retourné
    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {

        Point2D posEcran = camera.coordoEcran(position);

        context.setFill(couleur);
        context.fillOval(posEcran.getX(), posEcran.getY(), DIAMETRE, DIAMETRE);
    }



}
