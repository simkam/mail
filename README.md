How to setup and run the mail server
====================================
1) build the server: mvn install
2) start the server: sudo java -jar target/jbcts-greenmail-1.0-SNAPSHOT-jar-with-dependencies.jar
3) setup mailboxes: $TS_HOME/bin/ant populateMailbox (this has to be executed every time the mail server is started
4) run tests
