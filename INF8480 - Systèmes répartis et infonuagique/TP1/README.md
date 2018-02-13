REAMDE
Travail fait par: Etienne Asselin 1773922 - Vincent Rodier 1744784 - Frederic Bouchard 1734142

1- Afin d'exécuter le client ainsi que le serveur, la commande "ant" doit être exécuter dans le répertoire du client et du serveur.
2- Ensuite la commande "rmiregistry &" doit être exécuter dans le répertoire bin du client et du serveur.
3- Ensuite le client et le serveur peuvent s'exécuter avec "./client [option]" et "./serveur".

Tout les fichiers créer sont enregistrer dans le répertoire "files/" pour le client et le serveur.
Le serveur possède un fichier intituler "lock.txt" qui sert au contrôle des fichiers barrés.
Le client possède un fichier intituler "clientID.txt" qui sert à stocker ces informations d'indentification (id).
Le serveur possède aussi un fichier intituler "clientID.txt" qui sert cette fois à fournir le prochain id au client. 

Tout les appels fait à partir du client sont conforme à ceux défini dans le TP.
L'adresse IP pour les appel au serveur sont hardcoder à la ligne 19 du fichier "client.java" du répertoire "src".
