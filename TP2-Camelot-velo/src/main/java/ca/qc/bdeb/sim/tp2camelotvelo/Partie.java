package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Partie {

    private static final int LIMITE_NIVEAU = 16900;

    private Camelot camelot = new Camelot();
    private ArrayList<Maison> maisons = new ArrayList<>();
    private Camera camera = new Camera();
    private Image background = new Image("brique.png");
    private int niveauActuel = 1;
    private int argent = 0;
    private int journauxRestants = 0;
    private boolean chargerProchainNiveau = false;

    private double tempsEcouleLance = 0;
    private boolean enTransitionNiveau = false;
    private double compteurTransition = 0;

    private boolean partieFinie = false;
    private double timerFinPartie = 0;

    private Image imgJournal = new Image("icone-journal.png");
    private Image imgDollar = new Image("icone-dollar.png");
    private Image imgMaison = new Image("icone-maison.png");

    public Partie() {

    }

    public void update(double deltaTemps, GraphicsContext context) {

        //Si la partie fini, dessiner l ecran de fin de partie
        if (partieFinie) {
            timerFinPartie += deltaTemps;
            drawEcranFin(context);

            //apres 3 secondes, on arrete de dessiner et recommence au niveau 1
            if (timerFinPartie >= 3) {
                resetPartie();
                chargerNiveau(1);
                demarrerTransition();
            }
            return;
        }

        //Si on est en changement de niveau, dessiner l'écran de transition
        if (enTransitionNiveau) {
            compteurTransition += deltaTemps;

            drawTransitionNiveau(context);

            //Apres 3 secondes, on arrete de dessiner et commence le niveau
            if (compteurTransition >= 3) {
                enTransitionNiveau = false;
            }
            return;
        }

        camelot.supprimerJournaux(camera);
        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);

        traiterCollisionsJournaux();

        //Vérifier a chaque frame que les conditions de fin de niveau sont remplies ou non
        conditionPourChargerNiveau();

        boolean zPressed = Input.isKeyPressed(KeyCode.Z);
        boolean xPressed = Input.isKeyPressed(KeyCode.X);

        //Lancer un journal toutes les 0,5sec
        tempsEcouleLance += deltaTemps;
        if (tempsEcouleLance >= 0.5 && (zPressed || xPressed)) {
            lancerJournalCamelot();
            tempsEcouleLance = 0;
        }

    }

    private void traiterCollisionsJournaux() {
        ArrayList<Journal> journaux = camelot.getJournauxLances();
        ArrayList<Journal> journauxASupprimer = new ArrayList<>();

        for (Journal journal : journaux) {

            boolean collision = false;

            //Collision boite aux lettres
            for (Maison maison : maisons) {
                if (gererCollisionBoite(maison, journal)) {
                    collision = true;
                    break;
                }

                //Collision fenetres
                for (Fenetre fenetre : maison.fenetres) {
                    if (gererCollisionFenetre(maison, fenetre, journal)) {

                        collision = true;
                        break;
                    }
                }
                if (collision) break; //Éviter de parcourir tous les objets si on a déja trouver une collision
            }

            if (collision) {
                journauxASupprimer.add(journal);
            }
        }

        journaux.removeAll(journauxASupprimer);
    }

    private boolean gererCollisionBoite(Maison maison, Journal journal) {

        if (maison.boite.collisionAvecJournal(journal)) {
            if (maison.boite.abonne && !maison.boite.dejaTouchee) {
                argent += 1;
            }
            maison.boite.changerCouleurBoite();
            maison.boite.dejaTouchee = true;
            return true;
        }
        return false;
    }

    private boolean gererCollisionFenetre(Maison maison, Fenetre fenetre, Journal journal) {
        if (fenetre.collisionAvecJournal(journal)) {
            if (!fenetre.estBrisee) {
                if (maison.abonnee) {
                    argent -= 2;
                } else {
                    argent += 2;
                }
                fenetre.changerCouleurFenetre();
                fenetre.estBrisee = true;
            }
            return true;
        }
        return false;
    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        drawBrique(context);

        drawMaisons(context, camera);

        camelot.draw(context, camera);
        drawHUD(context);
    }

    public void drawBrique(GraphicsContext context) {

        double largeurBrique = 192;
        double hauteurBrique = 96;

        double debut = camera.getPositionCamera().getX();
        double fin = debut + MainJavaFX.WIDTH;

        int indexDebut = (int) Math.floor(debut / largeurBrique) - 1;
        int indexFin = (int) Math.ceil(fin / largeurBrique) + 1;


        for (int j = 0; j < MainJavaFX.HEIGHT; j += (int) hauteurBrique) {
            for (int i = indexDebut; i < indexFin; i++) {

                double x = i * largeurBrique;
                double xEcran = x - debut;

                context.drawImage(background, xEcran, j, largeurBrique, hauteurBrique);
            }

        }
    }

    public void drawMaisons(GraphicsContext context, Camera camera) {
        for (Maison maison : maisons) {
            maison.draw(context, camera);
        }
    }

    public void drawHUD(GraphicsContext context) {

        context.setFill(Color.rgb(0, 0, 0, 0.5)); // Fourni par ChatGPT pour un fond transparent
        context.fillRect(0, 0, MainJavaFX.WIDTH, 40);
        context.setFill(Color.WHITE);
        context.setFont(javafx.scene.text.Font.font(20));

        context.drawImage(imgJournal, 30, 5, 30, 30);
        context.fillText("" + journauxRestants, 70, 26);

        context.drawImage(imgDollar, 120, 7, 40, 25);
        context.fillText(argent + "", 170, 26);

        StringBuilder sb = new StringBuilder(); //StringBuilder : Concaténer et modifier des String efficacement

        for (Maison m : maisons) {
            if (m.abonnee) {
                sb.append(m.adresse).append("  ");
            }
        }

        context.setTextAlign(TextAlignment.LEFT);
        context.drawImage(imgMaison, 210, 5, 30, 30);
        context.fillText(sb.toString(), 210 + 30 + 5, 30);
    }

    //Méthode pour dessiner la transition de niveau
    private void drawTransitionNiveau(GraphicsContext context) {

            context.setFill(Color.BLACK);
            context.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
            context.setFill(Color.GREEN);
            context.setFont(javafx.scene.text.Font.font(40));
            context.setTextAlign(TextAlignment.CENTER);
            context.fillText("Niveau " + niveauActuel, MainJavaFX.WIDTH / 2, MainJavaFX.HEIGHT / 2);

    }

    private void drawEcranFin(GraphicsContext context) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(javafx.scene.text.Font.font(40));

        context.setFill(Color.RED);
        context.fillText("Rupture de stocks", MainJavaFX.WIDTH / 2, MainJavaFX.HEIGHT / 2 - 30);

        context.setFill(Color.GREEN);
        context.fillText("Argent collecté : " + argent + "$",
                MainJavaFX.WIDTH / 2, MainJavaFX.HEIGHT / 2 + 30);
    }

    public void ajouterMaisons() {

        int adresse = 100 + (int) (Math.random() * 851); //Numéro entre 100 et 950
        int posX = 1300;

        for (int i = 0; i < 12; i++) {

            //J'ai supprimé la création des boites aux lettres et fenetre car elles sont déjà
            //faites dans la classe Maison

            Maison m = new Maison(posX, adresse);
            maisons.add(m);

            adresse += 2;
            posX += 1300;
        }
    }

    public void demarrerTransition() {
        enTransitionNiveau = true;
        compteurTransition = 0;
    }

    public void resetPartie() {
        niveauActuel = 1;
        argent = 0;
        journauxRestants = 0;
        partieFinie = false;
    }

    public void chargerNiveau(int numeroNiveau) {
        niveauActuel = numeroNiveau;

        camelot = new Camelot();
        maisons.clear();

        if(numeroNiveau ==1){
            journauxRestants = 12;
        } else {
        journauxRestants += 12;
        }

        ajouterMaisons();

        enTransitionNiveau = true;
        compteurTransition = 0;
    }

    //Changer de niveau uniquement si l'une des conditions est remplie
    private void conditionPourChargerNiveau() {

        //condition pour fin de la partie
        if (!enTransitionNiveau &&
                journauxRestants <= 0 &&
                camelot.getJournauxLances().isEmpty()) {

            partieFinie = true;
            timerFinPartie = 0;
            return;
        }

        //condition pour prochain niveau
        if (!enTransitionNiveau &&
                camelot.getPosition().getX() >= LIMITE_NIVEAU) {

            niveauActuel++;
            chargerNiveau(niveauActuel);
            demarrerTransition();
        }
    }

    //Empecher de lancer un journal s'il n'en reste plus
    private void lancerJournalCamelot() {
        if (journauxRestants > 0) {
            camelot.lancerJournal();
            journauxRestants--;
        }
    }

    public boolean isEnTransitionNiveau() {
        return enTransitionNiveau;
    }
    public boolean isPartieFinie() {
        return partieFinie;
    }
}

