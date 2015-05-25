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

To store data you'll need a mongo that runs on port 27107 on a resolvable host 'mongo'

# Build Docker image #

```
docker build -t httpconfig  .
```
It needs a mongo database connection, via a linked container (or a host)

```
docker run -d -p 8080:8080 --link <runnning docker container>:mongo httpconfig 


docker run -d -p 8080:8080 --add-host <ip of mongo host>:mongo httpconfig
```



