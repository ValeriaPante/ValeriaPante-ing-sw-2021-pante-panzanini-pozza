## Software Engineering Final Project 2020/21
# _Masters of Renaissance_


The goal of the project was to create a software version of the board game Masters of Renaissance.
#### Basic functions implemented:
 - Complete rules
 - CLI
 - GUI
 - Socket
#### Advanced functions implemented:
 1. Parameter editor
    - Leader cards:
      - requirements 
      - abilities 
      - images
    - Development cards:
      - cost
      - production power
      - images
    - Faith Track:
      - Division
      - Pope favour position
      - Image
 2. Multiple games
 3. Local game

### How to run JARs
#### Server
***For the correct behavior it's mandatory to complete the customisation process at least once.***

The server by default is bind to the port 42000

To customize the assets of the game through GUI you can launch the server with the following command on the terminal.

    java -jar server.jar --custom

No connections will be accepted when customising.
If the customisation is interrupted by quitting at any time, the process will be closed and will not be considered complete.
Once this phase is complete the server will be automatically ready for incoming requests.

If the server is run via the command below it will be ready for incoming requests, and it will load the customisations previously chosen with ***java -jar server.jar --custom***.

    java -jar server.jar

#### Client
To start the game in the CLI version, you need to digit the following command on the terminal:

    java -jar client.jar -cli

>For a better command line game experience it is necessary that the client supports UTF-8 encoding, otherwise some features of the CLI, such as colours, will be lost.

To start the game in the GUI version, you need to digit the following line on the terminal:

    java -jar client.jar

Thanks to the javaFX dependencies in the pom file, this client is a cross-platform application, therefore it can be executed on all the main operating systems (Windows, Linux, MacOS).

>When the client application is first run, an online game must be started. 
This is so that the client can download all the assets needed (images, JSONs) to show the view of the game. The user needs to perform this action
in order to have the latest update of the assets, which can vary if any customisation has been done server-side.

>For an offline game, it's recommended to insert the JAR and its assets inside an empty folder.


### Coverage Tests
HTML documentation that reports the overall test coverage is available [here](../deliverables/CoverageTest).

