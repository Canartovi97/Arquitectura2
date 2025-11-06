package co.ucentral.microservices.parking_microservice.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            // Crear bucket si no existe
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                // Opcional: hacer el bucket público (NO recomendado en producción)
                // minioClient.setBucketPolicy(...);
            }

            // Generar nombre único
            String fileName = UUID.randomUUID() + "-" + cleanFileName(file.getOriginalFilename());

            // Subir archivo
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Devolver URL firmada (válida por 7 días)
            return getPublicUrl(fileName);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    private String cleanFileName(String fileName) {
        // Eliminar caracteres problemáticos (espacios, acentos, etc.)
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public String getPublicUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(7 * 24 * 60 * 60) // 7 días
                        .build()
        );
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO: " + fileName, e);
        }
    }


}
