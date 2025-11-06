//package co.ucentral.microservices.notification_microservice.controller;
//
//import co.ucentral.microservices.notification_microservice.domain.notification.CreateNotificationRequest;
//import co.ucentral.microservices.notification_microservice.domain.notification.NotificationResponse;
//import co.ucentral.microservices.notification_microservice.service.NotificationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/notifications")
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//
//
//    @PostMapping
//    public ResponseEntity<NotificationResponse> createNotification(
//            @Valid @RequestBody CreateNotificationRequest request) {
//        NotificationResponse response = notificationService.createNotification(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<NotificationResponse>> getNotifications(
//            @RequestParam Long userId,
//            @PageableDefault(size = 10) Pageable pageable) {
//        Page<NotificationResponse> response = notificationService.getNotificationsByUser(userId, pageable);
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/{id}/read")
//    public ResponseEntity<NotificationResponse> markAsRead(
//            @PathVariable UUID id,
//            @RequestParam Long userId) {
//        NotificationResponse response = notificationService.markAsRead(id, userId);
//        return ResponseEntity.ok(response);
//    }
//
//
//}
