package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends Pane {

    private IJeu jeu;
    private Label winnerLabel;
    private Map<Joueur, Integer> scores;

    public VueResultats(IJeu jeu) {
        scores = new HashMap<>();
        // Création des labels
        Label titre = new Label("La partie est terminée !");
        titre.setFont(Font.font("Arial", 30));
        titre.setTextFill(Color.WHITE);

        winnerLabel = new Label("Le grand gagnant est : "); // A BINDER
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Label rankingLabel = new Label("Classement des joueurs :");
        rankingLabel.setFont(Font.font("Arial", 18));
        rankingLabel.setTextFill(Color.WHITE);

        VBox classement = new VBox();
        this.jeu = jeu;
        for (IJoueur j : jeu.getJoueurs()){
            Joueur joueur = (Joueur) j;
            Integer scoreJoueur = ((Joueur) j).calculerScoreFinal();
            scores.put(joueur, scoreJoueur);
            classement.getChildren().add(new Label(joueur.getNom() + " : " + scoreJoueur + " points"));
        }

        // classement vbox
        VBox rankingVBox = new VBox(rankingLabel, classement);
        rankingVBox.setAlignment(Pos.CENTER_LEFT);
        rankingVBox.setSpacing(10);

        // container principale
        HBox hboxMain = new HBox(titre);
        hboxMain.setAlignment(Pos.CENTER);

        // Création de la VBox principale
        VBox finJeu = new VBox(hboxMain, winnerLabel, rankingVBox);

        finJeu.setSpacing(20);
        finJeu.setPadding(new Insets(20));
        finJeu.getStyleClass().add("tooltip");
        this.getChildren().add(finJeu);
        creerBindings();
    }

    public void creerBindings(){
        String nomGagnant = "";
        Integer scoreGagnant = 0;
        for (Joueur j : scores.keySet()) {
            if (scores.get(j) > scoreGagnant){
                scoreGagnant = scores.get(j);
                nomGagnant = j.getNom();
            } else if (scores.get(j).equals(scoreGagnant)) {
                nomGagnant += " & " + j.getNom();
            }
        }
        winnerLabel.textProperty().bind(Bindings.concat(winnerLabel.getText(), nomGagnant));
    }

}
