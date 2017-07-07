Domotics
========

Domestic Robotics is an interesting way of merging hobby electronics and programming. This project contains both Arduino projects for small sensor stations and a backend REST server to capture these readings into a MySQL database and an Angular2 front end to display charts of the readings capture.

This is an early cut of this project. Started on 3rd July 2016. Revised for Angular2/4 on July 2017.

To Build final
==============

`ng build --prod`

Copy everything from `dist` folder to your `public` folder under `resources`

`mvn package`

To run
======

Open a command window and run:

`ng serve`

The start script is setup to transpile so that any changes to .ts files will be recompiled

posit is pre-defined so can run `http://localhost:4200`

Alternatively you can configure the default HTTP host and port used by the development server with command-line options :

`ng serve --host 0.0.0.0 --port 4201`

Run Java project from inside IntelliJ IDE to start REST interface.

Use browser to connect to `http://localhost:31000/` 
to see inital chart.

`http://localhost:31000/api/health` 
for all Actuator REST endpoints.

can also build final combined jar using Build final instructions above and then:
 
` java -jar demotics-1.0-SNAPSHOT.jar`


To rebuild 
==========

Uses Maven to define dependencies and build process for project

For Front end:

need nodeJS - 4.4.7 LTS

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

Apache Tomcat/8.0.33

MySQL driver 5.1.39