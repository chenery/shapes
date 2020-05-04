package com.pupil.shapes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request a shape to be created
 */
@Getter
@AllArgsConstructor
public class CreateShapeCommand {
    private String name;
    private String type;
    private String wktGeometry;
}
