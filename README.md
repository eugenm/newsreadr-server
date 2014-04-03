# newsreadr-server

newsreadr is a web based [RSS](http://en.wikipedia.org/wiki/RSS) and [Atom](http://en.wikipedia.org/wiki/Atom_%28standard%29) reader.

## Installation

### Prerequisites
* Install a [Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (at least version 7)
* Install an application server like [Tomcat](http://tomcat.apache.org) (tested with Tomcat 7, the server must support at least Servlet API v3)

### Instructions

1. Download the latest newsreadr release (**newsreadr-server-&lt;version&gt;.tar.gz**) from http://nexus.patrick-gotthard.de/content/repositories/releases/de/patrickgotthard/newsreadr/newsreadr-server *
2. Unpack the downloaded archive
3. Open the unpacked **.newsreadr** folder, edit the contained **newsreadr.properties** and enter your database connection details
4. Move the **.newsreadr** folder to the home folder of the user your application server runs with
5. Give the same user write permissions for the **.newsreadr** directory
6. Deploy the **newsreadr-server-&lt;version&gt;.war** file on your application server
7. Log in with username 'admin' and password 'password'
8. Change the username and password of the admin user (not necessary but recommended for security)

\* Currently there are only development builds available under http://nexus.patrick-gotthard.de/content/repositories/snapshots/de/patrickgotthard/newsreadr/newsreadr-server

## Build from source

### Prerequisites
* Install a [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (at least version 7)
* Install [Maven](http://maven.apache.org) (tested with version 3.2.1 but 3.1.1 should work too)

### Instructions
1. Clone the sourcecode
2. execute ```mvn clean install```