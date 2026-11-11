package metier;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Entité Métier : Compte (Data Transfer Object - DTO)
 * ----------------------------------------------------
 * Représente un compte bancaire manipulé par le service réparti.
 * 
 * Principes académiques fondamentaux :
 * 1. Sérialisation d'objets (java.io.Serializable) :
 *    - Dans un système distribué, la mémoire n'est pas partagée entre JVMs distinctes.
 *    - Pour transmettre un objet métier en argument d'un appel distant (ex: creerCompte(Compte c)),
 *      cet objet DOIT implémenter 'java.io.Serializable'.
 *    - Contrairement aux objets distants (servants) qui sont transmis PAR RÉFÉRENCE
 *      (via leur Stub/Proxy), les objets sérialisables sont transmis PAR VALEUR
 *      (deep copy / passage par copie).
 * 
 * 2. Mécanisme de Marshalling / Unmarshalling :
 *    - Marshalling : L'état interne de l'objet (champs non-transient) est encodé
 *      en flux binaire sérialisé côté client.
 *    - Acheminement réseau par le protocole RMI (JRMP).
 *    - Unmarshalling : La JVM du serveur reconstruit une nouvelle instance identique
 *      dans son propre tas (Heap).
 * 
 * 3. Identifiant de version (serialVersionUID) :
 *    - Garantit la compatibilité binaire entre la version de la classe présente
 *      côté client et celle présente côté serveur lors de la désérialisation.
 * 
 * @author Salmen Zekri
 * @version 1.0
 */
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Code unique identifiant le compte bancaire.
     */
    private int code;

    /**
     * Solde actuel du compte en Dinars Tunisiens (TND).
     */
    private double solde;

    /**
     * Date et heure de création du compte bancaire.
     */
    private Date dateCreation;

    /**
     * Constructeur par défaut (sans arguments).
     * Requis pour les mécanismes de sérialisation Java et la convention JavaBean.
     */
    public Compte() {
        this.dateCreation = new Date();
    }

    /**
     * Constructeur avec paramètres pour initialiser un compte bancaire complet.
     * 
     * @param code         Identifiant unique du compte.
     * @param solde        Solde initial.
     * @param dateCreation Date d'ouverture du compte.
     */
    public Compte(int code, double solde, Date dateCreation) {
        this.code = code;
        this.solde = solde;
        this.dateCreation = (dateCreation != null) ? dateCreation : new Date();
    }

    // =========================================================================
    // Getters et Setters (Accesseurs et Mutateurs)
    // =========================================================================

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = (dateCreation != null) ? sdf.format(dateCreation) : "N/A";
        return String.format("Compte [Code = %d | Solde = %.2f TND | Date de création = %s]",
                code, solde, formattedDate);
    }
}
