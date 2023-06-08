package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends BorderPane {
    // top
    private Label scoreJoueur;
    // center
    private Label nomJoueur;
    private FlowPane lesCartesDuJoueur;
    // bottom
    private Label dest, nbPionsWagons, nbPionsBateaux, nbPorts;

    public VueJoueurCourant(IJoueur joueurCourant) {
        this.scoreJoueur = new Label("TEST");

        this.dest = new Label("Destinations");
        this.nomJoueur = new Label();
        this.lesCartesDuJoueur = new FlowPane(); lesCartesDuJoueur.setHgap(20); lesCartesDuJoueur.setVgap(20); lesCartesDuJoueur.setPadding(new Insets(20, 20 ,20 ,20));
        VBox centre = new VBox(this.nomJoueur, this.lesCartesDuJoueur);

        ImageView imgW = new ImageView("images/bouton-pions-wagon.png");
        imgW.setPreserveRatio(true); imgW.setFitWidth(30);
        this.nbPionsWagons = new Label();
        ImageView imgB = new ImageView("images/bouton-pions-bateau.png");
        imgB.setPreserveRatio(true); imgB.setFitWidth(30);
        this.nbPionsBateaux = new Label();
        ImageView imgP = new ImageView("images/port.png");
        imgP.setPreserveRatio(true); imgP.setFitWidth(30);
        this.nbPorts = new Label();
        HBox bas = new HBox(dest, imgW, nbPionsWagons, imgB, nbPionsBateaux, imgP, nbPorts);
        bas.setSpacing(10); bas.setAlignment(Pos.CENTER);


        this.setTop(scoreJoueur);
        this.setCenter(centre);
        this.setBottom(bas);

    }

    public String traduceColor(IJoueur.CouleurJoueur c){
        if(c.equals(IJoueur.CouleurJoueur.BLEU))
            return "cadetblue";
        else if (c.equals(IJoueur.CouleurJoueur.JAUNE)) {
            return "yellow";
        }
        else if (c.equals(IJoueur.CouleurJoueur.ROSE)) {
            return "pink";
        }
        else if (c.equals(IJoueur.CouleurJoueur.ROUGE)) {
            return "crimson";
        }
        else if (c.equals(IJoueur.CouleurJoueur.VERT)) {
            return "yellowgreen";
        }
        return "gray";
    }
    public void creerbindings(){

        ChangeListener<IJoueur> joueurCourantListener = (observableValue, oldJ, newJ) -> {
            this.setStyle("-fx-background-color: " + traduceColor(newJ.getCouleur())); // background
            scoreJoueur.setText("Score : "+ newJ.getScore()); // verif si il faut pas faire avec property

            nomJoueur.setText(newJ.getNom());
            // Les cartes transport
            lesCartesDuJoueur.getChildren().clear();
            for (ICarteTransport c: newJ.getCartesTransport()) {
                String lienCarte = getlinkImage(c);
                ImageView imgCarte = new ImageView(lienCarte);
                imgCarte.setPreserveRatio(true); // on preserve ses proportions
                imgCarte.setFitWidth(100);
                lesCartesDuJoueur.getChildren().add(imgCarte);
            }

            //Partie BOX
            if (nbPionsWagons.textProperty().isBound()){
                nbPionsWagons.textProperty().unbind();
                nbPionsBateaux.textProperty().unbind();
            }
            nbPionsWagons.textProperty().bind(newJ.nbPionsWagonsProperty().asString());
            nbPionsBateaux.textProperty().bind(newJ.nbPionsBateauxProperty().asString());
            nbPorts.setText("" + (3 - newJ.getNbPorts())); // TODO : bind pour que ca soit correct


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
