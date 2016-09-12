# infonuagique-tp1
TP1 INF4410 infonuagique poly mtl


## Partie 1

- creation de la machine virtuelle avec la bonne "flavor"
- creation et association avec la vm, de l'ip flottante
- copie des fichier sur le serveur distant: 
`scp -i ../cloud.key -r ResponseTime_Analyzer ubuntu@132.207.12.200:` 
- connexion ssh : `sh -i cloud.key ubuntu@132.207.12.200`
- compiler les sources: `cd ResponseTime_Analyzer && ant`
- lancer rmiregistry depuis les bin `cd ../bin && rmiregistry &`
- lancer le server: `cd .. && ./server`





