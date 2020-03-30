[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
### Description
Rendgine is a work-in-progress rendering framework designed primarily for personal use in my future art projects. However, it may be useful for others as well, or just as reference for various mechanics.

### Building
Gradle is used as the dependency and build management software. Included in the repo is the Gradle Wrapper which allows you to run gradle commands from the root directory of the project. You can compile and run the program with `gradlew run`. To build the source into a runnable jar, execute `gradlew build`.

To generate project files for IDEA and Eclipse: `gradlew idea` and `gradlew eclipse` respectively. Your IDE may also have the ability to import Gradle projects.
