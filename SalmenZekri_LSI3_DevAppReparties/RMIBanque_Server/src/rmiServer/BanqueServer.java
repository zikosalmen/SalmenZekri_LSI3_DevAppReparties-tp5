package rmiServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import rmiService.BanqueImpl;

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
 * Serveur RMI Standard : BanqueServer (Étape 2 & Étape 4)
 * ------------------------------------------------------
 * Initialise le registre RMI et publie le servant BanqueImpl via l'API Naming.
 * 
 * Cycle de vie du serveur :
 * 1. Activation du registre RMI local (port 1099).
 * 2. Création et exportation de l'instance distante BanqueImpl.
 * 3. Enregistrement (Binding) du stub dans l'annuaire sous le nom 'BanqueObject'.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class BanqueServer {

    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "BanqueObject";

    public static void main(String[] args) {
        try {
            System.out.println("============================================================");
            System.out.println("   DÉMARRAGE DU SERVEUR RMI - GESTION DES COMPTES BANCAIRES ");
            System.out.println("============================================================");

            // 1. Activation du registre RMI
            System.out.println("[1/3] Activation du registre RMI sur le port " + RMI_PORT + "...");
            try {
                LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("      -> Registre RMI local démarré sur le port " + RMI_PORT + ".");
            } catch (Exception e) {
                System.out.println("      -> Registre RMI déjà actif sur le port " + RMI_PORT + ".");
            }

            // 2. Instanciation du servant distant
            System.out.println("[2/3] Instanciation du service métier BanqueImpl...");
            BanqueImpl banqueService = new BanqueImpl();

            // 3. Enregistrement dans l'annuaire via Naming
            String url = "rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME;
            System.out.println("[3/3] Publication de l'objet dans l'annuaire : " + url);
            Naming.rebind(url, banqueService);
            System.out.println("      -> Publication réussie sous le nom : " + SERVICE_NAME);

            System.out.println("------------------------------------------------------------");
            System.out.println("Serveur RMI Bancaire opérationnel. Prêt à recevoir des requêtes.");
            System.out.println("============================================================");

        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE] Échec du démarrage du serveur BanqueServer :");
            e.printStackTrace();
        }
    }
}
