# Akka-kadabra

This is the accompanying example project to the Akka-kadabra talk given at the Scala User Group Düsseldorf by Ronny Bräunlich.
Start the example application by entering `sbt "project kadabraJVM" "run"` into your terminal.
After that, you can access the website on `localhost:8080/index`

To curl some data use:
``` 
curl -X PUT -H "Content-Type: application/json" --data '{"author": "bar", "text": "foo"}' http://localhost:8080/addEntry
```

To retrieve the data use:
```
curl http://localhost:8080/all
```