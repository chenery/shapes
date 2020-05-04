package com.pupil.shapes.service;

import com.pupil.shapes.model.Shape;
import com.vividsolutions.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 *
 */
public interface ShapeRepository extends CrudRepository<Shape, String> {

    Collection<Shape> findByName(String name);

    @Query(value = "SELECT * FROM shape s WHERE overlaps(s.polygon, ?1)", nativeQuery = true)
    Collection<Shape> findOverLappingShapes(Polygon polygon);
}
