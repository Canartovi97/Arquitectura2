package co.ucentral.microservices.payment_microservice.controller;

import co.ucentral.microservices.payment_microservice.domain.payment.CreatePaymentRequest;
import co.ucentral.microservices.payment_microservice.domain.payment.PaymentResponse;
import co.ucentral.microservices.payment_microservice.service.PaymentService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/mercado")
    public String payment() throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken("APP_USR-7357938394818510-110118-090eb7821b6c0c49e2bf27fce49fdfca-2959447849");

        PreferenceBackUrlsRequest backUrls =
// ...
                PreferenceBackUrlsRequest.builder()
                        .success("https://www.tu-sitio/success")
                        .pending("https://www.tu-sitio/pending")
                        .failure("https://www.tu-sitio/failure")
                        .build();

//        PreferenceRequest request = PreferenceRequest.builder().backUrls(backUrls).build();

        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id("1234")
                        .title("Games")
                        .description("PS5")
                        .pictureUrl("http://picture.com/PS5")
                        .categoryId("games")
                        .quantity(2)
                        .currencyId("BRL")
                        .unitPrice(new BigDecimal("4000"))
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items).backUrls(backUrls).build();
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);
        return preference.getSandboxInitPoint();

    }




    @PostMapping("/mercado-pago")
    public ResponseEntity<PaymentResponse> createMercadoPagoPayment(
            @Valid @RequestBody CreatePaymentRequest request) throws MPException {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }


}