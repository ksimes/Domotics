Domotics
========

Domestic Robotics is an intersting way of merging hobby electronics and programming. This project contains both Arduino projects for small sensor stations and a backend REST server to capture these readings into a MySQL database and an Angular2 front end to display charts of the readings capture.

This is an early cut of this project. Started on 3rd July 2016.

To run
======

Open a command window and run:

npm start

The start script is setup to run tsc -w so that any changes to .ts files will be recompiled to JS

Run Java project from inside IntelliJ IDE to start REST interface.

Use browser to connect to http://localhost:8080/

to see inital chart.

To rebuild 
==========

Uses SBT to define dependancies for Java project

For Front end:

need nodeJS - 4.4.7 LTS

Use Windows x64 msi installer

Follow instructions to start at:

https://angular.io/docs/js/latest/quickstart.html

Add chart.js/latest/quickstart

npm install chart.js --save

Add ng2-charts for TypeScript

npm install ng2-charts --save

Note: Must include a style for the chart to get it to show on screen.

Notes
=====

Spring Boot v1.3.5.RELEASE,

Spring v4.2.6.RELEASE

Angular 2.0 RC4 Using TypeScript

Apache Tomcat/8.0.33