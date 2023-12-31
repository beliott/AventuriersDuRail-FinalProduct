package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends VBox {

    private final IJeu jeu;
    private VuePlateau plateau;

    private HBox listeDestination;
    private VueJoueurCourant jCourant;

    private List<VueAutresJoueurs> jPasCourant;

    private HBox plateauEtJoueur;

    private BorderPane partieBas;
    private HBox saisiePionBox;
    private Label actionARealiser;
    private HBox cartesPiochables;

    public Label getActionARealiser() {
        return actionARealiser;
    }

    private int nombreDeJoueurs;
    private int indice;

    private List<TextField> utile;
    private List<String> nomDesJoueurs;

    private HBox container; // container des vues autres joueurs
    private Button wagonsBouton;
    private Button bateauxBouton;
    private VBox partieBasDroite;

    private FlowPane cartesVisibles;
    private StackPane stackpanePion;
    private ImageView dosWagon = new ImageView("images/cartesWagons/dos-WAGON.png");
    private ImageView dosBateau = new ImageView("images/cartesWagons/dos-BATEAU.png");
    private ImageView dosDestination = new ImageView("images/cartesWagons/destinations.png");
    Image bgroundo = new Image("destination.jpg");
    BackgroundImage background = new BackgroundImage(bgroundo,
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
            BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
    Background backgroundImage = new Background(background);
    EventHandler<MouseEvent> mouseEventPiocherCarteWagon = mouseEvent -> {
        getJeu().uneCarteWagonAEtePiochee();
    };
    EventHandler<MouseEvent> mouseEventPiocherCarteBateau = mouseEvent -> {
        getJeu().uneCarteBateauAEtePiochee();
    };
    EventHandler<MouseEvent> mouseEventPiocheDestinations = mouseEvent -> {
        getJeu().nouvelleDestinationDemandee();
        partieBas.setCenter(listeDestination);
    };

    public VueDuJeu(IJeu jeu) {
        this.nomDesJoueurs = new ArrayList<>();
        this.jeu = jeu;
        plateau = new VuePlateau();

        /* DEBUT EN COURS AVEC MONSIEUR NADAL*/
        //4
        this.actionARealiser = new Label();
        actionARealiser.textProperty().bind(jeu.instructionProperty());
        actionARealiser.setStyle("-fx-text-fill: white;-fx-font-family: Chilanka; -fx-font-size: 25px;-fx-font-weight: bold; ");
        actionARealiser.setPadding(new Insets(10,0,0,80));
        //3 Bouton passer
        Button passer = new Button("Passer");
        passer.setStyle("-fx-background-color: #0078B8; -fx-text-fill: white; -fx-font-size: 14px;-fx-font-weight: bold;");
        passer.setOnMouseClicked(event -> {
            jeu.passerAEteChoisi();
        });
        DropShadow dropShadow = new DropShadow(10, Color.GRAY);
        passer.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> passer.setEffect(dropShadow));
        passer.addEventHandler(MouseEvent.MOUSE_EXITED, event -> passer.setEffect(null));


        listeDestination = new HBox();
        listeDestination.setAlignment(Pos.CENTER);
        listeDestination.prefWidthProperty().bind(plateau.prefWidthProperty());
        // 8
        jCourant = new VueJoueurCourant(jeu.joueurCourantProperty().get());
        HBox h1 = new HBox(plateau);
        HBox h2 = new HBox(jCourant);
        this.plateauEtJoueur = new HBox(h1, h2);
        h1.setAlignment(Pos.CENTER);
        h2.setAlignment(Pos.CENTER);
        //plateauEtJoueur.setStyle("-fx-background-color: tan");
        plateauEtJoueur.setPadding(new Insets(20, 20        , 0, 0));
        plateauEtJoueur.setSpacing(5);

        /*Vue autre Joueur*/

        this.jPasCourant = new ArrayList<>();
        for(IJoueur j : jeu.getJoueurs()) {
            VueAutresJoueurs v = new VueAutresJoueurs(j.getNom());
            this.jPasCourant.add(v);
            v.setStyle("-fx-background-color: " + v.traduceColor(j.getCouleur()));
        }


        /* piocher cartes */
        this.cartesPiochables = new HBox(dosWagon, dosBateau, dosDestination);
        cartesPiochables.setSpacing(20); cartesPiochables.setAlignment(Pos.BASELINE_CENTER);
        dosWagon.setPreserveRatio(true); dosWagon.setFitHeight(100); dosWagon.setOnMouseClicked(mouseEventPiocherCarteWagon);
        dosBateau.setPreserveRatio(true); dosBateau.setFitHeight(100); dosBateau.setOnMouseClicked(mouseEventPiocherCarteBateau);
        dosDestination.setPreserveRatio(true); dosDestination.setFitWidth(100); dosDestination.setOnMouseClicked(mouseEventPiocheDestinations);



        /* Affichage en bas a droite */

        this.partieBasDroite = new VBox();
        HBox container = new HBox();
        partieBasDroite.prefWidthProperty().bind(jCourant.prefWidthProperty());
        for (VueAutresJoueurs v : this.jPasCourant) {
          container.getChildren().add(v);
        }
        container.setAlignment(Pos.CENTER);
        container.setSpacing(5);
        partieBasDroite.setSpacing(25);
        partieBasDroite.setPadding(new Insets(10,20,10,20));
        partieBasDroite.getChildren().add(container);
        partieBasDroite.getChildren().add(passer);
        passer.prefWidthProperty().bind(partieBasDroite.prefWidthProperty());
        //container.prefWidthProperty().bind(partieBasDroite.prefWidthProperty());



        /* SAISIE PIONS*/
        TextField saisieNbPions = new TextField();
        saisieNbPions.setMaxWidth(50);
        Button valider = new Button("Valider");
        valider.setOnMouseClicked(mouseEvent -> {
            if (!saisieNbPions.getText().equals("")){
                jeu.leNombreDePionsSouhaiteAEteRenseigne(saisieNbPions.getText());
                saisieNbPions.clear();
            }
        });

        /* visibles */
        cartesVisibles = new FlowPane();
        cartesVisibles.setHgap(20);
        cartesVisibles.setVgap(10);
        ListChangeListener<ICarteTransport> cartesVisiblesChangement = change -> {
            while (change.next()){
                if (change.wasAdded()){
                    for (ICarteTransport c : change.getAddedSubList()){
                        VueCarteTransport vct = new VueCarteTransport(c);
                        vct.setPrefWidth(120);
                        vct.setOnMouseClicked(mouseEvent -> {
                            jeu.uneCarteTransportAEteChoisie(c);
                        });
                        cartesVisibles.getChildren().add(vct);
                    }

                }
                if (change.wasRemoved()){
                    for (ICarteTransport c : change.getRemoved()) {
                        for (Node n : cartesVisibles.getChildren()){
                            VueCarteTransport v = (VueCarteTransport) n;
                            if (v.getCarteTransport().equals(c)){
                                cartesVisibles.getChildren().remove(v);
                                break;
                            }
                        }
                    }
                }
            }
        };
        jeu.cartesTransportVisiblesProperty().addListener(cartesVisiblesChangement);
        cartesPiochables.getChildren().add(cartesVisibles);

        //jeu.unPortAEteChoisi();
        HBox saisiePions = new HBox(saisieNbPions, valider);
        /* Affichage en bas */
        partieBas = new BorderPane();
        this.saisiePionBox= new HBox(saisiePions);

        saisiePionBox.setPadding(new Insets(0,0,0,80));
        //partieBas.setLeft(listeDestination);
        partieBas.setBottom(saisiePionBox);
        partieBas.setCenter(listeDestination);
        //partieBas.getChildren().add(saisiePions);
        // TEST AJOUT POUR NBPIONS
        partieBas.setRight(partieBasDroite);
        partieBas.setPadding(new Insets(5,0,10,0));


        setBackGround();
        this.getChildren().addAll( actionARealiser, plateauEtJoueur,partieBas);

    }

    public void setBackGround(){
        Image bground = new Image("bground.jpg");
        BackgroundImage background = new BackgroundImage(bground,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background backgroundImage = new Background(background);
        this.setBackground(backgroundImage);
    }
    public Button removeDestination(IDestination destination){
        for(Node n : listeDestination.getChildren()){
            Button boutonDestination = (Button) n;
            if(boutonDestination.getId().equals(destination.getNom())){
                return boutonDestination;
            }
        }
        return null;

    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty().divide(1.5));
        plateau.prefHeightProperty().bind(getScene().heightProperty().divide(1));

        jCourant.prefWidthProperty().bind(getScene().widthProperty().subtract(plateau.prefWidthProperty()));
        jCourant.prefHeightProperty().bindBidirectional(plateau.prefHeightProperty());
        jCourant.maxHeightProperty().bind(plateau.prefHeightProperty());







        /*for (VueAutresJoueurs v : jPasCourant) {
            v.prefWidthProperty().bind(jCourant.prefWidthProperty().divide(5));
            v.prefHeightProperty().bind(jCourant.prefHeightProperty().divide(5));
        }
        listeDestination.prefWidthProperty().bind(jCourant.prefWidthProperty().divide(5));
        listeDestination.prefHeightProperty().bind(jCourant.prefHeightProperty().divide(5));*/


        /* pour defausser des cartes destinations */

        ListChangeListener<IDestination> toto = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (IDestination iDestination : change.getAddedSubList()) {
                        Button b = new Button();
                    b.setBackground(backgroundImage);
                        BorderPane v = new BorderPane();
                        BorderPane h1 = new BorderPane();
                        BorderPane h2 = new BorderPane();
                        v.setTop(h1);
                        v.setBottom(h2);
                        Label valeur = new Label();
                        Label ville1 = new Label(iDestination.getVilles().get(0));
                        Label ville2 = new Label(iDestination.getVilles().get(iDestination.getVilles().size() - 1));
                        h1.setLeft(ville1);
                        h1.setRight(valeur);
                        h2.setRight(ville2);
                        v.setPrefSize(200, 200);
                        if (iDestination.getVilles().size() <= 2) {
                            valeur.setText(String.valueOf(iDestination.getValeur()));
                        } else {
                            valeur.setText(String.valueOf(iDestination.getValeurMax()));
                        }
                        b.setPrefSize(150, 75);
                        b.setGraphic(v);
                        valeur.setStyle("-fx-font-size: 25px");
                        b.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
                        listeDestination.setSpacing(10);
                        b.setId(iDestination.getNom());
                        b.setOnMouseClicked(mouseEvent -> {
                            jeu.uneDestinationAEteChoisie(iDestination);
                        });
                        listeDestination.getChildren().add(b);
                    }
                } else if (change.wasRemoved()) {
                    for (IDestination iDestination : change.getRemoved()) {
                        listeDestination.getChildren().remove(removeDestination(iDestination));
                    }
                }
            }
        }; jeu.destinationsInitialesProperty().addListener(toto);


        plateau.creerBindings();
        jCourant.creerbindings();


        /* pour VueAutresJoueurs */
        for (VueAutresJoueurs v : jPasCourant){
            v.creerBinding();
        }

        /* Listener du joueur actuel */
        ChangeListener<IJoueur> listenerJoueurAffichage = (observableValue, oldJ, newJ) -> {
            String nomJActuel = newJ.getNom();
            HBox parent = new HBox();
            VueAutresJoueurs vueDelAncien = new VueAutresJoueurs("");
            for ( VueAutresJoueurs v : jPasCourant){
                if (nomJActuel.equals(v.getNomJoueur().getText())){
                    HBox p = (HBox) v.getParent();
                    parent = p;
                    p.getChildren().remove(v);
                } else if (oldJ != null) {
                    if (oldJ.getNom().equals(v.getNomJoueur().getText())) {
                        vueDelAncien = v;
                    }
                }
            }
            parent.getChildren().add(vueDelAncien);
        };


        this.jeu.joueurCourantProperty().addListener(listenerJoueurAffichage);
        actionARealiser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (actionARealiser.getText().contains("Vous pouvez défausser") && actionARealiser.getText().contains("destination") ){
                partieBas.getChildren().clear();
                partieBas.setCenter(listeDestination);
                partieBas.setRight(partieBasDroite);
                if (!jCourant.getUtile().isEmpty()){
                    jCourant.getUtile().clear();
                }
            }
            if (actionARealiser.getText().contains("Saisissez un nombre de pion")){
                partieBas.getChildren().clear();
                partieBas.setRight(partieBasDroite);
                partieBas.setCenter(saisiePionBox);
                bateauxBouton.setVisible(false);
                wagonsBouton.setVisible(false);
                saisiePionBox.setVisible(true);


            }
            if (actionARealiser.getText().contains("Début du tour")  || actionARealiser.getText().contains("Avant-dernier tour de") || actionARealiser.getText().contains("Dernier tour de")){
                partieBas.getChildren().clear();
                partieBas.setRight(partieBasDroite);
                partieBas.setCenter(cartesPiochables);
                partieBas.setBottom(stackpanePion);
                saisiePionBox.setVisible(false);
                bateauxBouton.setVisible(true);
                wagonsBouton.setVisible(true);
                dosDestination.setVisible(true);
                if (!jCourant.getUtile().isEmpty()){
                    jCourant.getUtile().clear();
                }
            }
            if (actionARealiser.getText().contains("révéler une carte")){
                partieBas.getChildren().clear();
                partieBas.setRight(partieBasDroite);
                partieBas.setCenter(cartesPiochables);
                dosDestination.setVisible(false);
                if (!jCourant.getUtile().isEmpty()){
                    jCourant.getUtile().clear();
                }
            }
            if (actionARealiser.getText().contains("Il faut défausser") && actionARealiser.getText().contains("pour prendre la")) {
                partieBas.getChildren().clear();
                partieBas.setRight(partieBasDroite);
                if (!jCourant.getUtile().isEmpty()) {
                    for (VueCarteTransport v : jCourant.getUtile()) {
                        v.getStyleClass().add("tooltip");
                    }

                }
            }
        });

        /* Boutons pions Wagons/Bateaux */
        this.wagonsBouton = new Button("Prendre des pions Wagons");
        this.bateauxBouton= new Button("Prendre des pions Bateaux");

        HBox hbox1 = new HBox(wagonsBouton);
        HBox hbox2 = new HBox(bateauxBouton);

        HBox boxPions = new HBox(hbox1, hbox2);
        boxPions.setSpacing(20);


        /*TextField t1 = new TextField();
        t1.setPromptText("Veuillez saisir le nombre de pions wagons que vous souhaitez ");
        t1.setStyle("-fx-font-weight: bold");
        t1.setVisible(false);
        TextField t2 = new TextField();
        t2.setPromptText("Veuillez saisir le nombre de pions bateaux que vous souhaitez");
        t2.setStyle("-fx-font-weight: bold");
        t2.setVisible(false);*/

        wagonsBouton.setOnAction(e -> {
            jeu.nouveauxPionsWagonsDemandes();
            saisiePionBox.setVisible(true);
            wagonsBouton.setVisible(false);
            bateauxBouton.setVisible(false);
        });

        bateauxBouton.setOnAction(e -> {
            saisiePionBox.setVisible(true);
            jeu.nouveauxPionsBateauxDemandes();
            bateauxBouton.setVisible(false);
            wagonsBouton.setVisible(false);
        });
        this.stackpanePion= new StackPane();
        stackpanePion.getChildren().addAll(boxPions);
        stackpanePion.setPrefSize(60,25);
        stackpanePion.setPadding(new Insets(5,0,10,90));

    }

    public IJeu getJeu() {
        return jeu;
    }

    public void debutDePartie(){

    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

    public VuePlateau getPlateau() {
        return plateau;
    }
}
