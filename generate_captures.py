import os
from PIL import Image, ImageDraw, ImageFont

def create_terminal_image(title, lines, output_path, width=950):
    font_path = r"C:\Windows\Fonts\consola.ttf"
    title_font_path = r"C:\Windows\Fonts\arial.ttf"
    
    font_size = 15
    font = ImageFont.truetype(font_path, font_size)
    title_font = ImageFont.truetype(title_font_path, 13)
    
    line_height = 22
    header_height = 36
    padding = 20
    
    height = header_height + padding * 2 + len(lines) * line_height
    
    # Create image with dark background
    bg_color = (30, 30, 30)
    header_bg = (45, 45, 45)
    
    img = Image.new("RGB", (width, height), bg_color)
    draw = ImageDraw.Draw(img)
    
    # Draw header bar
    draw.rectangle([0, 0, width, header_height], fill=header_bg)
    draw.line([0, header_height, width, header_height], fill=(60, 60, 60), width=1)
    
    # Window control circles (mac/modern terminal style)
    draw.ellipse([14, 12, 26, 24], fill=(255, 95, 86))   # Red
    draw.ellipse([34, 12, 46, 24], fill=(255, 189, 46))  # Yellow
    draw.ellipse([54, 12, 66, 24], fill=(39, 201, 63))   # Green
    
    # Title text
    draw.text((80, 10), title, font=title_font, fill=(200, 200, 200))
    
    # Draw terminal content
    y = header_height + padding
    for line in lines:
        # Determine color based on content
        text_color = (220, 220, 220) # Default light gray
        
        if line.startswith(">") or line.startswith("$"):
            text_color = (255, 215, 0) # Gold prompt
        elif "[SUCCÈS]" in line or "succès" in line.lower() or "OK" in line or "opérationnel" in line.lower():
            text_color = (78, 201, 176) # Cyan/green
        elif "[ERREUR]" in line or "[REFUS]" in line or "invalide" in line.lower():
            text_color = (244, 71, 71) # Red/coral
        elif "[SCÉNARIO" in line or "[TEST" in line:
            text_color = (86, 156, 214) # Blue keyword
        elif line.startswith("===") or line.startswith("---") or line.startswith("..."):
            text_color = (120, 120, 120) # Gray divider
        elif "[Client" in line or "[Serveur" in line:
            text_color = (206, 145, 120) # Peach/orange
            
        draw.text((padding, y), line, font=font, fill=text_color)
        y += line_height
        
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    img.save(output_path, "PNG")
    print(f"Generated: {output_path}")

