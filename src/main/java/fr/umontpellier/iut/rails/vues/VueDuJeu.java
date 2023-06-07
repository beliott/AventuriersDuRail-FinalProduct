package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.EventListener;

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

    private final ListChangeListener<IDestination> toto = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (IDestination iDestination : change.getAddedSubList()) {
                    Button b;
                    if (iDestination.getVilles().size() <= 2)
                        b = new Button(iDestination.getVilles().toString().concat(" " + String.valueOf(iDestination.getValeur())));
                    else {
                        b = new Button(iDestination.getVilles().toString().concat(" " + String.valueOf(iDestination.getValeurMax())));
                    }
                    b.setStyle("-fx-font-family: Chilanka");
                    b.setStyle("-fx-font-weight:Bold");
                    listeDestination.getChildren().add(b);

                }
            } else if (change.wasRemoved()) {
                for (IDestination iDestination : change.getRemoved()) {
                    listeDestination.getChildren().remove(removeDestination(iDestination));
                }
            }
        }
    };

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();

        //4
        Label actionARealiser = new Label();
        actionARealiser.textProperty().bind(jeu.instructionProperty());

        //3
        Button passer = new Button("Passer");

        EventHandler<MouseEvent> monHandlerAvecConvenance = MouseEvent -> {
            jeu.passerAEteChoisi();
        };
        passer.setOnMouseClicked(monHandlerAvecConvenance);
        listeDestination = new HBox();
        jeu.destinationsInitialesProperty().addListener(toto);

        // 8
        jCourant = new VueJoueurCourant(jeu.joueurCourantProperty().get());

        /* Boutons */







        this.getChildren().addAll( actionARealiser, new HBox(plateau, jCourant), passer, listeDestination);
        //getChildren().add(plateau);
    }

    public Label removeDestination(IDestination destination){
        for(Node n : listeDestination.getChildren()){
            Label labelDestination = (Label) n;
            if(labelDestination.getText().equals(destination.getVilles().toString())){
                return labelDestination;
            }
        }
        return null;

    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty().divide(1.5));
        plateau.prefHeightProperty().bind(getScene().heightProperty().divide(1.5));
        plateau.creerBindings();
        jCourant.creerbindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
