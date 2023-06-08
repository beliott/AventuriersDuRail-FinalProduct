package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;



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
    private final ListChangeListener<IDestination> toto = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (IDestination iDestination : change.getAddedSubList()) {
                    Button b = new Button();
                    VBox v = new VBox();
                    HBox h1 = new HBox();
                    HBox h2 = new HBox();
                    v.getChildren().addAll(h1,h2);
                    Label valeur = new Label();
                    Label ville1= new Label(iDestination.getVilles().get(0));
                    Label ville2 = new Label(iDestination.getVilles().get(iDestination.getVilles().size()-1));
                    h1.getChildren().addAll(ville1);
                    h2.getChildren().add(ville2);
                    h2.setAlignment(Pos.CENTER_RIGHT);
                    if (iDestination.getVilles().size() <= 2){
                        valeur.setText(String.valueOf(iDestination.getValeur()));
                        h1.getChildren().add(valeur);
                    }
                    else {
                        valeur.setText(String.valueOf(iDestination.getValeurMax()));
                        h1.getChildren().add(valeur);
                    }
                    valeur.setAlignment(Pos.TOP_RIGHT);
                    h1.setSpacing(50);
                    b.setGraphic(v);
                    b.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                    listeDestination.setSpacing(10);
                    b.setId(iDestination.getNom());
                    listeDestination.getChildren().add(b);
                }
            } else if (change.wasRemoved()) {
                for (IDestination iDestination : change.getRemoved()) {
                    listeDestination.getChildren().remove(removeDestination(iDestination));
                }
            }
    };

};

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();

        //4
        Label actionARealiser = new Label();
        actionARealiser.textProperty().bind(jeu.instructionProperty());
        actionARealiser.setStyle("-fx-font-family: Chilanka; -fx-font-size: 25px ");

        //3 Bouton passer
        Button passer = new Button("Passer");
        passer.setStyle("-fx-background-color: #0078B8; -fx-text-fill: white; -fx-font-size: 14px;");
        passer.setOnMouseClicked(event -> {
            jeu.passerAEteChoisi();
        });
        DropShadow dropShadow = new DropShadow(10, Color.GRAY);
        passer.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> passer.setEffect(dropShadow));
        passer.addEventHandler(MouseEvent.MOUSE_EXITED, event -> passer.setEffect(null));



        listeDestination = new HBox();
        jeu.destinationsInitialesProperty().addListener(toto);

        // 8
        jCourant = new VueJoueurCourant(jeu.joueurCourantProperty().get());
        HBox plateauEtJoueur = new HBox(plateau, jCourant);
        plateauEtJoueur.setStyle("-fx-background-color: tan");
        plateauEtJoueur.setPadding(new Insets(20, 20, 20, 20));
        plateauEtJoueur.setSpacing(20);

        /*Vue autre Joueur*/

        this.jPasCourant = new ArrayList<>();
        for(IJoueur j : jeu.getJoueurs()) {
            if (!jCourant.equals(j)) {
                VueAutresJoueurs v = new VueAutresJoueurs(j.getNom());
                this.jPasCourant.add(v);
                v.creerBinding();
                v.setStyle("-fx-background-color: " + v.traduceColor(j.getCouleur()));
            }
        }


        /* Affichage en bas a droite */

        VBox partieBasDroite = new VBox();
        HBox container = new HBox();
        partieBasDroite.prefWidthProperty().bind(jCourant.prefWidthProperty());
        for (VueAutresJoueurs v : this.jPasCourant) {
          container.getChildren().add(v);
        }
        partieBasDroite.setPadding(new Insets(10,10,10,10));
        partieBasDroite.setSpacing(10);
        partieBasDroite.getChildren().add(container);
        partieBasDroite.getChildren().add(passer);
        passer.prefWidthProperty().bind(partieBasDroite.prefWidthProperty());


        /* Affichage en bas */
        BorderPane partieBas = new BorderPane();
        partieBas.setLeft(listeDestination);
        partieBas.setRight(partieBasDroite);




        setBackGround();
        this.getChildren().addAll( actionARealiser, plateauEtJoueur,partieBas);
        //getChildren().add(plateau);
    }
    public void setBackGround(){
        Image bground = new Image("bground.png");
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
        plateau.prefWidthProperty().bind(getScene().widthProperty().divide(1.3));
        plateau.prefHeightProperty().bind(getScene().heightProperty().divide(1.3));

        jCourant.prefWidthProperty().bind(getScene().widthProperty().subtract(plateau.prefWidthProperty()));
        jCourant.prefHeightProperty().bindBidirectional(plateau.prefHeightProperty());
        jCourant.maxHeightProperty().bind(plateau.prefHeightProperty());

        plateau.creerBindings();
        jCourant.creerbindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

    public VuePlateau getPlateau() {
        return plateau;
    }
}
