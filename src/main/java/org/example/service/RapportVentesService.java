package org.example.service;

import org.example.entities.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RapportVentesService {

    private final EntityManagerFactory emf;

    public RapportVentesService(
            EntityManagerFactory emf
    ) {
        this.emf = emf;
    }

    public Map<String, BigDecimal>
    chiffreAffairesMoisPrecedent(
            LocalDate dateReference
    ) {
        LocalDate debut =
                dateReference.withDayOfMonth(1)
                        .minusMonths(1);
        LocalDate finExclusive =
                dateReference.withDayOfMonth(1);

        EntityManager em =
                emf.createEntityManager();

        try {
            CriteriaBuilder cb =
                    em.getCriteriaBuilder();
            CriteriaQuery<Tuple> query =
                    cb.createTupleQuery();

            Root<Categorie> categorie =
                    query.from(Categorie.class);

            Join<Categorie, Produit> produit =
                    categorie.join(
                            "produits", JoinType.LEFT
                    );

            Join<Produit, CommandeProduit> ligne =
                    produit.join(
                            "lignes", JoinType.LEFT
                    );

            Join<CommandeProduit, Commande> commande =
                    ligne.join(
                            "commande", JoinType.LEFT
                    );

            Expression<BigDecimal> montant =
                    cb.toBigDecimal(
                            cb.prod(
                                    produit
                                            .<BigDecimal>get("prix"),
                                    ligne
                                            .<Integer>get("quantite")
                            )
                    );

            Predicate dansLeMois =
                    cb.and(
                            cb.greaterThanOrEqualTo(
                                    commande
                                            .<LocalDate>get(
                                                    "dateCommande"
                                            ),
                                    debut
                            ),
                            cb.lessThan(
                                    commande
                                            .<LocalDate>get(
                                                    "dateCommande"
                                            ),
                                    finExclusive
                            )
                    );

            Expression<BigDecimal> montantRetenu =
                    cb.<BigDecimal>selectCase()
                            .when(dansLeMois, montant)
                            .otherwise(BigDecimal.ZERO);

            query.multiselect(
                            categorie.get("nom")
                                    .alias("categorie"),
                            cb.sum(montantRetenu)
                                    .alias("total")
                    )
                    .groupBy(
                            categorie.get("id"),
                            categorie.get("nom")
                    )
                    .orderBy(
                            cb.asc(categorie.get("nom"))
                    );

            List<Tuple> lignes =
                    em.createQuery(query)
                            .getResultList();

            Map<String, BigDecimal> resultat =
                    new LinkedHashMap<>();

            for (Tuple ligneResultat : lignes) {
                resultat.put(
                        ligneResultat.get(
                                "categorie", String.class
                        ),
                        ligneResultat.get(
                                "total", BigDecimal.class
                        )
                );
            }

            return resultat;
        } finally {
            em.close();
        }
    }
}