# Centralized Remote File System

This project's goal is to implement a Java centralized file system through RMI.
Many clients can connect to it and can **create**, **modify**, with an **exclusive lock system**, **list** and **synchronise** files.

> Files are kept in Ram in the server.

## RMI Testing

> Comparing execution time for **local RMI server** and **Remote RMI server** with different parameter sizes. 

- Go to `Partie1` folder.
- compile sources : `ant`
- launch rmiregistry from **./bin** : `cd ../bin && rmiregistry &`
- launch server : `cd .. && ./server`

## Using the File System

- Go to `Partie2` folder.
- To compile `ant`
- Launch rmiregistry : `cd ../bin && rmiregistry &`
- Launch server : `cd .. && ./server`
- How to use client :

```bash
./client create file # will fail if an other file has the same name on the server
./client list 
./client lock file # => will download file if you don't have it locally
./client get file
./client push file # => need to be locked before
./client syncLocalDir # => overide your version
```

French report is available in **CR** folder : **cr.pdf**


## Author

- [Jérémy Wimsingues](https://github.com/JWimsingues/infonuagique-tp2)
- [Robin Royer](https://github.com/robinroyer)
