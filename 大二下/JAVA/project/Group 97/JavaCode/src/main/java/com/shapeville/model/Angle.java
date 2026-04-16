package com.shapeville.model;

/**
 * Represents an angle with its degree value and determined angle type.
 * Used in the angle recognition task to define the problem data.
 */
public class Angle {
    private double value; // in degrees
    private AngleType correctType;

    /**
     * Constructs a new Angle object.
     * Automatically determines the angle type based on the provided value.
     * @param value The angle value in degrees.
     */
    public Angle(double value) {
        this.value = value;
        this.correctType = determineType(value);
    }

    /**
     * Gets the degree value of the angle.
     * @return The angle value.
     */
    public double getValue() { return value; }

    /**
     * Gets the determined type of the angle.
     * @return The {@link AngleType} of the angle.
     */
    public AngleType getCorrectType() { return correctType; }

    /**
     * Determines the type of an angle based on its degree measurement.
     * Classifies angles as Zero, Acute, Right, Obtuse, Straight, Reflex, or Full Rotation.
     * Handles values within the 0-360 range.
     * @param angle The angle measurement in degrees.
     * @return The {@link AngleType} corresponding to the given angle.
     */
    public static AngleType determineType(double angle) {
        // Normalize angle to be within 0-360 for some checks, though input is expected to be
        // double normalizedAngle = ((angle % 360) + 360) % 360; // Handles negative angles too

        if (angle == 0) return AngleType.ZERO;
        if (angle > 0 && angle < 90) return AngleType.ACUTE;
        if (angle == 90) return AngleType.RIGHT;
        if (angle > 90 && angle < 180) return AngleType.OBTUSE;
        if (angle == 180) return AngleType.STRAIGHT;
        if (angle > 180 && angle < 360) return AngleType.REFLEX;
        if (angle == 360) return AngleType.FULL_ROTATION;
        return AngleType.UNKNOWN; // For values outside 0-360 or other unhandled cases
    }

    @Override
    /**
     * Returns a string representation of the Angle object.
     * @return A string containing the angle value and its determined type.
     */
    public String toString() {
        return "Angle{" +
               "value=" + value +
               ", correctType=" + correctType.getDisplayName() +
               '}';
    }
}