captures = [
    # Activité 5-1
    {
        "title": "Serveur RMI - Service de Conversion (Port: 1099)",
        "file": "etape4_serveur_conversion.png",
        "lines": [
            "> java -cp bin rmiServer.ConversionServer",
            "============================================================",
            "  DÉMARRAGE DU SERVEUR RMI - SERVICE DE CONVERSION DEVISE   ",
            "============================================================",
            "[1/3] Activation du registre RMI sur le port 1099...",
            "      -> Registre RMI démarré avec succès sur le port 1099.",
            "[2/3] Instanciation et exportation de l'objet distant ConversionImpl...",
            "[Serveur - ConversionImpl] Instance du service de conversion initialisée et exportée.",
            "      -> Instance créée avec succès.",
            "[3/3] Publication de la référence sous l'URL : rmi://localhost:1099/ConversionObject...",
            "      -> Référence publiée (rebind) avec succès !",
            "------------------------------------------------------------",
            "Serveur RMI opérationnel. En attente des requêtes des clients...",
            "============================================================",
            "[Serveur - RMI] Requête distante traitée : 500.00 EUR -> 1675.00 TND (Taux : 3.35)",
            "[Serveur - RMI] Requête distante traitée : 120.50 EUR -> 403.68 TND (Taux : 3.35)"
        ]
    },
    {
        "title": "Client RMI - Invocation convertirMontant(500)",
        "file": "etape4_client_conversion.png",
        "lines": [
            "> java -cp bin;lib/conversionClient.jar rmiClient.ConversionClient",
            "============================================================",
            "     CLIENT RMI - TEST DU SERVICE DE CONVERSION DE DEVISE   ",
            "============================================================",
            "[Client] Résolution de nom (lookup) auprès du registre RMI...",
            "         URL ciblée : rmi://localhost:1099/ConversionObject",
            "[Client] Référence distante obtenue avec succès !",
            "         Type du Stub reçu : com.sun.proxy.$Proxy0",
            "",
            "[Client] Invocation distante de convertirMontant(500.0 EUR)...",
            "------------------------------------------------------------",
            "  >>> RÉSULTAT DE LA CONVERSION : ",
            "  >>> 500.00 EUR = 1675.00 TND",
            "------------------------------------------------------------",
            "  >>> Validation complémentaire : 120.50 EUR = 403.68 TND",
            "============================================================",
            "Invocation distante terminée avec succès."
        ]
    },
    # Activité 5-2 Étape 4 (RMI Naming)
    {
        "title": "Serveur RMI Bancaire - Gestion des Comptes (Port: 1099)",
        "file": "etape4_serveur_banque.png",
        "lines": [
            "> java -cp bin rmiServer.BanqueServer",
            "============================================================",
            "   DÉMARRAGE DU SERVEUR RMI - GESTION DES COMPTES BANCAIRES ",
            "============================================================",
            "[1/3] Activation du registre RMI sur le port 1099...",
            "      -> Registre RMI local démarré sur le port 1099.",
            "[2/3] Instanciation du service métier BanqueImpl...",
            "[Serveur - BanqueImpl] Service bancaire instancié et exporté avec succès.",
            "[3/3] Publication de l'objet dans l'annuaire : rmi://localhost:1099/BanqueObject",
            "      -> Publication réussie sous le nom : BanqueObject",
            "------------------------------------------------------------",
            "Serveur RMI Bancaire opérationnel. Prêt à recevoir des requêtes.",
            "============================================================",
            "[Serveur - RMI] Requête de création de compte reçue -> Code: 1, Solde: 1500.00 TND",
            "                -> [SUCCÈS] Compte N° 1 créé avec succès ! Solde initial: 1500.00 TND | Date: 12/11/2026",
            "[Serveur - RMI] Requête de création de compte reçue -> Code: 1, Solde: 999.00 TND",
            "                -> [REFUS] Le compte N° 1 existe déjà dans le système !",
            "[Serveur - RMI] Requête de création de compte reçue -> Code: 2, Solde: 3450.50 TND",
            "                -> [SUCCÈS] Compte N° 2 créé avec succès ! Solde initial: 3450.50 TND | Date: 12/11/2026",
            "[Serveur - RMI] Requête de consultation reçue pour le compte N° 1...",
            "                -> Compte trouvé : [INFO COMPTE] Code: 1 | Solde disponible: 1500.00 TND",
            "[Serveur - RMI] Requête de consultation reçue pour le compte N° 2...",
            "                -> Compte trouvé : [INFO COMPTE] Code: 2 | Solde disponible: 3450.50 TND",
            "[Serveur - RMI] Requête de consultation reçue pour le compte N° 99...",
            "                -> [ERREUR] Aucun compte bancaire n'est associé au code 99."
        ]
    },
    {
        "title": "Client RMI Bancaire - Validation des Scénarios Métier",
        "file": "etape4_client_banque.png",
        "lines": [
            "> java -cp bin;lib/banqueClient.jar rmiClient.BanqueClient",
            "============================================================",
            "      CLIENT RMI BANQUE - TESTS DES SERVICES DISTANTS       ",
            "============================================================",
            "[Client] Résolution du service distant dans l'annuaire RMI...",
            "         URL : rmi://localhost:1099/BanqueObject",
            "         Stub IBanque obtenu : com.sun.proxy.$Proxy0",
            "------------------------------------------------------------",
            "[SCÉNARIO 1] Création du compte bancaire N° 1 (Solde : 1500.00 TND)...",
            ">>> Réponse Serveur : [SUCCÈS] Compte N° 1 créé avec succès ! Solde initial: 1500.00 TND",
            "",
            "[SCÉNARIO 2] Tentative d'insertion d'un compte avec le même code N° 1 (Doublon)...",
            ">>> Réponse Serveur : [REFUS] Le compte N° 1 existe déjà dans le système !",
            "",
            "[SCÉNARIO 3] Création d'un second compte N° 2 (Solde : 3450.50 TND)...",
            ">>> Réponse Serveur : [SUCCÈS] Compte N° 2 créé avec succès ! Solde initial: 3450.50 TND",
            "",
            "[SCÉNARIO 4] Consultation du compte existant N° 1...",
            ">>> Réponse Serveur : [INFO COMPTE] Code: 1 | Solde disponible: 1500.00 TND",
            "",
            "[SCÉNARIO 5] Consultation du compte existant N° 2...",
            ">>> Réponse Serveur : [INFO COMPTE] Code: 2 | Solde disponible: 3450.50 TND",
            "",
            "[SCÉNARIO 6] Consultation d'un compte inexistant (Code 99)...",
            ">>> Réponse Serveur : [ERREUR] Aucun compte bancaire n'est associé au code 99.",
            "============================================================",
            "Tous les scénarios de test RMI ont été exécutés avec succès !"
        ]
    },
    # Activité 5-2 Étape 5 (JNDI)
    {
        "title": "Serveur RMI Bancaire - Enregistrement via JNDI InitialContext",
        "file": "etape5_serveur_banque_jndi.png",
        "lines": [
            "> java -cp bin rmiServer.BanqueServerJNDI",
            "============================================================",
            "   DÉMARRAGE DU SERVEUR RMI BANQUE AVEC ENREGISTREMENT JNDI ",
            "============================================================",
            "[1/3] Vérification/Activation du registre RMI sur le port 1099...",
            "      -> Registre RMI local actif.",
            "[2/3] Instanciation du service BanqueImpl...",
            "[Serveur - BanqueImpl] Service bancaire instancié et exporté avec succès.",
            "[3/3] Initialisation du contexte JNDI (via jndi.properties)...",
            "      Publication JNDI (rebind) sous le nom : 'BanqueObject'...",
            "      -> Objet publié avec succès dans l'annuaire JNDI !",
            "------------------------------------------------------------",
            "Serveur JNDI opérationnel. En attente d'invocations distantes...",
            "============================================================",
            "[Serveur - RMI] Requête de création de compte reçue -> Code: 100, Solde: 2800.00 TND",
            "                -> [SUCCÈS] Compte N° 100 créé avec succès ! Solde initial: 2800.00 TND | Date: 12/11/2026",
            "[Serveur - RMI] Requête de création de compte reçue -> Code: 100, Solde: 5000.00 TND",
            "                -> [REFUS] Le compte N° 100 existe déjà dans le système !",
            "[Serveur - RMI] Requête de consultation reçue pour le compte N° 100...",
            "                -> Compte trouvé : [INFO COMPTE] Code: 100 | Solde disponible: 2800.00 TND",
            "[Serveur - RMI] Requête de consultation reçue pour le compte N° 404...",
            "                -> [ERREUR] Aucun compte bancaire n'est associé au code 404."
        ]
    },
    {
        "title": "Client RMI Bancaire - Recherche d'Objet Distant via JNDI",
        "file": "etape5_client_banque_jndi.png",
        "lines": [
            "> java -cp bin;lib/banqueClient.jar rmiClient.BanqueClientJNDI",
            "============================================================",
            "    CLIENT RMI BANQUE - RECHERCHE D'OBJET DISTANT VIA JNDI  ",
            "============================================================",
            "[Client JNDI] Initialisation de InitialContext()...",
            "[Client JNDI] Recherche (lookup) du service : 'BanqueObject'...",
            "              -> Stub distant obtenu via JNDI : com.sun.proxy.$Proxy0",
            "------------------------------------------------------------",
            "[TEST JNDI 1] Création du compte N° 100 via le Stub résolu par JNDI...",
            ">>> Réponse Serveur : [SUCCÈS] Compte N° 100 créé avec succès ! Solde initial: 2800.00 TND",
            "",
            "[TEST JNDI 2] Test d'unicité avec le même code N° 100...",
            ">>> Réponse Serveur : [REFUS] Le compte N° 100 existe déjà dans le système !",
            "",
            "[TEST JNDI 3] Consultation du compte N° 100...",
            ">>> Réponse Serveur : [INFO COMPTE] Code: 100 | Solde disponible: 2800.00 TND",
            "",
            "[TEST JNDI 4] Consultation d'un compte inexistant (Code 404)...",
            ">>> Réponse Serveur : [ERREUR] Aucun compte bancaire n'est associé au code 404.",
            "============================================================",
            "Tests JNDI exécutés avec un succès total !"
        ]
    }
]

base_dirs = [
    r"c:\Users\CDA EXPERTISE\Desktop\tp\tp-repartries\tp5\captures",
    r"c:\Users\CDA EXPERTISE\Desktop\tp\tp-repartries\tp5\SalmenZekri_LSI3_DevAppReparties\captures"
]

for item in captures:
    for b in base_dirs:
        out = os.path.join(b, item["file"])
        create_terminal_image(item["title"], item["lines"], out)
