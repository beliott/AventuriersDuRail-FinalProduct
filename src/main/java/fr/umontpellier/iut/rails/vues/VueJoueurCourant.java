package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static javafx.scene.paint.Color.BLACK;

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

    private VBox centre;

    public VueJoueurCourant(IJoueur joueurCourant) {
        this.scoreJoueur = new Label("Score :");
        this.scoreJoueur.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        this.dest = new Label("Destinations");
        this.nomJoueur = new Label();
        this.nomJoueur.setStyle("-fx-font-size: 40px; -fx-font-family:IMFeENsc28P");
        this.lesCartesDuJoueur = new FlowPane(); lesCartesDuJoueur.setHgap(20); lesCartesDuJoueur.setVgap(20); lesCartesDuJoueur.setPadding(new Insets(20, 20 ,20 ,20));
        this.centre = new VBox(this.nomJoueur,this.lesCartesDuJoueur);
        this.setBorder(new Border(new BorderStroke(BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
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
        bas.setBorder(new Border(new BorderStroke(BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
        this.nbPorts.setStyle("-fx-font-weight: bold;");
        this.nbPionsBateaux.setStyle("-fx-font-weight: bold;");
        this.nbPionsWagons.setStyle("-fx-font-weight: bold;");
        BorderPane b = new BorderPane();
        this.setTop(b);
        b.setRight(scoreJoueur);
        this.setCenter(centre);
        this.setBottom(bas);
        this.setStyle("-fx-background-color: #FFC9A3");
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
        IJeu leJeu = ((VueDuJeu) getScene().getRoot()).getJeu();

        ChangeListener<IJoueur> joueurCourantListener = (observableValue, oldJ, newJ) -> {
            //this.setStyle("-fx-background-color: " + traduceColor(newJ.getCouleur())); // background
            scoreJoueur.textProperty().bind(Bindings.concat("Score : ", newJ.scoreProperty()));

            nomJoueur.setText(newJ.getNom());
            // Les cartes transport
            lesCartesDuJoueur.getChildren().clear();
            for (ICarteTransport c: newJ.getCartesTransport()) {
                // TEST
                VueCarteTransport v = new VueCarteTransport(c);
                v.setOnMouseClicked(mouseEvent -> {
                    leJeu.uneCarteDuJoueurEstJouee(v.getCarteTransport());
                });

                lesCartesDuJoueur.getChildren().add(v);
            }

            //Partie BOX
            if (nbPionsWagons.textProperty().isBound()){
                nbPionsWagons.textProperty().unbind();
                nbPionsBateaux.textProperty().unbind();
                nbPorts.textProperty().unbind();
            }
            nbPionsWagons.textProperty().bind(newJ.nbPionsWagonsProperty().asString());
            nbPionsBateaux.textProperty().bind(newJ.nbPionsBateauxProperty().asString());
            nbPorts.textProperty().bind(newJ.nbPionsPortProperty().asString());

            FlowPane jcourantDestination = new FlowPane(); jcourantDestination.setHgap(20); jcourantDestination.setVgap(20); jcourantDestination.setPadding(new Insets(20, 20 ,20 ,20));
            for (IDestination d : ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get().getDestinations() ){
                HBox b = new HBox();
                Image bgroundo = new Image("destination.jpg");
                BackgroundImage background = new BackgroundImage(bgroundo,
                        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                        BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
                Background backgroundImage = new Background(background);
                b.setBackground(backgroundImage);
                BorderPane v = new BorderPane();
                BorderPane h1 = new BorderPane();
                BorderPane h2 = new BorderPane();
                v.setTop(h1);
                v.setBottom(h2);
                Label valeur = new Label();
                Label ville1 = new Label(d.getVilles().get(0));
                Label ville2 = new Label(d.getVilles().get(d.getVilles().size() - 1));
                h1.setLeft(ville1);
                h1.setRight(valeur);
                h2.setRight(ville2);
                v.setPrefSize(100, 50);
                valeur.setStyle("-fx-font-size: 25px");
                b.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
                if (d.getVilles().size() <= 2) {
                    valeur.setText(String.valueOf(d.getValeur()));
                } else {
                    valeur.setText(String.valueOf(d.getValeurMax()));
                }
                b.getChildren().add(v);
                jcourantDestination.getChildren().add(b);
            }
            this.getDest().hoverProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        centre.getChildren().clear();
                        centre.getChildren().addAll(nomJoueur,jcourantDestination);
                    } else {
                        centre.getChildren().clear();
                        centre.getChildren().addAll(nomJoueur,lesCartesDuJoueur);
                    }
                }
            });





        };
        leJeu.joueurCourantProperty().addListener(joueurCourantListener); // cf. la fin du readme
    }

    public Label getDest() {
        return dest;
    }

    public FlowPane getLesCartesDuJoueur() {
        return lesCartesDuJoueur;
    }

    public Label getNomJoueur() {
        return nomJoueur;
    }
}
