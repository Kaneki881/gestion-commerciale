package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    // Constructeurs
    public Categorie() {}
    public Categorie(String nom) { this.nom = nom; }

    // Getters & Setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() { return "Categorie{id=" + id + ", nom='" + nom + "'}"; }
}