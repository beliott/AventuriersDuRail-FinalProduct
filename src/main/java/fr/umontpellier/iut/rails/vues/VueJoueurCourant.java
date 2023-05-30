package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private Label nomJoueur;

    public VueJoueurCourant(String nomJoueur) {
        this.nomJoueur = new Label(nomJoueur);
        this.getChildren().add(this.nomJoueur);
    }

    public void creerbindings(){
        ChangeListener<IJoueur> joueurCourantListener = (observableValue, oldJ, newJ) -> {
            nomJoueur.setText(newJ.getNom());
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourantListener); // cf. la fin du readme
    }
}
