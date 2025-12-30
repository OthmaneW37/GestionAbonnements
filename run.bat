@echo off
echo Configuration de JAVA_HOME...
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
echo JAVA_HOME define sur: %JAVA_HOME%

echo Lancement de l'application...
call mvn clean javafx:run
pause
