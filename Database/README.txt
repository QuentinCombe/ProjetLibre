Copier les fichiers *.php dans le dossier www/projetlibre/ de ton wamp

Dans DatabaseHandler.java, changer l'adresse IP avec l'adresse de ta machine (localhost ne passe pas) - petit ipconfig a effectuer
Dans httpd.conf, dans la balise Directory, remplacer par :

<Directory />
    AllowOverride all
    Allow from All
</Directory>

Désactiver le pare-feu windows pendant l'éxecution, sinon y'aura une erreur d'accès