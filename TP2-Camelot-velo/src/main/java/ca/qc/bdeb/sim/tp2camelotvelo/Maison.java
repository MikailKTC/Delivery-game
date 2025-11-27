package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Maison extends Objet {


    protected int adresse;
    protected boolean abonnee;
    protected BoiteAuxLettres boite;
    protected ArrayList<Fenetre> fenetres = new ArrayList<>();

    public Maison(int positionX, int adresse) {
        super(new Point2D(positionX, MainJavaFX.HEIGHT - 200), 150, 200); //Taille arbitraire

        images = new Image[]{
                new Image("porte.png")
        };


        this.adresse = adresse;
        abonnee = Math.random() < 0.5;

        double hauteurMin = 0.2 * MainJavaFX.HEIGHT;
        double hauteurMax = 0.7 * MainJavaFX.HEIGHT;
        double yAleatoire = hauteurMin + Math.random() * (hauteurMax - hauteurMin);
        boite = new BoiteAuxLettres(new Point2D(positionX + 200, yAleatoire), abonnee);

        int nbFenetres = (int) (Math.random() * 3); // 0, 1 ou 2
        for (int i = 0; i < nbFenetres; i++) {
            Fenetre fenetre = new Fenetre(new Point2D(positionX + 300 * (i + 1), 50),abonnee);
            fenetres.add(fenetre);
        }

    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {
        boite.draw(context, camera);
        for(Fenetre fenetre: fenetres){
            fenetre.draw(context,camera);
        }

        Point2D posEcran = camera.coordoEcran(position);

        context.setFill(Color.WHITE);
        context.setFont(new javafx.scene.text.Font(22));

        context.setFont(new Font(24));

        double x = posEcran.getX() + 60;
        double y = posEcran.getY() + 70;

        context.fillText(String.valueOf(adresse), x, y);
    }
}
