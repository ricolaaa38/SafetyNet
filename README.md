SafetyNet Alerts
Description

SafetyNet Alerts est une API REST développée avec Spring Boot, conçue pour faciliter l'interaction entre les services de secours et les habitants. Elle permet de gérer et de diffuser des alertes d’intervention pour les pompiers et d'assurer une prise en compte des besoins spécifiques des citoyens.
Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés sur votre machine :

    Java : version 21 ou supérieure.
    Maven : version 3.8 ou supérieure.
    Git : pour cloner le repository.
    IDE (ex. IntelliJ IDEA, Eclipse, ou Spring Tool Suite).

Installation

Clonez ce repository :

    git clone https://github.com/ricolaaa38/SafetyNet.git
    cd SafetyNet

Construisez le projet avec Maven :

    mvn clean install

Lancez l'application :

Depuis votre IDE : Exécutez la classe principale com.project.SafetyNetApplication.
Ou depuis Maven :

        mvn spring-boot:run

Configuration

L'application utilise un fichier JSON comme base de données. Assurez-vous que le fichier data.json est correctement situé dans src/main/resources. Vous pouvez modifier son contenu pour personnaliser les données.
Stack technique
Version de Spring Boot :

    3.3.4

Version de Java :

    21

Liste des Starters de dépendances :

    spring-boot-starter-web
    Pour créer des APIs REST avec Spring MVC et gérer les requêtes HTTP.

    spring-boot-starter-log4j2
    Pour la gestion avancée des logs avec Log4j2.

    spring-boot-starter-test
    Pour les tests unitaires et d'intégration.

Autres dépendances :

    lombok : Réduction du code boilerplate.
    jackson-databind : Manipulation JSON.

Plugins Maven

    spring-boot-maven-plugin :
    Pour construire et exécuter l'application Spring Boot.

    jacoco-maven-plugin :
    Pour générer un rapport de couverture de tests.
        Version : 0.8.12

    maven-surefire-plugin :
    Pour exécuter les tests unitaires.

    maven-surefire-report-plugin :
    Pour générer un rapport détaillé des tests.

Exécution des Tests

Exécutez les tests :

    mvn clean test

Générez un rapport de couverture de code avec JaCoCo :

    mvn jacoco:report

Le rapport sera généré dans le dossier : target/site/jacoco/index.html

Générez un rapport des tests unitaires avec Surefire :

    mvn verify

Le rapport sera généré dans le dossier : target/site/surefire-report.html
