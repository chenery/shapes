package com.pupil.shapes.service;

import com.pupil.shapes.model.Shape;

/**
 *
 */
public interface ShapeValidator {

    void validate(Shape shape) throws ShapeValidationException;
}
