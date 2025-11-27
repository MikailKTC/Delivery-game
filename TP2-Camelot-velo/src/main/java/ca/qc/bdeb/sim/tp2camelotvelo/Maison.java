package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Maison extends Objet {

    //Ajout de constante pour la propreté et éviter les nombres incompréhensibles
    private static final int MAISON_LARGEUR = 150;
    private static final int MAISON_HAUTEUR = 200;
    private static final int DECALAGE_BOITE = 200;
    private static final int DECALAGE_FENETRE = 300;
    private static final int FENETRE_yPOSITION = 50;

    protected int adresse;
    protected boolean abonnee;
    protected BoiteAuxLettres boite;
    protected ArrayList<Fenetre> fenetres = new ArrayList<>();

    public Maison(int positionX, int adresse) {

        super(new Point2D(positionX, MainJavaFX.HEIGHT - MAISON_HAUTEUR), MAISON_LARGEUR, MAISON_HAUTEUR); //Taille arbitraire
        this.adresse = adresse;
        this.abonnee = Math.random() < 0.5;

        images = new Image[]{
                new Image("porte.png")
        };

        this.boite = creerBoiteAuxLettres();
        creerFenetres();
    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {

        boite.draw(context, camera);

        for (Fenetre fenetre : fenetres) {
            fenetre.draw(context, camera);
        }

        Point2D posEcran = camera.coordoEcran(position);

        context.setFill(Color.WHITE);
        context.setFont(new Font(24));
        context.fillText(String.valueOf(adresse), posEcran.getX() + 60, posEcran.getY() + 70);

    }

    private BoiteAuxLettres creerBoiteAuxLettres() {

        //Hauteur entre 20% et 70% de l'écran
        double hauteurMin = 0.2 * MainJavaFX.HEIGHT;
        double hauteurMax = 0.7 * MainJavaFX.HEIGHT;
        double yAleatoire = hauteurMin + Math.random() * (hauteurMax - hauteurMin);
        return new BoiteAuxLettres(new Point2D(position.getX() + DECALAGE_BOITE, yAleatoire), abonnee);
    }

    private void creerFenetres() {

        int nbFenetres = (int) (Math.random() * 3); // 0, 1 ou 2

        for (int i = 0; i < nbFenetres; i++) {
            fenetres.add(
                    new Fenetre(new Point2D(position.getX() + DECALAGE_FENETRE * (i + 1), FENETRE_yPOSITION), abonnee)
            );

        }
    }
}
