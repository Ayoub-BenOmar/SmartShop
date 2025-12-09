# SmartShop — Backend REST API

## Description

SmartShop est une API REST (backend seulement) qui fournit les fonctionnalités essentielles d'un magasin en ligne : gestion des utilisateurs (admin / client), gestion des produits, gestion des commandes et des paiements. Le projet sert de prototype/POC pour démontrer une architecture en couches propre, l'usage de DTOs et mappers, la gestion des erreurs, la pagination filtrée et une authentification simple par session HTTP.

L'API est destinée aux opérations administratives (CRUD produits, gestion des utilisateurs, création de commandes/paiements) et aux opérations client (consultation de profil, historique de commandes, statistiques personnelles).

---

## Technologies utilisées

- Langage : Java 17
- Framework : Spring Boot
- ORM : Spring Data JPA (Hibernate)
- Base de données : PostgreSQL en production, H2 pour les tests
- Mapping DTO ↔ Entité : MapStruct
- Réduction de boilerplate : Lombok
- Tests : JUnit 5, Mockito
- Build : Maven
- Authentification : HTTP Session (login/logout), pas de Spring Security ni JWT
- Gestion centralisée des erreurs : `@ControllerAdvice` (`GlobalExceptionHandler`)

---

## Installation & Exécution

1. Prérequis : Java 11+ (recommandé 17), Maven, une base de données PostgreSQL (optionnel pour tests locaux).

2. Configuration : modifier `src/main/resources/application.properties` pour renseigner la connexion à la base :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/SmartShop_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

3. Compiler et exécuter :

- Compilation :

```bash
mvn clean package
```

- Lancer en mode développement :

```bash
mvn spring-boot:run
```

- Ou exécuter le jar :

```bash
java -jar target/SmartShop-0.0.1-SNAPSHOT.jar
```

4. Tests :

- Lancer tous les tests unitaires et d'intégration :

```bash
mvn test
```

Remarque : les tests d'intégration utilisent H2 en mémoire pour isoler la base réelle.

---

## Architecture

L'application suit une architecture en couches classique :

