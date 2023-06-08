package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends StackPane {
    private Label nomJoueur;
    private Label nbPionsWagons, nbPionsBateaux, nbPorts;

    private VBox scoreAffichage;


    public VueAutresJoueurs(String nomJoueur) {
        this.nomJoueur = new Label(nomJoueur);
        this.nbPionsWagons = new Label("Pions Wagons : ");
        this.nbPionsBateaux = new Label("Pions Bateaux : ");
        this.nbPorts = new Label("Nombre de ports : ");
        this.scoreAffichage  = new VBox();
        scoreAffichage.setStyle("-fx-border-color: black");
        this.getChildren().addAll(scoreAffichage,this.nomJoueur);

    }
    public void creerBinding(){
        nbPionsBateaux.setText(nbPionsBateaux.getText().concat("40"));
        nbPorts.setText(nbPorts.getText().concat("30"));
        nbPionsWagons.setText(nbPionsWagons.getText().concat("60"));
        scoreAffichage.getChildren().addAll(nbPionsBateaux,nbPionsWagons,nbPorts);
        nbPionsBateaux.setAlignment(Pos.TOP_CENTER);
        nbPionsWagons.setAlignment(Pos.CENTER);
        nbPorts.setAlignment(Pos.BOTTOM_CENTER);
        scoreAffichage.setVisible(false);
        this.setOnMouseEntered(event -> {
            scoreAffichage.setVisible(true);
            this.nomJoueur.setVisible(false);
        });
        this.setOnMouseExited(event ->{
            scoreAffichage.setVisible(false);
            this.nomJoueur.setVisible(true);
        });
    }

    public void setNomJoueur(Label nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setNbPionsWagons(Label nbPionsWagons) {
        this.nbPionsWagons = nbPionsWagons;
    }

    public void setNbPionsBateaux(Label nbPionsBateaux) {
        this.nbPionsBateaux = nbPionsBateaux;
    }

    public void setNbPorts(Label nbPorts) {
        this.nbPorts = nbPorts;
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
}
