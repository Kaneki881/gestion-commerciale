package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @OneToMany(mappedBy = "commande",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CommandeProduit> lignes = new ArrayList<>();

    protected Commande() {
    }

    public Commande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Long getId() { return id; }
    public LocalDate getDateCommande() { return dateCommande; }
    public List<CommandeProduit> getLignes() { return lignes; }

    public void addProduit(Produit produit, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être positive"
            );
        }

        for (CommandeProduit ligne : lignes) {
            if (ligne.getProduit().getId()
                    .equals(produit.getId())) {
                throw new IllegalArgumentException(
                        "Produit déjà présent dans la commande"
                );
            }
        }

        lignes.add(
                new CommandeProduit(this, produit, quantite)
        );
    }
}