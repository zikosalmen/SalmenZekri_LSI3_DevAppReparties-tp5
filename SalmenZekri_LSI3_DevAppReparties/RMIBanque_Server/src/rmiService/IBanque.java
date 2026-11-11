package rmiService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import metier.Compte;

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
 * Interface distante IBanque :
 * ----------------------------
 * Définit le contrat d'opérations bancaires distribuées offertes aux clients.
 * 
 * Principes académiques fondamentaux :
 * 1. Interface Distante RMI :
 *    - Étend l'interface 'java.rmi.Remote'.
 *    - Sert de spécification commune partagée entre les couches client et serveur.
 * 
 * 2. Semantique des paramètres :
 *    - Dans la méthode creerCompte(Compte c), le paramètre 'c' est une instance
 *      sérialisable transmise PAR COPIE (marshalling binaire vers le serveur).
 *      Toute modification ultérieure effectuée par le serveur sur 'c' n'altère
 *      pas l'objet local présent dans la JVM du client.
 *    - Le paramètre primitif 'int code' de getInfoCompte(int code) est encodé
 *      directement dans le flux binaire de l'appel RPC.
 *    - La valeur de retour String est un objet Java standard intrinsèquement
 *      sérialisable.
 * 
 * 3. Gestion des erreurs distribuées :
 *    - Les deux méthodes propagent 'RemoteException', prévoyant toute défaillance
 *      liée au transport réseau, à la sérialisation ou au serveur.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public interface IBanque extends Remote {

    /**
     * Crée un nouveau compte bancaire sur le serveur distant.
     * Vérifie au préalable l'unicité du code de compte.
     * 
     * @param c L'objet Compte à enregistrer (transmis par valeur).
     * @return Un message textuel confirmant la création ou signalant l'existence d'un doublon.
     * @throws RemoteException En cas d'erreur de communication réseau RMI.
     */
    String creerCompte(Compte c) throws RemoteException;

    /**
     * Consulte les informations détaillées d'un compte bancaire à partir de son code.
     * 
     * @param code L'identifiant unique du compte recherché.
     * @return La représentation textuelle du compte ou un message d'erreur si introuvable.
     * @throws RemoteException En cas d'erreur de communication réseau RMI.
     */
    String getInfoCompte(int code) throws RemoteException;
}
