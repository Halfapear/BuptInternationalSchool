package com.shapeville.utils;

import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utility class for loading and scaling images from resources.
 * This class provides a static method to easily load image resources and scale them to a desired size.
 */
public final class ImageLoader {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ImageLoader() {
        // Prevent instantiation
    }

    /**
     * Loads an image from the application's resources and scales it to the specified dimensions.
     * The image path should be relative to the classpath (e.g., "/assets/images/myimage.png").
     * @param imagePath The path to the image resource.
     * @param width The desired width for the scaled image.
     * @param height The desired height for the scaled image.
     * @return An {@link ImageIcon} containing the scaled image, or {@code null} if the image cannot be loaded or scaled.
     */
    public static ImageIcon loadImageAndScale(String imagePath, int width, int height) {
        try {
            // 从资源文件中加载图片
            InputStream is = ImageLoader.class.getResourceAsStream(imagePath);
            if (is == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            
            // 读取图片
            Image image = ImageIO.read(is);
            if (image == null) {
                System.err.println("Cannot load image: " + imagePath);
                return null;
            }
            
            // 缩放图片
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
            
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
}