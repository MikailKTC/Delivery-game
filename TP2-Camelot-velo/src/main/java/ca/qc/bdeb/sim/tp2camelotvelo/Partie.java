package ca.qc.bdeb.sim.tp2camelotvelo;

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
    private ArrayList<ParticuleChargee> particules = new ArrayList<>();

    private Camera camera = new Camera();
    private Image background = new Image("brique.png");

    private int niveauActuel = 1;
    private int argent = 0;
    private int journauxRestants = 0;

    private double masseJournaux;

    private double tempsEcouleLance = 0;
    private boolean enTransitionNiveau = false;
    private double compteurTransition = 0;

    private boolean partieFinie = false;
    private double timerFinPartie = 0;

    private Image imgJournal = new Image("icone-journal.png");
    private Image imgDollar = new Image("icone-dollar.png");
    private Image imgMaison = new Image("icone-maison.png");

    private boolean modeDebugD = false;
    private boolean modeDebugF = false;
    private boolean modeDebugI = false;

    private ArrayList<Point2D> positionsVecteursFixes = new ArrayList<>();
    private ArrayList<Point2D> vecteursFixes = new ArrayList<>();
    private boolean vecteursInit = false; // pour ne calculer qu'une seule fois

    public Partie() {

    }

    public void update(double deltaTemps, GraphicsContext context) {

        if (levelManager(deltaTemps, context)) {
            return;  // si on est encore en transition, on empêche le code de continuer
        }

        camelot.supprimerJournaux(camera);

        updateAccelerationJournaux();

        camelot.update(deltaTemps);
        camera.suivreCamelot(camelot);
        updateParticules(deltaTemps);

        traiterCollisionsJournaux();
        updateLancerJournaux(deltaTemps);
    }


    //---- Gérer les collisions avec les objets ----
    private void traiterCollisionsJournaux() {
        ArrayList<Journal> journaux = camelot.getJournauxLances();
        ArrayList<Journal> journauxASupprimer = new ArrayList<>();

        for (Journal journal : journaux) {

            if (detecterEtGererCollisionMaison(journal)) {
                journauxASupprimer.add(journal);
            }
        }

        journaux.removeAll(journauxASupprimer);
    }

    private boolean detecterEtGererCollisionMaison(Journal journal) {
        for (Maison maison : maisons) {
            // Vérification de la collision avec la boîte aux lettres
            if (gererCollisionBoite(maison, journal)) {
                return true;
            }

            // Vérification de la collision avec les fenêtres
            for (Fenetre fenetre : maison.getFenetres()) {
                if (gererCollisionFenetre(maison, fenetre, journal)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean gererCollisionBoite(Maison maison, Journal journal) {

        if (maison.getBoite().collisionAvecJournal(journal)) {
            if (maison.getBoite().isAbonne() && !maison.getBoite().isDejaTouchee()) {
                argent += 1;
            }
            maison.getBoite().changerCouleurBoite();
            maison.getBoite().setDejaTouchee(true);
            return true;
        }
        return false;
    }

    private boolean gererCollisionFenetre(Maison maison, Fenetre fenetre, Journal journal) {
        if (fenetre.collisionAvecJournal(journal)) {
            if (!fenetre.isEstBrisee()) {
                if (maison.isAbonnee()) {
                    argent -= 2;
                } else {
                    argent += 2;
                }
                fenetre.changerCouleurFenetre();
                fenetre.setEstBrisee(true);
            }
            return true;
        }
        return false;
    }


    //---- Draw tous les éléments du jeu ----
    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        drawBrique(context);
        drawMaisons(context, camera);
        drawParticules(context, camera);
        camelot.draw(context, camera);
        drawHUD(context);

        if (modeDebugD) {
            drawDebugD(context);
        }
        if (modeDebugF) {
            drawVecteursChamp(context);
        }

    }

    private void drawBrique(GraphicsContext context) {

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

    private void drawMaisons(GraphicsContext context, Camera camera) {
        for (Maison maison : maisons) {
            maison.draw(context, camera);
        }
    }

    private void drawParticules(GraphicsContext context, Camera camera) {
        for (ParticuleChargee p : particules) {
            p.drawExtras(context, camera);
        }
    }

    private void drawHUD(GraphicsContext context) {

        context.setFill(Color.rgb(0, 0, 0, 0.5)); // Fourni par ChatGPT pour un fond transparent
        context.fillRect(0, 0, MainJavaFX.WIDTH, 40);
        context.setFill(Color.WHITE);
        context.setFont(javafx.scene.text.Font.font(20));

        context.drawImage(imgJournal, 30, 5, 30, 30);
        context.fillText("" + journauxRestants, 70, 26);

        context.drawImage(imgDollar, 120, 7, 40, 25);
        context.fillText(argent + "", 170, 26);

        StringBuilder sb = new StringBuilder();

        for (Maison m : maisons) {
            if (m.isAbonnee()) {
                sb.append(m.getAdresse()).append("  ");
            }
        }

        context.setTextAlign(TextAlignment.LEFT);
        context.drawImage(imgMaison, 210, 5, 30, 30);
        context.fillText(sb.toString(), 210 + 30 + 5, 30);
    }

    private void drawTransitionNiveau(GraphicsContext context) {

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
        context.setFill(Color.GREEN);
        context.setFont(javafx.scene.text.Font.font(40));
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText("Niveau " + niveauActuel, (double) MainJavaFX.WIDTH / 2, (double) MainJavaFX.HEIGHT / 2);

    }

    private void drawEcranFin(GraphicsContext context) {

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(javafx.scene.text.Font.font(40));

        context.setFill(Color.RED);
        context.fillText("Rupture de stocks", (double) MainJavaFX.WIDTH / 2, (double) MainJavaFX.HEIGHT / 2 - 30);

        context.setFill(Color.GREEN);
        context.fillText("Argent collecté : " + argent + "$",
                (double) MainJavaFX.WIDTH / 2, (double) MainJavaFX.HEIGHT / 2 + 30);
    }

    private void drawDebugD(GraphicsContext context) {

        context.setStroke(Color.YELLOW);
        context.setLineWidth(2);

        double xLigne = MainJavaFX.WIDTH * 0.2;
        context.strokeLine(xLigne, 0, xLigne, MainJavaFX.HEIGHT);

        for (Maison m : maisons) {

            context.strokeRect(
                    m.getBoite().position.getX() - camera.getPositionCamera().getX(),
                    m.getBoite().position.getY(),
                    81,
                    76
            );

            for (Fenetre f : m.getFenetres()) {
                context.strokeRect(f.position.getX() - camera.getPositionCamera().getX(), f.position.getY(), 159, 130);
            }
        }

        for (Journal j : camelot.getJournauxLances()) {
            context.strokeRect(j.getPosition().getX() - camera.getPositionCamera().getX(), j.getPosition().getY(), 52, 31);
        }


    }

    private void drawVecteursChamp(GraphicsContext context) {

        calculerVecteursDebugChamp();

        int margeAffichage = 30; //Afficher un peu à l'avance pour éviter qu'ils apparaissent brusquement

        //  Dessiner les vecteurs stockés
        for (int i = 0; i < positionsVecteursFixes.size(); i++) {

            Point2D positionMonde = positionsVecteursFixes.get(i);
            Point2D champ = vecteursFixes.get(i);

            double xEcran = positionMonde.getX() - camera.getPositionCamera().getX();
            double yEcran = positionMonde.getY();

            // Dessiner uniquement si le vecteur est visible à l'écran (avec une petite marge de 30)
            if (xEcran >= -margeAffichage && xEcran < MainJavaFX.WIDTH + margeAffichage) {
                UtilitairesDessins.dessinerVecteurForce(new Point2D(xEcran, yEcran), champ, context);
            }
        }
    }


    //Gérer le système de niveau
    public void demarrerTransition() {
        enTransitionNiveau = true;
        compteurTransition = 0;
    }

    private void resetPartie() {
        niveauActuel = 1;
        argent = 0;
        journauxRestants = 0;
        partieFinie = false;
    }

    public void chargerNiveau(int numeroNiveau) {
        niveauActuel = numeroNiveau;

        camelot = new Camelot();
        maisons.clear();
        particules.clear();
        vecteursFixes.clear();
        positionsVecteursFixes.clear();


        if (numeroNiveau == 1) {
            journauxRestants = 12;
        } else {
            journauxRestants += 12;
        }

        masseJournaux = 1 + Math.random();

        ajouterMaisons();
        ajouterParticules();

        vecteursInit = false;
        modeDebugF = false;
        modeDebugD = false;
        modeDebugI = true;

        calculerVecteursDebugChamp();

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

    private boolean levelManager(double deltaTemps, GraphicsContext context) {

        //Si la partie finie, dessiner l'écran de fin de partie
        if (partieFinie) {
            timerFinPartie += deltaTemps;
            drawEcranFin(context);

            //apres 3 secondes, on arrete de dessiner et recommence au niveau 1
            if (timerFinPartie >= 3) {
                resetPartie();
                chargerNiveau(1);
                demarrerTransition();
            }
            return true;
        }

        //Si on est en changement de niveau, dessiner l'écran de transition
        if (enTransitionNiveau) {
            compteurTransition += deltaTemps;

            drawTransitionNiveau(context);

            //Apres 3 secondes, on arrete de dessiner et commence le niveau
            if (compteurTransition >= 3) {
                enTransitionNiveau = false;
            }
            return true;
        }
        conditionPourChargerNiveau();


        return false;
    }

    public boolean isEnTransitionNiveau() {
        return enTransitionNiveau;
    }

    public boolean isPartieFinie() {
        return partieFinie;
    }


    // ---- Gérer le déboggage -----

    public void ajouterJournauxDebug() {
        journauxRestants += 10;
    }

    public void setJournauxZeroDebug() {
        journauxRestants = 0;
    }

    public void prochainNiveauDebug() {
        if (!enTransitionNiveau && !partieFinie) {
            niveauActuel++;
            chargerNiveau(niveauActuel);
            demarrerTransition();
        }
    }

    public void activerDebugD() {
        modeDebugD = !modeDebugD;
    }

    public void activerDebugF() {
        modeDebugF = !modeDebugF;

    }

    public void activerDebugI() {

        if (modeDebugI) {

            modeDebugF = false;

            //On vide les vecteurs, puis les re calcul avec les nouvelles particules
            particules.clear();
            vecteursFixes.clear();
            positionsVecteursFixes.clear();

            vecteursInit = false;

            for (int i = 0; i < LIMITE_NIVEAU; i += 50) {
                particules.add(new ParticuleChargee(new Point2D(i, 10)));
                particules.add(new ParticuleChargee(new Point2D(i, MainJavaFX.HEIGHT - 10)));
            }

            modeDebugI = false;
        }

    }

    private void calculerVecteursDebugChamp() {

        if (!vecteursInit) {

            // Initialiser uniquement si les vecteurs n'ont pas encore été calculés
            for (double x = 0; x < LIMITE_NIVEAU; x += 50) {
                for (double y = 0; y < MainJavaFX.HEIGHT; y += 50) {

                    Point2D positionMonde = new Point2D(x, y);
                    Point2D champ = champElectriqueTotal(positionMonde);

                    if (champ.magnitude() >= 1) { // Ne stocker que les champs significatifs
                        positionsVecteursFixes.add(positionMonde);
                        vecteursFixes.add(champ);
                    }
                }
            }
            vecteursInit = true; // Maintenant qu'on a les vecteurs, on les calcule plus
        }
    }


    // ---- Gérer les particules ----

    private void updateParticules(double deltaTemps) {
        for (ParticuleChargee particule : particules) {
            particule.update(deltaTemps);
        }
    }

    private void ajouterParticules() {
        int n = Math.min((niveauActuel - 1) * 30, 400);

        for (int i = 0; i < n; i++) {

            double x = Math.random() * LIMITE_NIVEAU;
            double y = Math.random() * MainJavaFX.HEIGHT;

            ParticuleChargee p = new ParticuleChargee(new Point2D(x, y));
            particules.add(p);
        }
    }


    // ---- Gérer les journaux lancés -----

    //Empecher de lancer un journal s'il n'en reste plus
    private void lancerJournalCamelot() {
        if (journauxRestants > 0) {
            camelot.lancerJournal(masseJournaux);
            journauxRestants--;
        }
    }

    private void updateAccelerationJournaux() {

        for (Journal journal : camelot.getJournauxLances()) {
            journal.setAcceleration(new Point2D(0, 1500));
            Point2D forceElectrique = champElectriqueTotal(journal.getPosition()).multiply(journal.getCharge());

            Point2D accelerationChamp = forceElectrique.multiply(1 / journal.getMasse());
            journal.setAcceleration(journal.getAcceleration().add(accelerationChamp));
        }
    }

    private void updateLancerJournaux(double deltaTemps) {
        boolean zPressed = Input.isKeyPressed(KeyCode.Z);
        boolean xPressed = Input.isKeyPressed(KeyCode.X);

        //Lancer un journal toutes les 0,5sec
        tempsEcouleLance += deltaTemps;
        if (tempsEcouleLance >= 0.5 && (zPressed || xPressed)) {
            lancerJournalCamelot();
            tempsEcouleLance = 0;
        }
    }


    // Calcul champ électrique total de toutes les particules ensemble
    private Point2D champElectriqueTotal(Point2D position) {

        Point2D champTotal = new Point2D(0, 0);

        for (ParticuleChargee particule : particules) {
            champTotal = champTotal.add(particule.champElectriqueAuPoint(position));

        }
        return champTotal;

    }

    // Gérer l'ajout des fenetres et boite aux lettres
    private void ajouterMaisons() {

        int adresse = 100 + (int) (Math.random() * 851); //Numéro entre 100 et 950
        int posX = 1300;

        for (int i = 0; i < 12; i++) {

            Maison m = new Maison(posX, adresse);
            maisons.add(m);

            adresse += 2;
            posX += 1300;
        }
    }

}

