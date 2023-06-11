package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.mecanique.Jeu;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 *
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    int indice = 1; // PERSO
    int nombreDeJoueurs = 1; // PERSO
    List<String> nomDesJoueurs; // PERSO

    private final ObservableList<String> nomsJoueurs;
    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    private IntegerProperty nbJoueurs;

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        VBox root = new VBox();
        Scene scene = new Scene(root);
        this.setScene(scene);

        /* --------------------------------------------------------------- */

        /* LE NOMBRE DE JOUEURS EST DE 2 A 5 JOUEURS SELON
           https://www.espritjeu.com/jeux-de-strategie/les-aventuriers-du-rail-autour-du-monde.html
           CE SONT LES VALEURS QUE NOUS ALLONS PRENDRE EN COMPTE
         */
        indice = 1;
        nombreDeJoueurs = 1;
        nomDesJoueurs = new ArrayList<>();;


        // Création des labels
        Label titre = new Label("Bienvenue dans Les Aventuriers du Rail !");
        titre.setFont(Font.font("Arial", 30));
        titre.setTextFill(Color.WHITE);
        HBox titrebox = new HBox();
        titrebox.getChildren().add(titre);
        titrebox.setAlignment(Pos.CENTER);

        Label choixAFaire = new Label("Veuillez saisir le nombre de joueurs dans la partie");
        choixAFaire.setFont(Font.font("Arial", 18));
        choixAFaire.setTextFill(Color.WHITE);

        // Création du champ de saisie du nombre de joueurs
        TextField champSaisie= new TextField();
        champSaisie.setPrefWidth(50);

        // Création du bouton pour valider le nombre de joueurs
        AtomicBoolean nbJoueurChoisis = new AtomicBoolean(false);
        Button confirmer = new Button("Valider");
        confirmer.setOnAction(e ->{
            if (nbJoueurChoisis.get() == false){  // nombre de joueur choisis
                if (champSaisie.getText().isEmpty()){
                    return;
                }
                else if (!champSaisie.getText().chars().allMatch(Character::isDigit)){
                    champSaisie.clear();
                    return;
                }
                nombreDeJoueurs = Integer.parseInt(champSaisie.textProperty().getValue());
                if (nombreDeJoueurs <= 1 || nombreDeJoueurs > 5)
                    return;
                nbJoueurChoisis.set(true);
                indice = 1;
                choixAFaire.textProperty().setValue("Veuillez saisir le nom du joueur " +"j"+ indice);
                champSaisie.clear();
            }
            else if (indice <= nombreDeJoueurs){ // quand nb joueurs définis

                if (champSaisie.textProperty().getValue().isEmpty()){
                    return;
                }
                else if (!this.nomDesJoueurs.isEmpty() && nomExisteDeja(champSaisie.textProperty().getValue())){
                    champSaisie.setStyle("-fx-border-color: red;");
                    champSaisie.setText("");
                    champSaisie.setPromptText("Nom déjà utilisé");
                    return;
                }

                champSaisie.setStyle("");
                champSaisie.setPromptText("");


                if (indice != nombreDeJoueurs + 1){
                    nomDesJoueurs.add(champSaisie.textProperty().getValue());
                }
                champSaisie.clear();


                if (indice != nombreDeJoueurs + 1){
                    indice++;
                    if (indice != nombreDeJoueurs + 1)
                        choixAFaire.textProperty().setValue("Veuillez saisir le nom du joueur " +"j"+ indice);
                }

            }
        });

        // Création de la VBox pour le nombre de joueurs
        VBox boxJoueur = new VBox(choixAFaire, champSaisie, confirmer);
        boxJoueur.setAlignment(Pos.CENTER);
        boxJoueur.setSpacing(10);

        Button boutonCommencer = new Button("Commencer la partie");

        boutonCommencer.setOnMouseClicked(mouseEvent -> {
            if (indice == nombreDeJoueurs + 1){
                nombreDeJoueurs = nomDesJoueurs.size(); // au cas ou
                nomsJoueurs.addAll(nomDesJoueurs);
                this.hide();
            }

        });

        VBox debutGame = new VBox(titrebox, boxJoueur,boutonCommencer);
        debutGame.setSpacing(20);
        debutGame.setPadding(new Insets(20));
        debutGame.getStyleClass().add("tooltip");
        root.getChildren().add(debutGame);


    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueursProperty().addListener(quandLesNomsDesJoueursSontDefinis);

    } // TODO : a modifier

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {

    } // TODO : a modifier

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return ((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs().size();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return nomsJoueurs.get(playerNumber - 1);
    } //TODO : verif si indice marche bien

    private boolean nomExisteDeja(String s) {
        for (String a: nomDesJoueurs) {
            if (a.equals(s)){
                return true;
            }
        }
        return false;
    }
}
