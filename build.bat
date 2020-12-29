@echo off
set version=%1
set pro=%2


if not defined version (
  echo  usage : build.bat version profile
  echo  example : build 1.0.4-SNAPSHOT release
  goto exit
  )


:build
mvn versions:set -DnewVersion=%version% && mvn clean package gpg:sign org.sonatype.plugins:nexus-staging-maven-plugin:deploy   -P%pro%

:exit