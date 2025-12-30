# 📋 Division du Travail - GestionAbonnements

## 🎯 Vue d'Ensemble

Ce projet JavaFX de gestion d'abonnements est divisé en **3 parties équilibrées** pour permettre à 3 élèves de travailler en parallèle.

---

## 👤 **ÉLÈVE 1: Authentification & Utilisateurs**

### Fichiers à gérer

#### Models
- `src/main/java/com/emsi/subtracker/models/User.java`

#### DAO (Data Access)
- `src/main/java/com/emsi/subtracker/dao/UserDAO.java`
- `src/main/java/com/emsi/subtracker/dao/impl/UserDAOImpl.java`

#### Services (Logique Métier)
- `src/main/java/com/emsi/subtracker/services/UserService.java`

#### Controllers (Interface)
- `src/main/java/com/emsi/subtracker/views/LoginController.java`
- `src/main/java/com/emsi/subtracker/views/RegisterController.java`
- `src/main/java/com/emsi/subtracker/views/UserProfileController.java`

#### Utils
- `src/main/java/com/emsi/subtracker/utils/UserSession.java`

### Responsabilités
- ✅ Connexion/Déconnexion
- ✅ Inscription nouveaux utilisateurs
- ✅ Gestion du profil
- ✅ Session utilisateur (Singleton)

---

## 📊 **ÉLÈVE 2: Gestion des Abonnements**

### Fichiers à gérer

#### Models
- `src/main/java/com/emsi/subtracker/models/Abonnement.java`

#### DAO (Data Access)
- `src/main/java/com/emsi/subtracker/dao/SubscriptionDAO.java`
- `src/main/java/com/emsi/subtracker/dao/impl/SubscriptionDAOImpl.java`

#### Services (Logique Métier)
- `src/main/java/com/emsi/subtracker/services/SubscriptionService.java`

#### Controllers (Interface)
- `src/main/java/com/emsi/subtracker/views/DashboardController.java`
- `src/main/java/com/emsi/subtracker/views/AddSubscriptionController.java`

### Responsabilités
- ✅ CRUD complet (Create, Read, Update, Delete)
- ✅ Calcul total mensuel
- ✅ Filtres et recherche
- ✅ Import/Export CSV

---

## 📈 **ÉLÈVE 3: Analytics & Notifications**

### Fichiers à gérer

#### Config
- `src/main/java/com/emsi/subtracker/config/EmailConfig.java`
- `src/main/java/com/emsi/subtracker/config/DatabaseConfig.java`

#### Services
- `src/main/java/com/emsi/subtracker/services/EmailService.java`

#### Controllers
- `src/main/java/com/emsi/subtracker/views/AnalyticsController.java`
- `src/main/java/com/emsi/subtracker/views/SettingsController.java`

#### Utils
- `src/main/java/com/emsi/subtracker/utils/SceneManager.java`
- `src/main/java/com/emsi/subtracker/utils/ThemeManager.java`

#### DAO Base
- `src/main/java/com/emsi/subtracker/dao/base/BaseDAO.java`

### Responsabilités
- ✅ Statistiques (Pie Chart, Bar Chart)
- ✅ Emails (Bienvenue, Alertes J-3)
- ✅ Paramètres (Devise, Thème)
- ✅ Navigation entre écrans

---

## 📊 Répartition des Charges

| Critère | Élève 1 | Élève 2 | Élève 3 |
|---------|:-------:|:-------:|:-------:|
| Fichiers Java | 7 | 5 | 7 |
| Contrôleurs | 3 | 2 | 2 |
| Complexité | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

---

## 🤝 Fichiers Partagés (Tous)

Ces fichiers sont utilisés par tous:
- `Main.java` - Point d'entrée
- `module-info.java` - Configuration modules
- `pom.xml` - Dépendances Maven
- Fichiers `.fxml` - Interfaces
- `styles_v2.css` - Styles

---

## 🛠️ Comment Travailler en Équipe

### 1. Chacun travaille sur sa branche Git
```bash
git checkout -b eleve1-authentification
git checkout -b eleve2-abonnements
git checkout -b eleve3-analytics
```

### 2. Éviter les conflicts
- **Élève 1**: Ne touche PAS aux fichiers d'Abonnement
- **Élève 2**: Ne touche PAS aux fichiers User/Email
- **Élève 3**: Ne touche PAS aux fichiers CRUD

### 3. Merger régulièrement
```bash
git pull origin main
git merge main
```

---

## ✅ Checklist par Élève

### Élève 1
- [ ] Tester Login
- [ ] Tester Inscription
- [ ] Tester Profil utilisateur
- [ ] Vérifier email de bienvenue

### Élève 2
- [ ] Tester Ajout abonnement
- [ ] Tester Modification
- [ ] Tester Suppression
- [ ] Tester Filtres

### Élève 3
- [ ] Tester Graphiques
- [ ] Tester Emails de notification
- [ ] Tester Changement de devise
- [ ] Tester Dark/Light mode

---

## 📞 Coordination

**Responsable de chaque module:**
- 👤 **User/Auth**: Élève 1
- 📊 **Subscriptions**: Élève 2
- 📈 **Analytics/Email**: Élève 3

**Réunions suggérées:**
- Début: Répartition claire des tâches
- Milieu: Point de synchronisation
- Fin: Tests d'intégration ensemble
