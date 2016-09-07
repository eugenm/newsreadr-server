# newsreadr-server

newsreadr is a web based [RSS](http://en.wikipedia.org/wiki/RSS) and [Atom](http://en.wikipedia.org/wiki/Atom_%28standard%29) reader.

## Building

1. Install [Java 8 Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Clone sourcecode
3. Execute ```./mvnw clean install``` on Unix or ```mvnw clean install``` on Windows
4. The target JAR file can be found under target/newsreadr-server.jar

## Installation

1. Install [Java 8 Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Setup a new database by executing ```CREATE DATABASE `newsreadr` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;```
3. Create a new folder
4. Copy **newsreadr-server.jar** and [**application.properties.tpl**](application.properties.tpl) into the folder
5. Rename **application.properties.tpl** to **application.properties**, open it with a text editor and configure the database connection
6. Start the application by executing ```java -jar newsreadr-server.jar```
7. Start your browser, open **http://server:8080** (where server is the name or IP of your computer) and log in with username **admin** and password **password** (please change it immediately).

If you want to install newsreadr as a linux service, have a look at section [Unix/Linux services](http://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-service) 
of the official Spring Boot documentation. 