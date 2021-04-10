[![Java CI with Maven](https://github.com/DmLvkvch/google_oauth/actions/workflows/maven.yml/badge.svg)](https://github.com/DmLvkvch/google_oauth/actions/workflows/maven.yml)

# google_oauth
git clone https://github.com/DmLvkvch/google_oauth

put google client_id and client_secret in src/main/resources/application.properties file

add redirect uri (for google oauth) in google api console http://HOST:PORT/code

mvn clean package

cd target

java -jar demo-0.0.1-SNAPSHOT.jar

http://localhost:8080/home


