package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Pane {

    private final ICarteTransport carteTransport;
    private final ImageView imgCarte;

    public VueCarteTransport(ICarteTransport carteTransport) {
        this.carteTransport = carteTransport;

        String lienCarte = getlinkImage(carteTransport);
        imgCarte = new ImageView(lienCarte);
        imgCarte.setId(carteTransport.getNom());
        imgCarte.setPreserveRatio(true); // on preserve ses proportions
        imgCarte.setFitWidth(120);

        this.getChildren().add(imgCarte);

    }
    public ICarteTransport getCarteTransport() {
        return carteTransport;
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

    public ImageView getImgCarte() {
        return imgCarte;
    }
}
