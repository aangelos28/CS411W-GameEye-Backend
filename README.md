# GameEye Backend
This is the project for the GameEye backend. The project is composed of multiple subprojects:
* Main backend (primary backend of GameEye)
* CLI (command line interface for interacting with the backend)
* Common (common code library shared between subprojects)

## Requirements
* Java 11

## Installation
Open the project with IntelliJ IDEA. If everything went smoothly, IntelliJ should have recognized
the subprojects as modules. You can check this under `File > Project Struture`.

You might need to add configurations to compile and run the projects. The configurations should be
`Spring Boot` and point to the main classes of the necessary subprojects.

In order to run the projects you need to put `secrets.json` under the `resources` folder in
the CLI and main backend subprojects.
