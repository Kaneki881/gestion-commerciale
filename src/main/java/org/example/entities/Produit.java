package org.example.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@NamedQueries({
        @NamedQuery(
                name = "Produit.findByCategorie",
                query = "SELECT p FROM Produit p " +
                        "WHERE p.categorie.id = :catId ORDER BY p.id"
        ),
        @NamedQuery(
                name = "Produit.searchByLibelle",
                query = "SELECT p FROM Produit p " +
                        "WHERE LOWER(p.libelle) LIKE :motif ORDER BY p.id"
        )
})
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "produit")
    private List<CommandeProduit> lignes = new ArrayList<>();

    protected Produit() {
    }

    public Produit(
            String libelle,
            BigDecimal prix,
            Categorie categorie
    ) {
        this.libelle = libelle;
        this.prix = prix;
        this.categorie = categorie;
        this.dateCreation = LocalDate.now();
    }

    public Long getId() { return id; }
    public String getLibelle() { return libelle; }
    public BigDecimal getPrix() { return prix; }
    public LocalDate getDateCreation() { return dateCreation; }
    public Categorie getCategorie() { return categorie; }
    public List<CommandeProduit> getLignes() { return lignes; }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}