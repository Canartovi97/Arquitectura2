package co.ucentral.microservices.payment_microservice.service;

import co.ucentral.microservices.payment_microservice.client.BookingDto;
import co.ucentral.microservices.payment_microservice.client.BookingServiceClient;
import co.ucentral.microservices.payment_microservice.client.ParkingSpaceInformation;
import co.ucentral.microservices.payment_microservice.domain.payment.CreatePaymentRequest;
import co.ucentral.microservices.payment_microservice.domain.payment.Payment;
import co.ucentral.microservices.payment_microservice.domain.payment.PaymentRepository;
import co.ucentral.microservices.payment_microservice.domain.payment.PaymentResponse;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {


    @Value("${mercado-pago.access-token}")
    private String accessToken;

    private final PaymentRepository paymentRepository;

//    @Value("${app.frontend-url}")
    private String frontendUrl = "https://google.com";

    private final BookingServiceClient bookingServiceClient;

    public PaymentResponse createPayment(CreatePaymentRequest request) throws MPException {
        try {
            // 1. Validar que la reserva exista (opcional pero recomendado)
//            bookingServiceClient.getBookingById(request.bookingId());

            BookingDto bookingDto = bookingServiceClient.getBookingById(request.bookingId());
            ParkingSpaceInformation parking = bookingServiceClient.getBookingByIdInformation(bookingDto.id());

            // 2. Configurar Mercado Pago
            MercadoPagoConfig.setAccessToken(accessToken);

            // 3. Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(frontendUrl + "/payment/success")
                    .pending(frontendUrl + "/payment/pending")
                    .failure(frontendUrl + "/payment/failure")
                    .build();

            // 4. Crear item de pago
//            PreferenceItemRequest item = PreferenceItemRequest.builder()
//                    .id(request.bookingId().toString())
//                    .title("Reserva de estacionamiento")
//                    .pictureUrl("\"C:\\Users\\santi\\OneDrive\\Imágenes\\carro.png\"")
//                    .description(request.description())
//                    .quantity(1)
//                    .currencyId(request.currency())
//                    .unitPrice(request.amount())
//                    .build();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(parking.id().toString())
                    .title(parking.title() + " " + parking.description() + " " + bookingDto.totalAmount() + " desde las: " + bookingDto.startTime() + " hasta las: " + bookingDto.endTime()  )
                    .pictureUrl(parking.imageUrls().getFirst())
                    .description(parking.description())
                    .quantity(1)
                    .currencyId(request.currency())
                    .unitPrice(bookingDto.totalAmount())
                    .build();

            // 5. Crear preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .build();

            PreferenceClient client = new PreferenceClient();
            var preference = client.create(preferenceRequest);

            // 6. Guardar pago en BD con estado PENDING
//             paymentRepository.save(createPaymentEntity(request, preference.getId()));



            return new PaymentResponse(
                    preference.getSandboxInitPoint(),
                    preference.getId()
            );

        } catch (MPException | MPApiException e) {
            throw new MPException("Error creating Mercado Pago preference: " + e.getMessage());
        }
    }




}
