package rmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
 * Implémentation du service bancaire distant : BanqueImpl
 * -------------------------------------------------------
 * Servant RMI centralisant la gestion des comptes bancaires.
 * 
 * Principes académiques fondamentaux :
 * 1. État interne du serveur et Concurrence :
 *    - Un serveur RMI traite les requêtes distantes au moyen de threads concurrents
 *      alloués dynamiquement par le runtime RMI.
 *    - Pour garantir l'intégrité des données face à des accès multi-clients
 *      simultanés sans verrouillage grossier, une 'ConcurrentHashMap' est utilisée.
 * 
 * 2. Règles Métier :
 *    - Unicité du code : avant d'enregistrer un compte, le serveur vérifie si
 *      un compte possédant le même code existe déjà.
 *    - Consultation : le serveur recherche dans sa structure de données
 *      l'instance correspondante et renvoie ses attributs (code, solde, date).
 * 
 * 3. Exportation et Communication :
 *    - Hérite de UnicastRemoteObject pour exporter automatiquement l'objet
 *      sur le protocole RMI standard.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class BanqueImpl extends UnicastRemoteObject implements IBanque {

    private static final long serialVersionUID = 1L;

    /**
     * Stockage en mémoire des comptes bancaires gérés par le serveur.
     * La clé est le code unique du compte (Integer), la valeur est l'objet Compte.
     * ConcurrentHashMap assure la cohérence des lectures/écritures multi-threads.
     */
    private final Map<Integer, Compte> comptes = new ConcurrentHashMap<>();

    /**
     * Format de date standard pour les journaux d'audit et les réponses aux clients.
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Constructeur par défaut.
     * Déclenche l'exportation du servant RMI sur le port anonyme.
     * 
     * @throws RemoteException En cas d'échec de liaison réseau lors de l'exportation.
     */
    public BanqueImpl() throws RemoteException {
        super();
        System.out.println("[Serveur - BanqueImpl] Service bancaire instancié et exporté avec succès.");
    }

    /**
     * Crée un nouveau compte bancaire si son code n'est pas déjà attribué.
     * 
     * @param c L'instance Compte reçue par désérialisation du client.
     * @return Message de succès ou d'erreur en cas de code préexistant.
     * @throws RemoteException En cas d'erreur de communication RMI.
     */
    @Override
    public String creerCompte(Compte c) throws RemoteException {
        if (c == null) {
            return "[ERREUR] Objet compte nul non accepté.";
        }

        int code = c.getCode();
        System.out.printf("[Serveur - RMI] Requête de création de compte reçue -> Code: %d, Solde: %.2f TND%n",
                code, c.getSolde());

        // Règle métier : Vérification de l'unicité du code
        if (comptes.containsKey(code)) {
            String messageRefus = String.format("[REFUS] Le compte N° %d existe déjà dans le système !", code);
            System.out.println("                -> " + messageRefus);
            return messageRefus;
        }

        // Si la date de création n'a pas été renseignée côté client, on l'estampille à l'heure du serveur
        if (c.getDateCreation() == null) {
            c.setDateCreation(new Date());
        }

        // Enregistrement atomique dans la base en mémoire
        comptes.put(code, c);

        String messageSucces = String.format(
                "[SUCCÈS] Compte N° %d créé avec succès ! Solde initial: %.2f TND | Date: %s",
                code, c.getSolde(), dateFormat.format(c.getDateCreation()));
        System.out.println("                -> " + messageSucces);
        return messageSucces;
    }

    /**
     * Consulte les informations d'un compte bancaire à partir de son identifiant unique.
     * 
     * @param code L'identifiant du compte.
     * @return Informations détaillées du compte ou message d'erreur si introuvable.
     * @throws RemoteException En cas d'erreur de communication RMI.
     */
    @Override
    public String getInfoCompte(int code) throws RemoteException {
        System.out.printf("[Serveur - RMI] Requête de consultation reçue pour le compte N° %d...%n", code);

        Compte c = comptes.get(code);

        if (c != null) {
            String dateStr = (c.getDateCreation() != null) ? dateFormat.format(c.getDateCreation()) : "Inconnue";
            String info = String.format(
                    "[INFO COMPTE] Code: %d | Solde disponible: %.2f TND | Date de création: %s",
                    c.getCode(), c.getSolde(), dateStr);
            System.out.println("                -> Compte trouvé : " + info);
            return info;
        } else {
            String introuvable = String.format("[ERREUR] Aucun compte bancaire n'est associé au code %d.", code);
            System.out.println("                -> " + introuvable);
            return introuvable;
        }
    }
}
