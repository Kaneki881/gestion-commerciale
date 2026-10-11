package org.example;

import org.example.dao.ProduitRepository;
import org.example.entities.Categorie;
import org.example.service.*;
import org.example.util.HibernateUtil;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf =
                HibernateUtil.getSessionFactory();

        try {
            CrudService<Categorie, Long> categories =
                    new CrudService<>(
                            Categorie.class, emf
                    );

            CommerceService commerce =
                    new CommerceService(emf);

            ProduitRepository produits =
                    new ProduitRepository(emf);

            Categorie categorie =
                    new Categorie("Électronique");
            categories.create(categorie);

            Long clavierId =
                    commerce.creerProduit(
                            "Clavier",
                            new BigDecimal("49.99"),
                            categorie.getId()
                    );

            Long sourisId =
                    commerce.creerProduit(
                            "Souris",
                            new BigDecimal("19.90"),
                            categorie.getId()
                    );

            Map<Long, Integer> quantites =
                    new LinkedHashMap<>();
            quantites.put(clavierId, 3);
            quantites.put(sourisId, 5);

            LocalDate dateCommande =
                    LocalDate.now().minusMonths(1);

            Long commandeId =
                    commerce.creerCommande(
                            dateCommande, quantites
                    );

            System.out.println(
                    "Commande créée : " + commandeId
            );

            System.out.println(
                    "Catégorie du clavier : "
                            + produits
                            .findWithCategorie(clavierId)
                            .getCategorie()
                            .getNom()
            );

            System.out.println(
                    new RapportVentesService(emf)
                            .chiffreAffairesMoisPrecedent(
                                    LocalDate.now()
                            )
            );
        } finally {
            HibernateUtil.shutdown();
        }
    }
}