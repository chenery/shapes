package com.pupil.shapes.service;

import com.pupil.shapes.model.Shape;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *  Uses db to validate shape
 */
@Component
public class RepositoryShapeValidator implements ShapeValidator {

    private ShapeRepository shapeRepository;

    public RepositoryShapeValidator(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    @Override
    public void validate(Shape shape) throws ShapeValidationException {
        // todo validate type field matches wkt geometry?

        // unique name
        Collection<Shape> alsoNamed = shapeRepository.findByName(shape.getName());
        if (alsoNamed.size() > 0) {
            throw new ShapeValidationException("Non Unique Name");
        }

        // not overlapping
        Collection<Shape> overLappingShapes = shapeRepository.findOverLappingShapes(shape.getPolygon());
        if (overLappingShapes.size() > 0) {
            throw new ShapeValidationException("Overlapping Shape");
        }
    }
}
