package rmiServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import rmiService.ConversionImpl;

/**
 * ============================================================================
 * Université de Sfax - Faculté des Sciences de Sfax (FSS)
 * Département d'Informatique et des Communications
 * Matière : Développement d'Applications Réparties (LSI 3)
 * Enseignantes : Amal ABID & Zeineb DAOUD
 * Étudiant : Salmen Zekri
 * ============================================================================
 * 
 * Activité 5-1 : Service de conversion de devises
 * 
 * Serveur RMI : ConversionServer
 * ------------------------------
 * Cette application serveur orchestre le déploiement du service distant :
 * 
 * Étapes canoniques d'un serveur RMI :
 * 1. Activation de l'annuaire RMIRegistry :
 *    - Le registre RMI est un service d'annuaire (Bootstrap Naming Service)
 *      fonctionnant par défaut sur le port TCP 1099.
 *    - LocateRegistry.createRegistry(1099) démarre l'annuaire au sein de la JVM,
 *      évitant le lancement manuel de l'outil externe 'rmiregistry'.
 * 
 * 2. Instanciation du servant :
 *    - Création de l'objet métier ConversionImpl. Son instanciation déclenche
 *      son exportation sur un socket TCP anonyme.
 * 
 * 3. Publication (Binding) dans l'annuaire :
 *    - Naming.rebind() associe une URL RMI ("rmi://localhost:1099/ConversionObject")
 *      au stub de l'objet distant.
 *    - Important : L'annuaire ne stocke JAMAIS l'objet d'implémentation lui-même,
 *      mais uniquement sa référence distante (le Stub/Proxy), permettant aux
 *      clients de localiser le serveur et de communiquer avec lui via JRMP.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class ConversionServer {

    /**
     * Port d'écoute standard pour le service de registre RMI.
     */
    public static final int RMI_PORT = 1099;

    /**
     * Nom logique d'enregistrement du service de conversion dans l'annuaire.
     */
    public static final String SERVICE_NAME = "ConversionObject";

    public static void main(String[] args) {
        try {
            System.out.println("============================================================");
            System.out.println("  DÉMARRAGE DU SERVEUR RMI - SERVICE DE CONVERSION DEVISE   ");
            System.out.println("============================================================");

            // -------------------------------------------------------------
            // Étape (i) : Activer l'annuaire RMIRegistry
            // -------------------------------------------------------------
            System.out.println("[1/3] Activation du registre RMI sur le port " + RMI_PORT + "...");
            try {
                LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("      -> Registre RMI démarré avec succès sur le port " + RMI_PORT + ".");
            } catch (Exception e) {
                System.out.println("      -> Registre déjà actif sur le port " + RMI_PORT + ".");
            }

            // -------------------------------------------------------------
            // Étape (ii) : Créer une instance de la classe ConversionImpl
            // -------------------------------------------------------------
            System.out.println("[2/3] Instanciation et exportation de l'objet distant ConversionImpl...");
            ConversionImpl conversionService = new ConversionImpl();
            System.out.println("      -> Instance créée avec succès.");

            // -------------------------------------------------------------
            // Étape (iii) : Publier la référence dans l'annuaire RMIRegistry
            // -------------------------------------------------------------
            String rmiUrl = "rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME;
            System.out.println("[3/3] Publication de la référence sous l'URL : " + rmiUrl + "...");
            Naming.rebind(rmiUrl, conversionService);
            System.out.println("      -> Référence publiée (rebind) avec succès !");

            System.out.println("------------------------------------------------------------");
            System.out.println("Serveur RMI opérationnel. En attente des requêtes des clients...");
            System.out.println("============================================================");

        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE] Échec du démarrage du serveur RMI :");
            e.printStackTrace();
        }
    }
}
