# MinecraftClone
Mineclone is my final Computer Science 30 project. It is a clone of the popular video game Minecraft. It was written in Java, using LWJGL and KappaEngine to create the 3D scene and fast noise for random noise generation.

## Building
To build this project, Java 12 (or greater) and Maven are required. Once both are installed, install KappaEngine to your local m2 repository using `mvn install` in the KappaEngine directory (same as the pom.xml)

After KappaEngine has been installed, install Mineclone. In the mineclone directory, run `mvn install` to build the project. The shaded jar file will be located in `./target/Sandbox-SNAPSHOT-1.0-jar-with-dependancies.jar`.

The jar file can be run with `java -jar <path to jar>`
