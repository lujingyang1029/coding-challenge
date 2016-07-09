#!/usr/bin/env bash

javac -classpath ./src/json-simple-1.1.1.jar:./src/ ./src/median_degree.java
cd src && jar cvfm program.jar manifest.txt ./json-simple-1.1.1.jar median_degree.class
java -jar program.jar ../venmo_input/venmo-trans.txt ../venmo_output/output.txt
