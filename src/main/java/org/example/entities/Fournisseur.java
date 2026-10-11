// Fournisseur.java
package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "fournisseurs")
public class Fournisseur extends User {

    @Column(name = "adresse")
    private String adresse;

    protected Fournisseur() {
    }

    public Fournisseur(String nom, String adresse) {
        super(nom);
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }
}