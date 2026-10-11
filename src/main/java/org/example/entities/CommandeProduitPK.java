package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandeProduitPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "commande_id")
    private Long commandeId;

    @Column(name = "produit_id")
    private Long produitId;

    public CommandeProduitPK() {
    }

    public Long getCommandeId() { return commandeId; }
    public Long getProduitId() { return produitId; }

    @Override
    public boolean equals(Object autre) {
        if (this == autre) return true;
        if (!(autre instanceof CommandeProduitPK)) return false;

        CommandeProduitPK pk =
                (CommandeProduitPK) autre;

        return Objects.equals(commandeId, pk.commandeId)
                && Objects.equals(produitId, pk.produitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, produitId);
    }
}