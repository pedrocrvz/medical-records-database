# medical-records-database

# Documentation is included in this folder, read carefully to understand with in-depth knowledge this project implementation

Project was developed and tested with Ubuntu 16.04, OpenJDK 1.8.0_151, Maven 3.3.9, and Python 3.5.2

## Key generation (1 hospital, 1 doctor/hospital, 1 patient)
```
cd keys
./gen-keys.py --h 1 --d 1 --p 1
```

## Compile to Jar
```
mvn package
```

## Start RMI Directory
```
./rmi &
```

## Run NHS example
```
java -cp target/medical-records-database-1.0-SNAPSHOT-jar-with-dependencies.jar pt.ulisboa.tecnico.sirs.NHS
``` 

## Run Hospital example
```
java -cp target/medical-records-database-1.0-SNAPSHOT-jar-with-dependencies.jar pt.ulisboa.tecnico.sirs.Hospital


