Please leave a star if you like it!

# MinecraftClone
Mineclone is my final Computer Science 30 project. It is a clone of the popular video game Minecraft. It was written in Java, using LWJGL and KappaEngine to create the 3D scene and fast noise for random noise generation.

## Running In Netbeans
To run the project in netbeans, right click in the files pane on the left side. Choose Open project. Navigate to the location where you downloaded the project files, and open the KappaEngine project. This folder is located inside the files you downloaded. Repeat the same steps for the Mineclone (Sandbox) project. After both projects are in netbeans, build kappaengine by selecting KappaEngine in the left pane, and select the build button on the top of the screen (You can also use F11). After, you can select Sandbox on the side and click run. This should launch the game.

## Building
To build this project, Java 12 (or greater) and Maven are required. Once both are installed, install KappaEngine to your local m2 repository using `mvn install` in the KappaEngine directory (same as the pom.xml)

After KappaEngine has been installed, install Mineclone. In the mineclone directory, run `mvn install` to build the project. The shaded jar file will be located in `./target/Sandbox-SNAPSHOT-1.0-jar-with-dependancies.jar`.

The jar file can be run with `java -jar <path to jar>`

## How to play
WSAD to move, SPACE to jump. Left click will destroy the block you are looking at, and right click will place oak planks.
