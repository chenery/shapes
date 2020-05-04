package com.pupil.shapes.service;

import com.pupil.shapes.model.dto.CreateShapeCommand;
import com.pupil.shapes.model.dto.ShapeResponse;

/**
 *
 */
public interface ShapeService {

    ShapeResponse create(CreateShapeCommand createShapeCommand) throws ShapeValidationException;

    Iterable<ShapeResponse> findAll();
}
