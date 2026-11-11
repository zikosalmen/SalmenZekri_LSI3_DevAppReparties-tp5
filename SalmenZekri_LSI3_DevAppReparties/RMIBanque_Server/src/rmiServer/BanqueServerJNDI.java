package rmiServer;

import java.rmi.registry.LocateRegistry;
import javax.naming.Context;
import javax.naming.InitialContext;
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
 * Serveur RMI avec intégration JNDI : BanqueServerJNDI (Étape 5)
 * -------------------------------------------------------------
 * Utilise Java Naming and Directory Interface (JNDI) pour l'enregistrement (binding)
 * de l'objet distant au lieu de l'API propriétaire LocateRegistry/Naming.
 * 
 * Principes académiques fondamentaux :
 * 1. Abstraction JNDI :
 *    - JNDI fournit une API standard unifiée pour accéder à divers services
 *      de nommage et d'annuaire (RMI Registry, LDAP, DNS, CORBA COS Naming).
 *    - L'application n'est plus couplée à la classe propriétaire java.rmi.Naming.
 * 
 * 2. Configuration externe via jndi.properties :
 *    - Le fichier 'jndi.properties' placé à la racine du Classpath (dossier src)
 *      configure automatiquement le contexte initial :
 *      * java.naming.factory.initial = com.sun.jndi.rmi.registry.RegistryContextFactory
 *      * java.naming.provider.url = rmi://localhost:1099
 * 
 * 3. Enregistrement (Binding) :
 *    - context.rebind("BanqueObject", banqueService) publie le stub du servant
 *      dans le registre RMI ciblé par l'annuaire JNDI.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class BanqueServerJNDI {

    public static final int RMI_PORT = 1099;
    public static final String JNDI_BINDING_NAME = "BanqueObject";

    public static void main(String[] args) {
        try {
            System.out.println("============================================================");
            System.out.println("   DÉMARRAGE DU SERVEUR RMI BANQUE AVEC ENREGISTREMENT JNDI ");
            System.out.println("============================================================");

            // 1. Démarrage préalable de l'annuaire RMI (s'il n'est pas déjà actif)
            System.out.println("[1/3] Vérification/Activation du registre RMI sur le port " + RMI_PORT + "...");
            try {
                LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("      -> Registre RMI local actif.");
            } catch (Exception e) {
                System.out.println("      -> Registre RMI déjà en écoute.");
            }

            // 2. Instanciation du servant distant
            System.out.println("[2/3] Instanciation du service BanqueImpl...");
            BanqueImpl banqueService = new BanqueImpl();

            // 3. Initialisation du contexte JNDI et publication (rebind)
            System.out.println("[3/3] Initialisation du contexte JNDI (via jndi.properties)...");
            Context ctx = new InitialContext();

            System.out.println("      Publication JNDI (rebind) sous le nom : '" + JNDI_BINDING_NAME + "'...");
            ctx.rebind(JNDI_BINDING_NAME, banqueService);
            System.out.println("      -> Objet publié avec succès dans l'annuaire JNDI !");

            System.out.println("------------------------------------------------------------");
            System.out.println("Serveur JNDI opérationnel. En attente d'invocations distantes...");
            System.out.println("============================================================");

        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE JNDI] Échec du déploiement serveur via JNDI :");
            e.printStackTrace();
        }
    }
}
