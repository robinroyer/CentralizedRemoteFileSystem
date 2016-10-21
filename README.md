# Centralized Remote File System

This project's goal is to implement a Java centralized file system through RMI.
Many clients can connect to it and can **create**, **modify**, with an **exclusive lock system**, **list** and **synchronise** files.

> Files are kept in Ram in server side.

## RMI Testing

- compile sources : `ant`
- launch rmiregistry from **./bin** : `cd ../bin && rmiregistry &`
- launch server : `cd .. && ./server`

## Using the File System

- To compile `ant`
- launch rmiregistry : `cd ../bin && rmiregistry &`
- launch server : `cd .. && ./server`
- How to use client :

```bash
./client create file # will fail if an other file has the same name on the server
./client list 
./client lock file # => will download file if you don't have it locally
./client get file
./client push file # => need to be locked before
./client syncLocalDir # => overide your version
```

French report is available at root : **cr.pdf**


## Author

- [Jérémy Wimsingues](https://github.com/JWimsingues/infonuagique-tp2)
- [Robin Royer](https://github.com/robinroyer)
