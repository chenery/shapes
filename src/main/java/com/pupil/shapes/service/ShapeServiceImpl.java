package com.pupil.shapes.service;

import com.pupil.shapes.model.Shape;
import com.pupil.shapes.model.dto.CreateShapeCommand;
import com.pupil.shapes.model.dto.ShapeResponse;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *
 */
@Service
public class ShapeServiceImpl implements ShapeService {

    private ShapeRepository shapeRepository;
    private ShapeValidator shapeValidator;

    public ShapeServiceImpl(ShapeRepository shapeRepository, ShapeValidator shapeValidator) {
        this.shapeRepository = shapeRepository;
        this.shapeValidator = shapeValidator;
    }

    @Override
    public ShapeResponse create(CreateShapeCommand createShapeCommand) throws ShapeValidationException {

        WKTReader fromText = new WKTReader();
        Polygon polygon;
        try {
            polygon = (Polygon) fromText.read(createShapeCommand.getWktGeometry());
        } catch (Exception e) {
            throw new ShapeValidationException("Failed to parse shape geometry: " + e.getMessage());
        }

        Shape shape = new Shape(
                UUID.randomUUID().toString(),
                createShapeCommand.getName(),
                createShapeCommand.getType(),
                createShapeCommand.getWktGeometry(),
                polygon);

        shapeValidator.validate(shape);
        shapeRepository.save(shape);
        return new ShapeResponse(shape);
    }

    @Override
    public Iterable<ShapeResponse> findAll() {
        Iterable<Shape> all = shapeRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .map(ShapeResponse::new)
                .collect(Collectors.toList());
    }
}
