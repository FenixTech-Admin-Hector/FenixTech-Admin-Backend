package com.proyecto.fenixtech.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final Path root = Paths.get("/app/uploads");

    public String guardarImagen(MultipartFile archivo) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            
            Files.copy(archivo.getInputStream(), this.root.resolve(nombreUnico));
            
            return nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen: " + e.getMessage());
        }
    }
}