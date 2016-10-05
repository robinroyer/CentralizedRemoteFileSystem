INF4410 - Infonuagique 
Polytechnique Montréal
Travail Pratique 1  
Jérémy Wimsingues - 1860682
Robin Royer - 1860715

## Partie 1

- compiler les sources : `ant`
- lancer rmiregistry depuis les bin : `cd ../bin && rmiregistry &`
- lancer le server : `cd .. && ./server`

## Partie 2

- compiler `ant`
- lancer rmiregistry : `cd ../bin && rmiregistry &`
- lancer le server : `cd .. && ./server`
- utiliser le client :
```
./client create file
./client list
./client lock file
./client get file
./client push file
./client syncLocalDir
```

Les détails de choix de conception est les résultats des tests sont disponibles
dans le rapport de TP situé à la racine de l'archive : cr.pdf