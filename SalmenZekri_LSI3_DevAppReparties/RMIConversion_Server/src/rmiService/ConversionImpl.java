package rmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
 * Implémentation du service distant (Servant) : ConversionImpl
 * -----------------------------------------------------------
 * Cette classe fournit le code métier effectif exécuté sur la JVM du serveur.
 * 
 * Principes académiques fondamentaux :
 * 1. Héritage de UnicastRemoteObject :
 *    - UnicastRemoteObject est la classe de base standard de Java RMI pour les
 *      objets distants point-à-point (unicast).
 *    - Lors de l'instanciation, le constructeur de UnicastRemoteObject effectue
 *      l'« exportation » de l'objet (via UnicastRemoteObject.exportObject).
 *    - Cette exportation ouvre un socket serveur sur un port TCP anonyme
 *      (ou spécifié) afin d'écouter les requêtes distantes au format JRMP
 *      (Java Remote Method Protocol).
 * 
 * 2. Constructeur et déclaration de RemoteException :
 *    - Le constructeur par défaut doit obligatoirement propager RemoteException,
 *      car l'appel implicite à super() tente d'initialiser les ressources réseau
 *      et d'exporter l'objet distant.
 * 
 * 3. Thread-Safety :
 *    - La JVM du serveur RMI distribue chaque requête entrante sur un thread
 *      dédié issu d'un pool d'exécution. Les méthodes de l'objet distant
 *      doivent donc être conçues pour supporter les accès concurrents.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class ConversionImpl extends UnicastRemoteObject implements IConversion {

    private static final long serialVersionUID = 1L;

    /**
     * Taux de change fixe appliqué pour la conversion :
     * 1 Euro (€) = 3.35 Dinars Tunisiens (TND).
     */
    private static final double TAUX_CONVERSION = 3.35;

    /**
     * Constructeur par défaut.
     * Déclenche l'exportation de l'objet distant sur le protocole RMI/JRMP.
     * 
     * @throws RemoteException Si l'exportation réseau de l'objet distant échoue.
     */
    public ConversionImpl() throws RemoteException {
        super();
        System.out.println("[Serveur - ConversionImpl] Instance du service de conversion initialisée et exportée.");
    }

    /**
     * Implémentation de la méthode distante de conversion de devises.
     * Convertit un montant en Euros vers le Dinar Tunisien.
     * 
     * @param mt Montant en Euros.
     * @return Montant converti en Dinars Tunisiens (TND).
     * @throws RemoteException En cas d'anomalie réseau lors de la communication.
     */
    @Override
    public double convertirMontant(double mt) throws RemoteException {
        double resultat = mt * TAUX_CONVERSION;
        System.out.printf("[Serveur - RMI] Requête distante traitée : %.2f EUR -> %.2f TND (Taux : %.2f)%n",
                mt, resultat, TAUX_CONVERSION);
        return resultat;
    }
}
