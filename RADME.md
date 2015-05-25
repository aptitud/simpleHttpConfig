### SimpleHttpConfig ###

Shamelessly stolen from  on https://github.com/spray/spray-template/tree/on_spray-can_1.3.

Builds an executable jar that starts a service on port 8080, or can be run as a docker container.

# Build Executable Jar #


Run the sbt assembly command 
```
sbt 
> assembly
... 
java -jar target/target/scala-2.11/http-config-assembly-0.1.jar 

```

# Build Docker image #
```
docker build -t httpconfig  .
docker run -d -p 8080:8080 httpconfig
```




