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
        validarArchivo(archivo);

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

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o no se ha enviado correctamente.");
        }

        String tipo = archivo.getContentType();
        if (tipo == null || (!tipo.equals("image/jpeg") && !tipo.equals("image/png") && !tipo.equals("image/jpg"))) {
            throw new IllegalArgumentException("Formato no permitido. Solo se aceptan imágenes JPG, JPEG o PNG.");
        }
        
        String nombre = archivo.getOriginalFilename();
        if (nombre == null || !nombre.toLowerCase().matches("^.+\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("La extensión del archivo no es válida.");
        }
    }
}