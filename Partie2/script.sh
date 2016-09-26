jaune='\e[1;33m'
neutre='\e[0;m'

echo -e ${jaune}Suppression .user, fileX et processus rmiregistry ${neutre}
pkill rmiregistry
rm file*
rm .user

echo -e ${jaune}Clean, git pull, ant ${neutre}
ant clean
git pull
ant

echo -e ${jaune}Lancement rmi registry ${neutre}
cd bin
rmiregistry &
cd ..

echo -e ${jaune}Lancement du serveur ${neutre}
./server
