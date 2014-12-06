eat (Restaurant/Taxi Recommendation MashUp)
===========================================

Project info: Restaurant Recommendation system that provides recommendations based on user's free text query.

Pre-requisites:
 1. Java JDK v1.7 and Java Runtime Environment
 2. Apache Maven v3.2.3 or later
 3. Apache Tomcat v7.0
 
Steps to build and deploy the application on local tomcat server:
 1. Navigate to the project folder named eat-webapp
 2. Clean, compile, build and deploy - mvn clean package tomcat7:deploy
 3. Clean, compile, build and deploy - mvn clean package tomcat7:reploy

Other useful maven goals:
 1. Compile and package the jar file - mvn package
 2. compile - mvn compile
 3. test - mvn test
 4. clean the previous builds - mvn clean
 5. Deploy the build - mvn tomcat7:deploy
