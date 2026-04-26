package com.vicky.staykation.controller;

import com.vicky.staykation.model.Property;
import com.vicky.staykation.model.PropertyImage;
import com.vicky.staykation.repository.PropertyRepository;
import com.vicky.staykation.repository.PropertyImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ImageUploadController(
            PropertyRepository propertyRepository,
            PropertyImageRepository propertyImageRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long propertyId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description)
            throws IOException {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Create upload directory if not exists
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Save file with unique name
        String fileName = UUID.randomUUID() + "_" +
                file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        file.transferTo(new File(filePath));

        // Save image record
        PropertyImage image = new PropertyImage();
        image.setProperty(property);
        image.setImageUrl("/uploads/images/" + fileName);
        image.setDescription(description);
        propertyImageRepository.save(image);

        return ResponseEntity.ok(Map.of(
                "imageUrl", "/uploads/images/" + fileName,
                "message", "Image uploaded successfully"
        ));
    }
}