- Controller : points d'entrée HTTP (validation basique, contrôle de session). Ex : `AuthController`, `AdminController`, `ClientController`.
- Service : logique métier (transactions, règles d'intégrité, calculs). Ex : `ProductService`, `CommandeService`.
- Repository : interfaces Spring Data JPA qui exposent l'accès aux données.
- Model : entités JPA (`model.entity`) et DTOs (`model.dto`).
- Mapper : MapStruct pour convertir entités ⇄ DTOs (`model.mapper`).
- Exceptions : exceptions métiers personnalisées + `GlobalExceptionHandler` pour mapper exception → code HTTP.

Conventions :
- Les services ne renvoient que des DTO aux contrôleurs.
- Les entités JPA ne sont pas exposées directement à la couche API.
- Validation via annotations (`@Valid`, `@NotNull`, `@Size`, ...).

---

## Conception

Principes et choix de conception :

- Séparation des préoccupations : chaque couche a une responsabilité claire.
- DTOs + Mappers : évitent d'exposer les entités et permettent d'ajuster le modèle d'API indépendamment du schéma persistant.
- MapStruct : mappers compilés offrant de bonnes performances et un code propre.
- Lombok : réduit le code répétitif (getters/setters/constructeurs).
- Pagination & filtres : `ProductService` propose une méthode paginée avec filtres dynamiques (name, minPrice, maxPrice, isVisible) via `Specification` et `JpaSpecificationExecutor`.
- Authentification : session HTTP simple (cookie `JSESSIONID`), stockage de `USER_ID` et `USER_ROLE` en session. Flux : login → session créée → cookie renvoyé au client.
- Gestion d'erreurs : exceptions métiers spécifiques (BadRequest, Unauthorized, Forbidden, NotFound, UnprocessableEntity) et handler central renvoyant un JSON uniforme contenant timestamp, status, error, message et path.
- Paiements : le statut de paiement appartient désormais à l'entité `Paiement` (plus de `paymentStatus` sur `Commande`) pour permettre plusieurs paiements partiels.
- Sécurité : prototype simple sans Spring Security ; pour production, on recommandera Spring Security + JWT / sessions sécurisées.

---

## Endpoints principaux

Liste non exhaustive, exemples et usage :

- Auth
  - POST /api/auth/login — body : `{ "username": "...", "password": "..." }` (renvoie DTO user et crée session)
  - POST /api/auth/logout — invalide la session

- Admin (requiert session ADMIN)
  - POST /api/admin/create-client — créer un client (body: `UserDto`)
  - GET /api/admin/users — lister les utilisateurs
  - GET /api/admin/users/{id} — récupérer utilisateur
  - PUT /api/admin/users/{id} — mettre à jour utilisateur
  - DELETE /api/admin/users/{id}
  - POST /api/admin/create-product — créer produit (body: `ProductDto`)
  - PUT /api/admin/update-product/{id}
  - DELETE /api/admin/delete-product/{id} — marque `isVisible=false`
  - GET /api/admin/products — liste produits visibles
  - GET /api/admin/products/{id} — produit par id
  - GET /api/admin/products/paged — pagination
    - Query params : `page` (default 0), `size` (default 10), `name`, `minPrice`, `maxPrice`, `isVisible`
  - POST /api/admin/create-commande — créer commande
  - POST /api/admin/create-payment — créer paiement
  - PUT /api/admin/commandes/{id}/status/{status} — mettre à jour statut commande

Exemples (curl) :

- Login (sauvegarder cookie) :

```bash
curl -i -c cookies.txt -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"pwd"}' \
  http://localhost:8080/api/auth/login
```

- Appel paginé protégé (avec cookie) :

```bash
curl -i -b cookies.txt "http://localhost:8080/api/admin/products/paged?page=0&size=10&name=chaise"
```

---

## Tests unitaires

- Frameworks : JUnit 5 et Mockito.
- Tests inclus :
  - `ProductServiceTest` — tests unitaires du `ProductService` avec `ProductRepository` et `ProductMapper` moqués.
  - `ProductRepositoryH2Test` — test d'intégration `@DataJpaTest` utilisant H2 pour valider les requêtes du repository.

Exécution des tests :

```bash
mvn test
```

Conseils :
- Les tests unitaires doivent couvrir les règles métier des services (ex : calcul total, mise à jour fidélité, contrôles de stock).
- Les tests d'intégration doivent se faire sur une base H2 pour être isolés de la base réelle.

---

## Règles métier importantes

Voici les règles métier appliquées dans le code :

- Les mots de passe sont hachés avant persistance (BCrypt). Les DTO retournés n’exposent pas le mot de passe.
- Seul un administrateur (session contenant `USER_ROLE = ADMIN`) peut créer/supprimer/modifier des produits et des utilisateurs via les endpoints `/api/admin/*`.
- La création d'une commande :
  - Doit contenir au moins un item ; sinon 400 Bad Request.
  - Pour chaque item, la quantité demandée est comparée au stock disponible ; si insuffisant → 422 Unprocessable Entity.
  - Le total, la TVA et le montant restant sont calculés et stockés sur la commande.
  - La fidélité du client est mise à jour après confirmation de commande (règles basées sur nombre de commandes et montant cumulé).
- Paiements :
  - Chaque paiement est lié à une commande et possède son propre `PaymentStatus`.
  - Montant du paiement doit être positif (400) et ne doit pas dépasser le montant restant (422).
  - Lorsqu'un paiement ramène le remainingAmount de la commande à 0, la commande passe en statut `CONFIRMED`.
- Erreurs standardisées :
  - 400 → Validation / BadRequestException
  - 401 → UnauthorizedException (login requis)
  - 403 → ForbiddenException (permissions insuffisantes)
  - 404 → NotFoundException
  - 422 → UnprocessableEntityException (règles métiers violées)
  - 500 → erreurs internes inattendues

---

## Auteur

- Ayuub (développements et modifications dans ce dépôt)

