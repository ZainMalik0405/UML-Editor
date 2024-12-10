package com.example.umleditor.ui.components;

import java.io.Serializable;

/**
 * Represents the types of arrows used in UML diagrams to denote different relationships.
 */
public enum ArrowType implements Serializable {

    /**
     * Generalization (inheritance) relationship.
     */
    GENERALIZATION,

    /**
     * Aggregation relationship (a whole-part relationship with shared ownership).
     */
    AGGREGATION,

    /**
     * Composition relationship (a whole-part relationship with exclusive ownership).
     */
    COMPOSITION,

    /**
     * Association relationship (a general connection between two classes).
     */
    ASSOCIATION
}
