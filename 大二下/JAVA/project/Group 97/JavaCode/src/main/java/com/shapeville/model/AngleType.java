package com.shapeville.model;

/**
 * Enumerates the different types of angles for the angle recognition task.
 * Each enum constant represents a specific angle type with a user-friendly display name.
 */
public enum AngleType {
    /** Angle greater than 0 and less than 90 degrees. */
    ACUTE("Acute Angle"),
    /** Angle exactly 90 degrees. */
    RIGHT("Right Angle"),
    /** Angle greater than 90 and less than 180 degrees. */
    OBTUSE("Obtuse Angle"),
    /** Angle exactly 180 degrees. */
    STRAIGHT("Straight Angle"),
    /** Angle greater than 180 and less than 360 degrees. */
    REFLEX("Reflex Angle"),
    /** Angle exactly 360 degrees. Represents a full rotation. */
    FULL_ROTATION("Full Rotation (360°)"), // Added for clarity if needed
    /** Angle exactly 0 degrees. */
    ZERO("Zero Angle (0°)"),             // Added for clarity if needed
    /** Represents an unknown or unclassified angle type. */
    UNKNOWN("Unknown");

    private final String displayName;

    /**
     * Constructs an AngleType enum constant.
     * @param displayName The user-friendly name for the angle type.
     */
    AngleType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the user-friendly display name of the angle type.
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }
}
