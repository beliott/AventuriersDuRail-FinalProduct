package fr.umontpellier.iut.rails;

import java.util.List;

public interface IDestination {
    String getNom();
    int getValeur();
    int getValeurMax();
    List<String> getVilles();

}