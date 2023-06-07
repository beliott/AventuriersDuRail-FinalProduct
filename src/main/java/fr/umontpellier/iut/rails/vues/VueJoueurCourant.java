package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private Label nomJoueur;
    private VBox lesCartesDuJoueur;

    public VueJoueurCourant(String nomJoueur) {
        this.nomJoueur = new Label(nomJoueur);
        this.lesCartesDuJoueur = new VBox();
        this.getChildren().addAll(this.nomJoueur, this.lesCartesDuJoueur);
    }

    public void creerbindings(){
        ChangeListener<IJoueur> joueurCourantListener = (observableValue, oldJ, newJ) -> {
            nomJoueur.setText(newJ.getNom());
            lesCartesDuJoueur.getChildren().clear();
            for (ICarteTransport c: newJ.getCartesTransport()) {
                String lienCarte = getlinkImage(c);
                ImageView imgCarte = new ImageView(lienCarte);
                imgCarte.setPreserveRatio(true); // on preserve ses proportions
                imgCarte.setFitWidth(50);
                lesCartesDuJoueur.getChildren().add(imgCarte);
            }

        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourantListener); // cf. la fin du readme
    }

    public String getlinkImage(ICarteTransport c){
        String rep = "/images/cartesWagons/carte-";
        if (c.estDouble())
            rep +="DOUBLE-";
        else if (c.estJoker())
            rep+="JOKER-";
        else if (c.estBateau())
            rep+="BATEAU-";
        else if (c.estWagon())
            rep+="WAGON-";

        rep+=c.getStringCouleur();

        if (c.getAncre())
            rep+="-A";
        rep+=".png";

        return rep;
    }
}
