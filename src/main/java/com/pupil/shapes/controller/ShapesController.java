package com.pupil.shapes.controller;

import com.pupil.shapes.model.dto.CreateShapeCommand;
import com.pupil.shapes.model.dto.ShapeResponse;
import com.pupil.shapes.service.ShapeService;
import com.pupil.shapes.service.ShapeValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *  todo Define a consistent response class for successs and error responses, perhaps as per jsonapi spec.
 */
@RestController
public class ShapesController {

    private ShapeService shapeService;

    public ShapesController(ShapeService shapeService) {
        this.shapeService = shapeService;
    }

    @GetMapping(value = "/shapes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<ShapeResponse>> getShapes() {
        return ResponseEntity.ok(shapeService.findAll());
    }

    @PostMapping(value = "/shapes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShapeResponse> createShape(@RequestBody CreateShapeCommand createShapeCommand) {
        try {
            return new ResponseEntity<>(shapeService.create(createShapeCommand), HttpStatus.CREATED);
        } catch (ShapeValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
