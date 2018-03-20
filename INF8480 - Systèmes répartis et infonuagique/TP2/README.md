### README TD2 INF8480 ###

### Préalable
Exécutez la commande ant clean suivit de la commande ant dans le dossier TP2.
Ouvrir le fichier config.txt qui se trouve directement dans le dossier TP2 et y inscrire l'addresse IP du poste ou le LDAP s'exécutera. (Un exemple d'addresse IP s'y trouve déjà)

### Ordre d'exécution
1- Partir le serveur LDAP.
2- Démarrez toutes les instances de serveur requis.
3- Démarrez le client.

############# Faire les rmiregistry #############

### rmiregistry du LDAP:
1- Accédez au dossier bin sur le poste où le serveur LDAP s'exécutera.
2- Entrez la commande rmiregistry 5001 &
3- Retournez au dossier TP2 afin de pouvoir exécuter le LDAP

### rmiregistry des serveurs
Faire ceci sur chaque poste acceuillant un serveur.
1- Accédez  au dossier bin sur le poste où le serveur s'exécutera.
2- Entrez la commande rmiregistry 5002 &
3- Retournez au dossier TP2 afin de pouvoir exécuter le serveur

### rmiregistry du client
Faire ceci sur chaque poste acceuillant un serveur.
1- Accédez  au dossier bin sur le poste où le serveur s'exécutera.
2- Entrez la commande rmiregistry 5000 &
3- Retournez au dossier TP2 afin de pouvoir exécuter le client


############# Exécution #############
### Ordre d'exécution
1- Partir le serveur LDAP.
2- Démarrez toutes les instances de serveur requis.
3- Démarrez le client.

### LDAP ###
Usage: Simplement exécuter ldap:
./ldap

### Serveur ###
Usage: ./serveur [qi] [malicious]

Exemple d'un serveur avec un qi de 3 et qui retourne 5% du temps une réponse erronée:
./serveur 3 5

Exemple d'un serveur avec un qi de 5 et qui retourne 70% du temps une réponse erronée:
./serveur 5 70

### Client ###
Usage: ./client [Nom d'usage] [mot de passe] [nom du fichier d'operation] [isSecure (1 ou 0)]

Exemple d'un client avec le nom d'usagé "etienne", le mot de passe "pass123" qui utilise le fichier d'opération "operation-588" et qui utilise le mode sécurisé
./client etienne pass123 operation-588 1

Exemple d'un client avec le nom d'usagé "etienne", le mot de passe "pass123" qui utilise le fichier d'opération "operation-3266" et qui utilise le mode non-sécurisé
./client etienne pass123 operation-3266 0



