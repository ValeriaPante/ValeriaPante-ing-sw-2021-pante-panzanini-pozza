## Softaware Engeneering Final Project 2020/21
# _Masters of Renaissance_


The goal of the project was to create a software version of the board game Masters of Renaissance.
#### Basic functionalities implemented:
 - Complete rules
 - CLI
 - GUI
 - Socket
#### Advanced functionalities implemented:
 - Parameter editor
 - Multiple games
 - Local game

### How to run JARs
#### Server
#### Client
In case you want to start the game in the CLI version, you need to digit the following command on the terminal:

    java -jar client.jar -cli

>For a better command line game experience it is necessary that the client supports UTF-8 encoding, otherwise some features of the CLI, such as colours, will be lost.

To start the game in the GUI version, you need to digit the following line on the terminal:

    java -jar client.jar

Thanks to the insertion inside the pom of the javaFX dependencies, client application is cross-platform, therefore it can be executed in all the main operating systems (Windows, Linux, MacOS).

>For the first run of the client application, it is needed to start an online game. 
In this way the client can download all the assets needed (imgs, JSONs) to show the view of the game. The user needs to perform this action
in order to have the latest updates of the assets, which can vary if a parametrization has been done.

>For the offline game, it's recommended to insert the JAR and its assets inside an empty folder.


Coverage Test:

![](https://github.com/ValeriaPante/ing-sw-2021-pante-panzanini-pozza/blob/main/Coverage.png)
