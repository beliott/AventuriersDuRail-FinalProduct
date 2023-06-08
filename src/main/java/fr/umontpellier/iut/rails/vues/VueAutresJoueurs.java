package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.beans.binding.Bindings;
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
    private Label nbPionsWagons, nbPionsBateaux, nbPorts, score;

    private VBox scoreAffichage;


    public VueAutresJoueurs(String nomJoueur) {
        this.nomJoueur = new Label(nomJoueur);
        this.score = new Label("Score : ");
        this.nbPionsWagons = new Label("Pions Wagons : " );
        this.nbPionsBateaux = new Label("Pions Bateaux : ");
        this.nbPorts = new Label("Nombre de ports : ");
        this.scoreAffichage  = new VBox();
        this.getChildren().addAll(scoreAffichage,this.nomJoueur);

    }

    IJoueur getJoueur(String nom){
        for (IJoueur j : ((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs()) {
            if (j.getNom().equals(this.nomJoueur.getText()))
                return j;
        }
        return null;
    }
    public void creerBinding(){
        nbPionsBateaux.textProperty().bind(Bindings.concat(nbPionsBateaux.getText(),((Joueur)getJoueur(this.nomJoueur.getText())).nbPionsBateauxProperty()));
        //nbPionsBateaux.setText(nbPionsBateaux.getText().concat("40"));
        nbPorts.setText(nbPorts.getText().concat("30"));

        nbPionsWagons.textProperty().bind(Bindings.concat(nbPionsWagons.getText(),((Joueur)getJoueur(this.nomJoueur.getText())).nbPionsWagonsProperty()));
        //nbPionsWagons.setText(nbPionsWagons.getText().concat("20"));
        scoreAffichage.getChildren().addAll(score,nbPionsBateaux,nbPionsWagons,nbPorts);
        nbPionsBateaux.setAlignment(Pos.TOP_CENTER);
        nbPionsWagons.setAlignment(Pos.CENTER);
        nbPorts.setAlignment(Pos.BOTTOM_CENTER);
        scoreAffichage.setVisible(false);
        scoreAffichage.setStyle("-fx-background-color: #6F4E37; -fx-text-fill: white; -fx-font-size: 20px; " +
                "-fx-padding: 10px; -fx-border-color: #543A29; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");


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

    public Label getNomJoueur() {
        return nomJoueur;
    }
}
