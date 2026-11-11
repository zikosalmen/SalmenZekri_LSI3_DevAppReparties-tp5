package rmiClient;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.util.Date;
import metier.Compte;
import rmiService.IBanque;

/**
 * ============================================================================
 * Université de Sfax - Faculté des Sciences de Sfax (FSS)
 * Département d'Informatique et des Communications
 * Matière : Développement d'Applications Réparties (LSI 3)
 * Enseignantes : Amal ABID & Zeineb DAOUD
 * Étudiant : Salmen Zekri
 * ============================================================================
 * 
 * Activité 5-2 : Service de banque en utilisant RMI et JNDI
 * 
 * Client RMI Standard : BanqueClient (Étape 3 & Étape 4)
 * -----------------------------------------------------
 * Consomme les services bancaires distants via le registre RMI classique (Naming.lookup).
 * 
 * Démonstrations et Scénarios validés :
 * 1. Sérialisation d'objets métier : instanciation de 'Compte' côté client,
 *    sérialisation (marshalling) et transmission par valeur vers le serveur.
 * 2. Création de compte réussie (créerCompte).
 * 3. Contrôle d'unicité : tentative de création d'un compte avec un code déjà existant.
 * 4. Consultation d'un compte existant (getInfoCompte).
 * 5. Consultation d'un compte inexistant (gestion des cas limites).
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class BanqueClient {

    private static final String SERVICE_URL = "rmi://localhost:1099/BanqueObject";

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("      CLIENT RMI BANQUE - TESTS DES SERVICES DISTANTS       ");
        System.out.println("============================================================");

        try {
            // 1. Résolution de nom dans l'annuaire RMI
            System.out.println("[Client] Résolution du service distant dans l'annuaire RMI...");
            System.out.println("         URL : " + SERVICE_URL);
            IBanque banque = (IBanque) Naming.lookup(SERVICE_URL);
            System.out.println("         Stub IBanque obtenu : " + banque.getClass().getName());
            System.out.println("------------------------------------------------------------");

            // -------------------------------------------------------------
            // SCÉNARIO 1 : Création d'un premier compte bancaire (Code 1)
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 1] Création du compte bancaire N° 1 (Solde : 1500.00 TND)...");
            Compte c1 = new Compte(1, 1500.00, new Date());
            String reponse1 = banque.creerCompte(c1);
            System.out.println(">>> Réponse Serveur : " + reponse1);

            // -------------------------------------------------------------
            // SCÉNARIO 2 : Tentative de création d'un doublon (Même code 1)
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 2] Tentative d'insertion d'un compte avec le même code N° 1 (Doublon)...");
            Compte c1Doublon = new Compte(1, 999.00, new Date());
            String reponseDoublon = banque.creerCompte(c1Doublon);
            System.out.println(">>> Réponse Serveur : " + reponseDoublon);

            // -------------------------------------------------------------
            // SCÉNARIO 3 : Création d'un deuxième compte (Code 2)
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 3] Création d'un second compte N° 2 (Solde : 3450.50 TND)...");
            Compte c2 = new Compte(2, 3450.50, new Date());
            String reponse2 = banque.creerCompte(c2);
            System.out.println(">>> Réponse Serveur : " + reponse2);

            // -------------------------------------------------------------
            // SCÉNARIO 4 : Consultation d'un compte existant (Code 1)
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 4] Consultation du compte existant N° 1...");
            String infoC1 = banque.getInfoCompte(1);
            System.out.println(">>> Réponse Serveur : " + infoC1);

            // -------------------------------------------------------------
            // SCÉNARIO 5 : Consultation du compte N° 2
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 5] Consultation du compte existant N° 2...");
            String infoC2 = banque.getInfoCompte(2);
            System.out.println(">>> Réponse Serveur : " + infoC2);

            // -------------------------------------------------------------
            // SCÉNARIO 6 : Consultation d'un compte inexistant (Code 99)
            // -------------------------------------------------------------
            System.out.println("\n[SCÉNARIO 6] Consultation d'un compte inexistant (Code 99)...");
            String infoInexistant = banque.getInfoCompte(99);
            System.out.println(">>> Réponse Serveur : " + infoInexistant);

            System.out.println("\n============================================================");
            System.out.println("Tous les scénarios de test RMI ont été exécutés avec succès !");
            System.out.println("============================================================");

        } catch (NotBoundException e) {
            System.err.println("[ERREUR] Le nom '" + SERVICE_URL + "' n'est pas enregistré dans RMIRegistry.");
            System.err.println("         Veuillez démarrer BanqueServer au préalable.");
        } catch (MalformedURLException e) {
            System.err.println("[ERREUR] URL RMI invalide : " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[ERREUR COMMUNICATION RMI] " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERREUR INATTENDUE] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
