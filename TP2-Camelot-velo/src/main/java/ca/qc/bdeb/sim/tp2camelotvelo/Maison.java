package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Maison extends Objet {


    private int adresse;
    private boolean abonnee;
    protected BoiteAuxLettres boite;
    protected ArrayList<Fenetre> fenetres = new ArrayList<>();

    public Maison(int positionX) {
        super(new Point2D(positionX, MainJavaFX.HEIGHT - 200), 150, 200); //Taille arbitraire

        images = new Image[]{
                new Image("porte.png")
        };

        double hauteurMin = 0.2 * MainJavaFX.HEIGHT;
        double hauteurMax = 0.7 * MainJavaFX.HEIGHT;
        double yAleatoire = hauteurMin + Math.random() * (hauteurMax - hauteurMin);
        boite = new BoiteAuxLettres(new Point2D(positionX + 200, yAleatoire), abonnee);

        int nbFenetres = (int) (Math.random() * 3); // 0, 1 ou 2
        for (int i = 0; i < nbFenetres; i++) {
            fenetres.add(new Fenetre(new Point2D(positionX + 300 * (i + 1), 50)));
        }

    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {
        boite.draw(context, camera);
        for(Fenetre fenetre: fenetres){
            fenetre.draw(context,camera);
        }
    }
}
