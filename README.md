Domotics
========

Domestic Robotics is an interesting way of merging hobby electronics and programming. This project contains both Arduino projects for small sensor stations running on ESP8266 chipsets to draw information from DHT22 sensors 
and a backend REST server to capture these readings into an Arango database as well as an Angular front end to display charts of the readings capture. 

It is planned to produce a heat map of the house showing over time (maybe using a slider) how heat is distributed over
time.

This is still early in this project. Started on 3rd July 2016. Revised for Angular2/4 on July 2017.
Converted from MySQLDb to AngularDb in 2018 as I needed to learn more about Arango for work. 
As in all these projects in this a Work in Progress and with all pet projects developed by professional programmers, there are no damn tests! Feel free to add some as they will almost certainly pick up issues in the UI.

To Build for deployment
==============

Open a Git bash command window and run:

`mvn package`

will produce a integrated jar which contains both the web front end in production mode and 
the Java back end, including the endpoint for receiving sensor values from sensor stations.

I need to include instructions of how to build one of the senor stations in the future.

To run locally
======

Open a bash command window and run:

`ng serve`

The start script is setup to transpile so that any changes to .ts files will be recompiled

port is pre-defined so can run `http://localhost:4200`

Alternatively you can configure the default HTTP host and port used by the development server with command-line options :

`ng serve --host 0.0.0.0 --port 4201`

Run Java project from inside IntelliJ IDE to start REST interface.

Use browser to connect to `http://localhost:4200/domotics` 
to see inital table of sensor stations.

`http://localhost:4200/api/health` 
for all Actuator REST endpoints.

can also build final combined jar using Build final instructions above and then:
 
` java -jar demotics-1.0-SNAPSHOT.jar`


To rebuild 
==========

Uses Maven 3.3.3 to load dependencies and the build management for the project.

For Front end:

need minimum nodeJS - 4.4.7 LTS

Use Windows x64 msi installer

Follow instructions to start at:

`https://angular.io/docs/js/latest/quickstart.html`

Install ng (also called Angular Command Line Interface)

`npm install -g @angular/cli`

Add chart.js/latest/quickstart

`npm install chart.js --save`

Add ng2-charts for TypeScript

`npm install ng2-charts --save`

Note: Must include a style for the chart to get it to show on screen.

Add moment library for formatting dates

`npm install moment --save`

Notes
=====

Java 1.8.0,

Spring Boot v1.4.2.RELEASE,

Spring Boot Actuator v1.4.2.RELEASE,

Spring Boot Auto Configure v1.4.2.RELEASE,

Spring v4.2.6.RELEASE

Angular 2.0 RC4 Using TypeScript

Arango DB Java Driver 3.3.0

**Check the POM.xml for latest details.**