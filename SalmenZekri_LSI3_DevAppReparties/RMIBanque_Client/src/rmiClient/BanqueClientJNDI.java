package rmiClient;

import java.rmi.RemoteException;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
 * Client RMI avec recherche JNDI : BanqueClientJNDI (Étape 5)
 * -----------------------------------------------------------
 * Effectue la recherche (lookup) de l'objet distant IBanque via l'annuaire JNDI
 * au lieu d'utiliser l'API Naming.lookup().
 * 
 * Principes académiques fondamentaux :
 * 1. Découplage de la localisation de service :
 *    - Le code client utilise l'interface standard javax.naming.Context.
 *    - L'adresse du serveur d'annuaire et la fabrique de contexte (ContextFactory)
 *      sont externalisées dans le fichier 'jndi.properties'.
 *    - Si le système migre d'un annuaire RMI Registry vers LDAP ou un annuaire CORBA,
 *      le code source de l'application reste inchangé (seul jndi.properties change).
 * 
 * 2. Mécanisme de Lookup JNDI :
 *    - ctx.lookup("BanqueObject") résout le nom symbolique via le provider RMI
 *      spécifié par java.naming.provider.url (rmi://localhost:1099).
 *    - Le stub renvoyé est ensuite transtypé en l'interface distante 'IBanque'.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class BanqueClientJNDI {

    public static final String JNDI_SERVICE_NAME = "BanqueObject";

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("    CLIENT RMI BANQUE - RECHERCHE D'OBJET DISTANT VIA JNDI  ");
        System.out.println("============================================================");

        try {
            // 1. Initialisation du contexte JNDI
            System.out.println("[Client JNDI] Initialisation de InitialContext()...");
            Context ctx = new InitialContext();

            // 2. Recherche (lookup) dans l'annuaire JNDI
            System.out.println("[Client JNDI] Recherche (lookup) du service : '" + JNDI_SERVICE_NAME + "'...");
            IBanque banque = (IBanque) ctx.lookup(JNDI_SERVICE_NAME);

            System.out.println("              -> Stub distant obtenu via JNDI : " + banque.getClass().getName());
            System.out.println("------------------------------------------------------------");

            // -------------------------------------------------------------
            // TEST JNDI 1 : Création d'un compte
            // -------------------------------------------------------------
            System.out.println("\n[TEST JNDI 1] Création du compte N° 100 via le Stub résolu par JNDI...");
            Compte c100 = new Compte(100, 2800.00, new Date());
            String res1 = banque.creerCompte(c100);
            System.out.println(">>> Réponse Serveur : " + res1);

            // -------------------------------------------------------------
            // TEST JNDI 2 : Tentative d'insertion du même compte (Unicité)
            // -------------------------------------------------------------
            System.out.println("\n[TEST JNDI 2] Test d'unicité avec le même code N° 100...");
            Compte c100Bis = new Compte(100, 5000.00, new Date());
            String resDoublon = banque.creerCompte(c100Bis);
            System.out.println(">>> Réponse Serveur : " + resDoublon);

            // -------------------------------------------------------------
            // TEST JNDI 3 : Consultation du compte créé
            // -------------------------------------------------------------
            System.out.println("\n[TEST JNDI 3] Consultation du compte N° 100...");
            String info100 = banque.getInfoCompte(100);
            System.out.println(">>> Réponse Serveur : " + info100);

            // -------------------------------------------------------------
            // TEST JNDI 4 : Consultation d'un compte inexistant
            // -------------------------------------------------------------
            System.out.println("\n[TEST JNDI 4] Consultation d'un compte inexistant (Code 404)...");
            String info404 = banque.getInfoCompte(404);
            System.out.println(">>> Réponse Serveur : " + info404);

            System.out.println("\n============================================================");
            System.out.println("Tests JNDI exécutés avec un succès total !");
            System.out.println("============================================================");

        } catch (NamingException e) {
            System.err.println("[ERREUR JNDI] Impossible de résoudre le nom '" + JNDI_SERVICE_NAME + "' :");
            System.err.println("              " + e.getMessage());
            System.err.println("              Vérifiez que le serveur BanqueServer (ou BanqueServerJNDI) est bien lancé.");
        } catch (RemoteException e) {
            System.err.println("[ERREUR COMMUNICATION RMI] " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERREUR INATTENDUE] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
