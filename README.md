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

## Compile project
```
mvn compile
```

## Run NHS
```
mvn exec:java -Dexec.mainClass="pt.ulisboa.tecnico.sirs.NHS" -Dexec.args="keys/NHS.jks NHS password123"
``` 

## Run Hospital example
```
mvn compile exec:java -Dexec.mainClass="pt.ulisboa.tecnico.sirs.Hospital" -Dexec.args="keys/Hospital-1.jks Hospital-1 password123"
```
