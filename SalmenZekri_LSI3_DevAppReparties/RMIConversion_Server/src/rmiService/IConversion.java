package rmiService;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
 * Interface distante IConversion :
 * --------------------------------
 * En architecture Java RMI (Remote Method Invocation), une interface distante
 * définit le contrat de service partagé entre le client et le serveur.
 * 
 * Principes académiques fondamentaux :
 * 1. Héritage de java.rmi.Remote :
 *    Il s'agit d'une interface marqueur (marker interface). Tout objet dont
 *    les méthodes peuvent être appelées depuis une machine virtuelle Java (JVM)
 *    distante doit implémenter une interface qui étend Remote.
 * 
 * 2. Déclaration de RemoteException :
 *    Chaque méthode d'une interface distante DOIT obligatoirement déclarer
 *    l'exception contrôlée 'java.rmi.RemoteException' dans sa clause throws.
 *    Cette exigence découle des "8 illusions de l'informatique distribuée"
 *    (Fallacies of Distributed Computing) : tout appel distant est susceptible
 *    d'échouer en raison de pannes réseau, de crash de serveur, de timeouts
 *    ou d'erreurs de sérialisation/désérialisation (marshalling/unmarshalling).
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public interface IConversion extends Remote {

    /**
     * Convertit un montant exprimé en Euros vers le Dinar Tunisien (TND).
     * 
     * @param mt Le montant en Euros à convertir.
     * @return Le montant équivalent converti en Dinars Tunisiens (TND).
     * @throws RemoteException En cas d'erreur de communication réseau ou
     *                         d'anomalie lors de l'invocation distante RMI.
     */
    double convertirMontant(double mt) throws RemoteException;
}
