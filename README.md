# newsreadr-server

newsreadr is a web based [RSS](http://en.wikipedia.org/wiki/RSS) and [Atom](http://en.wikipedia.org/wiki/Atom_%28standard%29) reader.

## Building

1. Install [Java 8 Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Install [Maven 3](http://maven.apache.org)
3. Clone sourcecode
4. Execute ```mvn clean install```
5. The target JAR file can be found under target/newsreadr-server.jar

## Installation

1. Install [Java 8 Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Create a new folder
3. Copy **newsreadr-server.jar** and [**application.properties.tpl**](application.properties.tpl) into the folder
4. Rename **application.properties.tpl** to **application.properties**, open it with a text editor and configure the database connection
5. Start the application by executing ```java -jar newsreadr-server.jar```
6. Start your browser, open **http://server:8080** (where server is the name or IP of your computer) and log in with username **admin** and password **password** (please change it immediately).

If you want to install newsreadr as a linux service, have a look at section [55.1 Unix/Linux services](http://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-service) 
of the official Spring Boot documentation. 