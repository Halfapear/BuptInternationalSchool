package com.shapeville.model;

/**
 * Represents a geometric shape with its name, image path, and type.
 * Used to define the shapes for recognition and calculation tasks.
 */
public class Shape {
    private String name;
    private String imagePath; // Relative path from resources/assets, e.g., "/assets/shapes/circle.png"
    private String type;      // e.g., "2D_BASIC", "3D_ADVANCED", "CIRCLE", "RECTANGLE"

    /**
     * Constructs a new Shape object.
     * @param name The name of the shape (e.g., "circle", "cube").
     * @param imagePath The relative path to the shape's image file.
     * @param type The type of the shape (e.g., "2D", "3D").
     */
    public Shape(String name, String imagePath, String type) {
        this.name = name;
        this.imagePath = imagePath;
        this.type = type;
    }

    /**
     * Gets the name of the shape.
     * @return The shape's name.
     */
    public String getName() { return name; }

    /**
     * Gets the relative path to the shape's image file.
     * @return The image path.
     */
    public String getImagePath() { return imagePath; }

    /**
     * Gets the type of the shape.
     * @return The shape's type.
     */
    public String getType() { return type; }

    @Override
    /**
     * Returns a string representation of the Shape object.
     * @return A string containing the shape's name, image path, and type.
     */
    public String toString() {
        return "Shape{" +
               "name='" + name + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", type='" + type + '\'' +
               '}';
    }
    // TODO: Consider adding equals() and hashCode() if shapes are stored in Sets or used as Map keys
}