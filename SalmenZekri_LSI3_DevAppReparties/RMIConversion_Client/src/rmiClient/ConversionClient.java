package rmiClient;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import rmiService.IConversion;

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
 * Client RMI : ConversionClient
 * ----------------------------
 * Cette application cliente consomme le service distant hébergé sur le serveur.
 * 
 * Principes académiques fondamentaux :
 * 1. Découplage fort (Interface Segregation) :
 *    - Le client ne dispose PAS du code source ni du bytecode de la classe
 *      d'implémentation (ConversionImpl).
 *    - Le client référence uniquement l'interface distante 'IConversion' fournie
 *      via l'archive 'conversionClient.jar'.
 * 
 * 2. Mécanisme de Naming Lookup :
 *    - Naming.lookup("rmi://<host>:<port>/<name>") interroge l'annuaire RMIRegistry.
 *    - L'annuaire renvoie une référence distante : une instance de Stub (Proxy dynamique).
 *    - Le client transtype (cast) cette référence vers l'interface 'IConversion'.
 * 
 * 3. Invocation distante transparente (RPC / RMI) :
 *    - L'appel stub.convertirMontant(500) ressemble syntaxiquement à un appel
 *      local en Java, mais le Stub intercepte l'appel, sérialise les paramètres
 *      (marshalling), les achemine via une connexion socket TCP vers la JVM distante,
 *      attend le résultat, le désérialise (unmarshalling) et le retourne.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class ConversionClient {

    /**
     * URL du service distant publié dans l'annuaire RMIRegistry.
     */
    private static final String SERVICE_URL = "rmi://localhost:1099/ConversionObject";

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("     CLIENT RMI - TEST DU SERVICE DE CONVERSION DE DEVISE   ");
        System.out.println("============================================================");

        try {
            // -------------------------------------------------------------
            // Étape (ii) : Chercher la référence distante dans le registre RMI
            // -------------------------------------------------------------
            System.out.println("[Client] Résolution de nom (lookup) auprès du registre RMI...");
            System.out.println("         URL ciblée : " + SERVICE_URL);

            IConversion stubConversion = (IConversion) Naming.lookup(SERVICE_URL);

            System.out.println("[Client] Référence distante obtenue avec succès !");
            System.out.println("         Type du Stub reçu : " + stubConversion.getClass().getName());

            // -------------------------------------------------------------
            // Étape (iii) : Invoquer la méthode distante convertirMontant(500)
            // -------------------------------------------------------------
            double montantAConvertir = 500.0;
            System.out.println("\n[Client] Invocation distante de convertirMontant(" + montantAConvertir + " EUR)...");

            double resultatEnDinars = stubConversion.convertirMontant(montantAConvertir);

            System.out.println("------------------------------------------------------------");
            System.out.println("  >>> RÉSULTAT DE LA CONVERSION : ");
            System.out.printf("  >>> %.2f EUR = %.2f TND%n", montantAConvertir, resultatEnDinars);
            System.out.println("------------------------------------------------------------");

            // Test additionnel avec d'autres montants pour validation approfondie
            double autreMontant = 120.50;
            double autreResultat = stubConversion.convertirMontant(autreMontant);
            System.out.printf("  >>> Validation complémentaire : %.2f EUR = %.2f TND%n", autreMontant, autreResultat);

            System.out.println("============================================================");
            System.out.println("Invocation distante terminée avec succès.");
            System.out.println("============================================================");

        } catch (NotBoundException e) {
            System.err.println("[ERREUR] Le nom '" + SERVICE_URL + "' n'est pas lié dans le registre RMI.");
            System.err.println("         Assurez-vous que le serveur ConversionServer est bien démarré.");
        } catch (MalformedURLException e) {
            System.err.println("[ERREUR] L'URL RMI fournie est invalide : " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[ERREUR COMMUNICATION RMI] Échec de la communication avec le serveur :");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERREUR INATTENDUE] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
