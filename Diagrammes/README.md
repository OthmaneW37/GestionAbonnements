# Diagrammes UML - GestionAbonnements

Diagrammes UML de conception pour l'application de gestion d'abonnements.

## 📋 Diagrammes disponibles

### 1. Diagramme de Classes
**Fichier**: `DiagrammeClasses.puml`

Modèle de données simplifié avec les 2 entités principales:
- **User**: Représente un utilisateur (id, username, email, password)
- **Abonnement**: Représente un abonnement (id, nom, prix, date, fréquence, catégorie)

**Relation**: Un utilisateur possède plusieurs abonnements (1 → 0..*)

---

### 2. Diagramme de Cas d'Utilisation
**Fichier**: `DiagrammeCasUtilisation.puml`

Fonctionnalités principales:
- S'inscrire / Se connecter
- Ajouter / Modifier / Supprimer un abonnement
- Consulter ses abonnements
- Voir les statistiques

---

### 3. Diagramme de Séquence - Authentification
**Fichier**: `DiagrammeSequence_Authentification.puml`

Flux simplifié de connexion utilisateur.

---

### 4. Diagramme de Séquence - Gestion d'Abonnements
**Fichier**: `DiagrammeSequence_GestionAbonnement.puml`

Deux scénarios principaux:
- Ajouter un abonnement
- Consulter la liste des abonnements

---

### 5. Diagramme d'Activité
**Fichier**: `DiagrammeActivite_AjoutAbonnement.puml`

Processus d'ajout d'un abonnement avec validation basique.

---

### 6. Diagramme de Composants
**Fichier**: `DiagrammeComposants.puml`

Architecture 3-tiers simplifiée:
- Interface Utilisateur (JavaFX)
- Logique Métier
- Accès aux Données

---

### 7. Diagramme de Déploiement
**Fichier**: `DiagrammeDeploiement.puml`

Architecture physique:
- Client: Application JavaFX
- Serveur: Base de données SQL Server

---

### 8. Diagramme d'États
**Fichier**: `DiagrammeEtats_Abonnement.puml`

Cycle de vie simplifié d'un abonnement:
- Actif → En modification → Actif
- Actif → Supprimé

---

## 🛠️ Visualiser les diagrammes

### Option 1: En ligne
1. Ouvrir [PlantUML Online](http://www.plantuml.com/plantuml/uml/)
2. Copier-coller le contenu d'un fichier `.puml`
3. Visualiser le diagramme généré

### Option 2: VSCode
1. Installer l'extension **PlantUML**
2. Ouvrir un fichier `.puml`
3. Appuyer sur `Alt+D` pour prévisualiser

### Option 3: Générer des images
```bash
# Installer PlantUML
choco install plantuml  # Windows
brew install plantuml   # macOS

# Générer les images
cd Diagrammes
plantuml *.puml         # PNG
plantuml -tsvg *.puml   # SVG
```

---

## 📝 Notes

Ces diagrammes représentent la **conception initiale** du projet, avant l'implémentation. Ils montrent les fonctionnalités essentielles de manière claire et concise.

**Technologies**:
- JavaFX pour l'interface
- SQL Server pour la base de données
- Architecture 3-tiers classique
