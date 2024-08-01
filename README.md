# webprojekt
Ein kleines Webprojekt von Merle Erdmann, welches im Rahmen des Moduls Webbasierte Anwendungen an der Hochschule RheinMain entstanden ist.
Projektleiter: Prof. Dr. Wolfgang Weitz

# features
Das Projekt kann über "./gradlew bootRun" im Projektordner gestartet werden und ist dann unter localhost:8080 erreichbar.
Auf der Startseite werden die angebotenen Touren über eine VueJS Single Page Application angezeigt und können gefiltert werden.
Die Touren werden über einen StompBroker automatisch mit dem Backend synchronisiert.

Das Backend ist unter localhost:8080/admin erreichbar, wo neue Benutzer, Orte und Touren angelegt werden können.
Mit dem Link "erstelle Dummy Daten" können vorgefertigte Daten für Orte, Benutzer und Touren erstellt werden.

