package org.example.dao;

import org.example.entities.Produit;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProduitRepository {

    private final EntityManagerFactory emf;

    public ProduitRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Produit findWithCategorie(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> resultat = em.createQuery(
                            "SELECT p FROM Produit p " +
                                    "JOIN FETCH p.categorie " +
                                    "WHERE p.id = :id",
                            Produit.class
                    )
                    .setParameter("id", id)
                    .getResultList();

            return resultat.isEmpty()
                    ? null : resultat.get(0);
        } finally {
            em.close();
        }
    }

    public List<Produit> findByCategorie(
            Long categorieId
    ) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery(
                            "Produit.findByCategorie",
                            Produit.class
                    )
                    .setParameter("catId", categorieId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produit> search(
            String terme,
            BigDecimal prixMin,
            BigDecimal prixMax
    ) {
        EntityManager em = emf.createEntityManager();

        try {
            CriteriaBuilder cb =
                    em.getCriteriaBuilder();
            CriteriaQuery<Produit> query =
                    cb.createQuery(Produit.class);
            Root<Produit> produit =
                    query.from(Produit.class);

            List<Predicate> filtres =
                    new ArrayList<>();

            if (terme != null
                    && !terme.trim().isEmpty()) {
                filtres.add(cb.like(
                        cb.lower(
                                produit.<String>get("libelle")
                        ),
                        "%" + terme.trim()
                                .toLowerCase(Locale.ROOT)
                                + "%"
                ));
            }

            if (prixMin != null) {
                filtres.add(
                        cb.greaterThanOrEqualTo(
                                produit.<BigDecimal>get("prix"),
                                prixMin
                        )
                );
            }

            if (prixMax != null) {
                filtres.add(
                        cb.lessThanOrEqualTo(
                                produit.<BigDecimal>get("prix"),
                                prixMax
                        )
                );
            }

            query.select(produit)
                    .where(filtres.toArray(
                            new Predicate[0]
                    ))
                    .orderBy(
                            cb.asc(produit.get("id"))
                    );

            return em.createQuery(query)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Numérotation des pages à partir de 1.
    public List<Produit> findPage(
            int pageNumber,
            int pageSize
    ) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException(
                    "Page et taille doivent être positives"
            );
        }

        long offset =
                (long) (pageNumber - 1) * pageSize;

        if (offset > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Numéro de page trop élevé"
            );
        }

        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p ORDER BY p.id",
                            Produit.class
                    )
                    .setFirstResult((int) offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}