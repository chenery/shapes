# Concept

- Delegate responsibility of calculating overlapping shapes to the database to make use of the geometry datatype, and spatial indexing.
However a basic square db could be build using logic from here: https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection
- In this case I have used an in-memory MariaDB implementation of MySQL, however in production I would prefer PostGis. 
- Shape is described by the "well known text" geometry representation, this allows a single endpoint for "all shapes",
and avoids implementing concrete classes for shapes, e.g. Square, Triangle etc.

# Limitations

- Only managed polygons, and needs work to get circles
- The ST_Overlaps(g1, g2) function does not detect the "same" shape - need to look into this
- The overlaps function can be "bypassed" by concurrent writes to the database
- Only had time to write "full integration" tests, normally would cover with unit tests, and appropriately scoped integration tests as well.

# Setup

Requirements:

- Java 11

Note: ran out of time to Dockerize 

Build:

```bash
./mvnw package
```

Run: 
```bash
java -jar target/shapes-0.0.1-SNAPSHOT.jar
```

NOTE: Still need to wire up the db run with the application, rather than just in ShapesApplicationTests

# Resources:

- https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection

- https://postgis.net/workshops/postgis-intro/indexing.html

- https://www.baeldung.com/hibernate-spatial

- https://stackoverflow.com/questions/54556185/how-to-embed-in-memory-mariadb4j-to-replace-default-spring-datasource-in-junit-t

- https://en.wikipedia.org/wiki/Well-known_text_representation_of_geometry

- https://stackoverflow.com/questions/39147145/spring-data-jpa-and-geometry-type

- https://dev.mysql.com/doc/refman/8.0/en/spatial-relation-functions-object-shapes.html#function_st-overlaps