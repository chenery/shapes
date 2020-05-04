package com.pupil.shapes.model.dto;

import com.pupil.shapes.model.Shape;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *  Defines how a shape is returned from the API
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShapeResponse {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String type;
    private String wktGeometry;

    public ShapeResponse(Shape shape) {
        this.id = shape.getId();
        this.name = shape.getName();
        this.type = shape.getType();
        this.wktGeometry = shape.getWktGeometry();
    }
}
