package com.pupil.shapes.model;

import com.vividsolutions.jts.geom.Polygon;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *  Defines the persisted shape in the database
 */
@Entity(name = "shape")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shape {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    // unique index on the name to improve the lookup performance, and protect against concurrent updates
    @Column(unique=true)
    private String name;
    // todo maybe model with an enum of the supported types
    private String type;
    private String wktGeometry;
    // todo can this be modelled as Geometry?
    private Polygon polygon;
}
