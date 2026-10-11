package org.example;

import org.example.dao.ProduitRepository;
import org.example.entities.*;
import org.example.service.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.annotations.BeforeClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class CommerceTest {

    private static SessionFactory emf;

    @org.testng.annotations.BeforeClass
    public static void init() {
        emf = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    @AfterClass
    public static void fermer() {
        emf.close();
    }

    @Test
    public void commandeRapportEtCategorieChargee() {
        CrudService<Categorie, Long> categories =
                new CrudService<>(
                        Categorie.class, emf
                );

        Categorie categorie =
                new Categorie("Papeterie");
        categories.create(categorie);

        CommerceService commerce =
                new CommerceService(emf);

        Long produitId =
                commerce.creerProduit(
                        "Stylo",
                        new BigDecimal("1.20"),
                        categorie.getId()
                );

        Produit charge =
                new ProduitRepository(emf)
                        .findWithCategorie(produitId);

        assertNotNull(charge);
        assertEquals(
                "Papeterie",
                charge.getCategorie().getNom()
        );

        LocalDate reference =
                LocalDate.of(2026, 10, 11);

        commerce.creerCommande(
                LocalDate.of(2026, 9, 12),
                Collections.singletonMap(
                        produitId, 3
                )
        );

        Map<String, BigDecimal> rapport =
                new RapportVentesService(emf)
                        .chiffreAffairesMoisPrecedent(
                                reference
                        );

        assertEquals(
                0,
                new BigDecimal("3.60")
                        .compareTo(
                                rapport.get("Papeterie")
                        )
        );
    }
}