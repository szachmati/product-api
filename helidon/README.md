# shop

Minimal Helidon MP project suitable to start from scratch.

## Build and run

With JDK17+
```bash
docker compose up -d --build
mvn package
java -jar target/shop.jar
````

## Swagger
Swagger API available at:
* http://localhost:8040/openapi
* http://localhost:8040/openapi-ui


## Building a Native Image

Make sure you have GraalVM locally installed:

```
$GRAALVM_HOME/bin/native-image --version
```

Build the native image using the native image profile:

```
mvn package -DskipTests -Pnative-image
```

When command above is not working try

```
mvn clean package -DskipTests && native-image -jar target/shop.jar
```

This uses the helidon-maven-plugin to perform the native compilation using your installed copy of GraalVM. It might take a while to complete.
Once it completes start the application using the native executable (no JVM!):

```
./target/shop
```

Yep, it starts fast. You can exercise the applicationâ€™s endpoints as before.


## Building the Docker Image
```
docker build -t shop .
```

## Running the Docker Image

```
docker run --rm -p 8080:8080 shop:latest
```

Exercise the application as described above.
                                

## Building a Custom Runtime Image

Build the custom runtime image using the jlink image profile:

```
mvn package -Pjlink-image
```

This uses the helidon-maven-plugin to perform the custom image generation.
After the build completes it will report some statistics about the build including the reduction in image size.

The target/shop-jri directory is a self contained custom image of your application. It contains your application,
its runtime dependencies and the JDK modules it depends on. You can start your application using the provide start script:

```
./target/shop-jri/bin/start
```

Class Data Sharing (CDS) Archive
Also included in the custom image is a Class Data Sharing (CDS) archive that improves your applicationĂ˘â‚¬â„˘s startup
performance and in-memory footprint. You can learn more about Class Data Sharing in the JDK documentation.

The CDS archive increases your image size to get these performance optimizations. It can be of significant size (tens of MB).
The size of the CDS archive is reported at the end of the build output.

If youĂ˘â‚¬â„˘d rather have a smaller image size (with a slightly increased startup time) you can skip the creation of the CDS
archive by executing your build like this:

```
mvn package -Pjlink-image -Djlink.image.addClassDataSharingArchive=false
```

For more information on available configuration options see the helidon-maven-plugin documentation.
                                
