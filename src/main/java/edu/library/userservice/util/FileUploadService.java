package edu.library.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FileUploadService {
    private static final Logger logger = Logger.getLogger(FileUploadService.class.getName());

    @Value("${file.storage.path}")
    private String fileStoragePath;

    public String savePhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded or the file is empty.");
        }

        try {
            validateStoragePath();

            String fileExtension = getFileExtension(Objects.requireNonNull(photo.getOriginalFilename()));
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Create the target file path
            Path targetLocation = Paths.get(fileStoragePath, newFileName);

            // Copy the file to the target location
            Files.copy(photo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to store the file", e);
            throw new RuntimeException("Failed to store photo", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid input", e);
            throw e;
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex > 0) ? fileName.substring(lastDotIndex) : "";
    }

    private void validateStoragePath() {
        Path storageDir = Paths.get(fileStoragePath);

        if (!Files.exists(storageDir)) {
            try {
                Files.createDirectories(storageDir);
                logger.info("File storage directory created at: " + fileStoragePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create storage directory", e);
            }
        }
    }

    public Resource downloadPhoto(String fileName) throws Exception {
        // Load file as Resource
        Path filePath = Paths.get(fileStoragePath).resolve(fileName);
        Resource resource = new FileSystemResource(filePath);

        // Check if the file exists
        if (!resource.exists()) {
            throw new Exception("File not found with name: " + fileName);
        }
        return resource;
    }

    public void deletePhoto(String photoPath) throws IOException {
        Path path = Paths.get(photoPath);
        if (Files.exists(path)) {
            Files.delete(path);
        } else {
            System.out.println("Photo not found at path: " + photoPath);
        }
    }
}
