package com.emsi.subtracker.services;

import com.emsi.subtracker.models.Abonnement;

import java.util.List;

/**
 * Service Métier - Intermédiaire entre la vue et la couche de données.
 * Contient la logique de calcul et de validation.
 */
public class SubscriptionService {

    private final com.emsi.subtracker.dao.SubscriptionDAO subscriptionDAO;

    public SubscriptionService() {
        this.subscriptionDAO = new com.emsi.subtracker.dao.impl.SubscriptionDAOImpl();
    }

    /**
     * Récupère tous les abonnements.
     */
    /**
     * Récupère tous les abonnements de l'utilisateur connecté.
     */
    public List<Abonnement> getAll() {
        com.emsi.subtracker.models.User user = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (user == null) {
            System.err.println("Aucun utilisateur connecté !");
            return List.of();
        }
        return subscriptionDAO.findAll(user.getId());
    }

    /**
     * Ajoute un abonnement pour l'utilisateur connecté.
     */
    public void add(Abonnement abonnement) {
        com.emsi.subtracker.models.User user = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (user != null) {
            abonnement.setUserId(user.getId());
        }
        subscriptionDAO.save(abonnement);
    }

    public void update(Abonnement abonnement) {
        subscriptionDAO.update(abonnement);
    }

    /**
     * Supprime un abonnement par son ID.
     */
    /**
     * Supprime tous les abonnements de l'utilisateur connecté.
     */
    public void removeAll() {
        com.emsi.subtracker.models.User user = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (user != null) {
            subscriptionDAO.deleteAll(user.getId());
        }
    }

    /**
     * Supprime un abonnement par son ID.
     */
    public void remove(int id) {
        // TODO: Ensure user owns this subscription before deleting
        boolean removed = subscriptionDAO.delete(id);
        if (removed) {
            System.out.println("Abonnement ID " + id + " supprimé.");
        } else {
            System.err.println("Aucun abonnement trouvé avec l'ID " + id);
        }
    }

    /**
     * Calcule le coût mensuel total de tous les abonnements de l'utilisateur.
     * Pour un abonnement annuel, le prix est divisé par 12.
     * Arrondi à 2 chiffres après la virgule.
     */
    public double calculerTotalMensuel() {
        return getAll().stream()
                .mapToDouble(a -> {
                    if ("Annuel".equalsIgnoreCase(a.getFrequence())) {
                        return a.getPrix() / 12.0;
                    }
                    return a.getPrix();
                })
                .sum();
    }

    /**
     * Importe les abonnements depuis le fichier CSV data_abonnements.csv
     * et les sauvegarde en base de données pour l'utilisateur courant.
     */
    public void importFromCsv() {
        CsvService csvService = new CsvService();
        List<Abonnement> csvSubscriptions = csvService.chargerTout();

        com.emsi.subtracker.models.User user = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (user == null) {
            System.err.println("Impossible d'importer : aucun utilisateur connecté.");
            return;
        }

        int count = 0;
        for (Abonnement sub : csvSubscriptions) {
            sub.setUserId(user.getId());
            // On laisse la DAO générer un nouvel ID auto-incrémenté pour éviter les
            // conflits
            subscriptionDAO.save(sub);
            count++;
        }
        System.out.println("Importation terminée : " + count + " abonnements ajoutés depuis le CSV.");
    }
}
