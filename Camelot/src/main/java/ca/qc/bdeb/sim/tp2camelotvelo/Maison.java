package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Maison extends ObjetDuJeu {


    private static final int MAISON_LARGEUR = 150;
    private static final int MAISON_HAUTEUR = 200;

    private int adresse;
    private boolean abonnee;
    private BoiteAuxLettres boite;
    private ArrayList<Fenetre> fenetres = new ArrayList<>();

    public Maison(int positionX, int adresse) {

        super(new Point2D(positionX, MainJavaFX.HEIGHT - MAISON_HAUTEUR), MAISON_LARGEUR, MAISON_HAUTEUR);
        this.adresse = adresse;

        //50% de chance que la maison soit abonnée ou non
        this.abonnee = Math.random() < 0.5;

        images = new Image[]{
                new Image("porte.png")
        };

        this.boite = creerBoiteAuxLettres();
        creerFenetres();
    }

    @Override
    protected void drawExtras(GraphicsContext context, Camera camera) {

        final int decalageLargeur = 50;
        final int decalageHauteur = 40;

        //Dessiner la boîte aux lettres
        boite.draw(context, camera);

        //Dessiner toutes les fenêtres
        for (Fenetre fenetre : fenetres) {
            fenetre.draw(context, camera);
        }

        Point2D posEcran = camera.coordoEcran(position);

        //Dessine le numéro d'adresse en jaune par-dessus la porte
        context.setFill(Color.YELLOW);
        context.setFont(new Font(32));
        context.fillText(String.valueOf(adresse), posEcran.getX() + decalageLargeur
                , posEcran.getY() + decalageHauteur);

    }

    private BoiteAuxLettres creerBoiteAuxLettres() {

        final int DECALAGE_BOITE = 200;

        //Hauteur entre 20% et 70% de l'écran
        double hauteurMin = 0.2 * MainJavaFX.HEIGHT;
        double hauteurMax = 0.7 * MainJavaFX.HEIGHT;
        double yAleatoire = hauteurMin + Math.random() * (hauteurMax - hauteurMin);
        return new BoiteAuxLettres(new Point2D(position.getX() + DECALAGE_BOITE, yAleatoire), abonnee);
    }

    private void creerFenetres() {

        final int DECALAGE_FENETRE = 300;
        final int FENETRE_POSITION_Y = 50;

        int nbFenetres = (int) (Math.random() * 3); // 0, 1 ou 2

        for (int i = 0; i < nbFenetres; i++) {
            fenetres.add(
                    new Fenetre(new Point2D(position.getX() + DECALAGE_FENETRE * (i + 1),
                            FENETRE_POSITION_Y), abonnee)
            );

        }
    }

    public int getAdresse() {
        return adresse;
    }

    public boolean isAbonnee() {
        return abonnee;
    }

    public BoiteAuxLettres getBoite() {
        return boite;
    }

    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }
}
