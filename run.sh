#!/bin/sh
cd front && ./gradlew run &
cd api && ./gradlew run