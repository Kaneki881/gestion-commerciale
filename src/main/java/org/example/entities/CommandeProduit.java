package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "commande_produit")
public class CommandeProduit {

    @EmbeddedId
    private CommandeProduitPK id =
            new CommandeProduitPK();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("produitId")
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private int quantite;

    protected CommandeProduit() {
    }

    public CommandeProduit(
            Commande commande,
            Produit produit,
            int quantite
    ) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
    }

    public CommandeProduitPK getId() { return id; }
    public Commande getCommande() { return commande; }
    public Produit getProduit() { return produit; }
    public int getQuantite() { return quantite; }
}