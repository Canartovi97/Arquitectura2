package co.ucentral.microservices.parking_microservice.service;

import co.ucentral.microservices.parking_microservice.configuration.mapper.LocationMapper;
import co.ucentral.microservices.parking_microservice.configuration.mapper.ParkingSpaceMapper;
import co.ucentral.microservices.parking_microservice.domain.parkingImage.ParkingImage;
import co.ucentral.microservices.parking_microservice.domain.parkingImage.ParkingImageRepository;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.*;
import co.ucentral.microservices.parking_microservice.exception.ParkingNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceMapper parkingSpaceMapper;
    private final LocationMapper locationMapper;
    private final FileStorageService fileStorageService;
    private final ParkingImageRepository parkingImageRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;



    public ParkingSpace createParkingSpace(NewParkingRequest request, List<MultipartFile> images) {
        // 1. Crear y guardar el espacio
        ParkingSpace parkingSpace = parkingSpaceMapper.toParkingSpace(request);
        ParkingSpace savedSpace = parkingSpaceRepository.save(parkingSpace);

        // 2. Subir imágenes (si existen)
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileUrl = fileStorageService.uploadFile(file);

                    ParkingImage image = ParkingImage.builder()
                            .fileName(extractFileNameFromUrl(fileUrl))
                            .originalName(file.getOriginalFilename())
                            .contentType(file.getContentType())
                            .size(file.getSize())
                            .parkingSpace(savedSpace)
                            .uploadedAt(LocalDateTime.now())
                            .build();

                    parkingImageRepository.save(image);
                }
            }
        }

        return savedSpace;
    }

    private String extractFileNameFromUrl(String url) {
        return url.split("\\?")[0].substring(url.lastIndexOf('/') + 1);
    }


    public ParkingSpaceResponse findById(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingNotFoundException(id));

        List<String> imageUrls = generateImageUrls(parkingSpace.getImages());
        return parkingSpaceMapper.toResponseDto(parkingSpace, imageUrls);
    }

    public Page<ParkingSpaceResponse> findAllParkingSpaces(Pageable pageable) {
        Page<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAllWithImages(pageable);
        return parkingSpaces.map(space -> {
            List<String> imageUrls = generateImageUrls(space.getImages());
            return parkingSpaceMapper.toResponseDto(space, imageUrls);
        });
    }

    private List<String> generateImageUrls(List<ParkingImage> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .map(image -> {
                    try {
                        return fileStorageService.getPublicUrl(image.getFileName());
                    } catch (Exception e) {
                        return "ERROR: Unable to generate URL for " + image.getFileName();
                    }
                })
                .collect(Collectors.toList());
    }


    public Page<ParkingSpaceResponse> findAvailableSpaces(Pageable pageable) {
        Page<ParkingSpace> parkingSpaces = parkingSpaceRepository
                .findByStatus(ParkingSpaceStatus.AVAILABLE, pageable);

        return parkingSpaces.map(space -> {
            List<String> imageUrls = generateImageUrls(space.getImages());
            return parkingSpaceMapper.toResponseDto(space, imageUrls);
        });
    }


    @Transactional
    public void deleteParkingSpace(Long id) {
        // 1. Buscar el espacio con sus imágenes
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingNotFoundException(id));

        // 2. Eliminar imágenes de MinIO (primero las imágenes, luego la BD)
        if (parkingSpace.getImages() != null && !parkingSpace.getImages().isEmpty()) {
            for (ParkingImage image : parkingSpace.getImages()) {
                try {
                    fileStorageService.deleteFile(image.getFileName());
                } catch (Exception e) {
                    // Opcional: loggear el error, pero no detener la eliminación de la BD
                    System.err.println("Warning: Could not delete image from MinIO: " + image.getFileName());
                    // Puedes decidir si lanzar excepción o continuar
                }
            }
        }

        // 3. Eliminar el espacio (y en cascada, las imágenes en BD gracias a orphanRemoval)
        parkingSpaceRepository.delete(parkingSpace);
    }


    public void updateStatus(Long id, ParkingSpaceStatus status) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingNotFoundException(id));
        space.setStatus(status);
        parkingSpaceRepository.save(space);
    }

}
