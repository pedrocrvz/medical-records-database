# medical-records-database

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
