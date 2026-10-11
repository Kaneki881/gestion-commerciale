package org.example.service;

import org.example.entities.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class CommerceService {

    private final EntityManagerFactory emf;

    public CommerceService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Long creerProduit(
            String libelle,
            BigDecimal prix,
            Long categorieId
    ) {
        if (libelle == null
                || libelle.trim().isEmpty()
                || prix == null
                || prix.signum() < 0) {
            throw new IllegalArgumentException(
                    "Produit invalide"
            );
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Categorie categorie =
                    em.getReference(
                            Categorie.class, categorieId
                    );

            Produit produit =
                    new Produit(
                            libelle, prix, categorie
                    );

            em.persist(produit);
            tx.commit();
            return produit.getId();

        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }

    public Long creerCommande(
            LocalDate date,
            Map<Long, Integer> quantites
    ) {
        if (date == null
                || quantites == null
                || quantites.isEmpty()) {
            throw new IllegalArgumentException(
                    "Commande invalide"
            );
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Commande commande =
                    new Commande(date);

            for (Map.Entry<Long, Integer> entree
                    : quantites.entrySet()) {
                Produit produit =
                        em.getReference(
                                Produit.class,
                                entree.getKey()
                        );

                commande.addProduit(
                        produit,
                        entree.getValue()
                );
            }

            em.persist(commande);
            tx.commit();
            return commande.getId();

        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }
}