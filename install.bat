@echo off
set version=%1
set pro=%2

if not defined version (
  echo  usage : install.bat version
  echo  example : install 1.0.4-SNAPSHOT
  goto exit
  )

:build
mvn versions:set -DnewVersion=%version% && mvn versions:commit && mvn clean install -P%pro%

:exit