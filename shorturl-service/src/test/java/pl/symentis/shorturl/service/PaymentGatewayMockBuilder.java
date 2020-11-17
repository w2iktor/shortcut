package pl.symentis.shorturl.service;

import java.util.Map;
import java.util.function.Supplier;

public class PaymentGatewayMockBuilder {
    public static class PaymentInteractionBuilder {
        private final PaymentGatewayMockBuilder builder;
        private final PaymentRequest paymentRequest;

        public PaymentInteractionBuilder(PaymentGatewayMockBuilder builder, PaymentRequest paymentRequest) {
            this.builder = builder;
            this.paymentRequest = paymentRequest;
        }

        public PaymentInteractionBuilder then(Supplier<PaymentResponse> response) {
            builder.addRequest(paymentRequest, response);
            return this;
        }

        public PaymentInteractionBuilder callback(Supplier<PaymentCallbackRequest> callback) {
            builder.addCallback(paymentRequest, callback);
            return this;
        }

        public PaymentGatewayMockBuilder and() {
            return builder;
        }

    }

    static class PaymentResponseAndCallback {
        Supplier<PaymentResponse> paymentResponse;
        Supplier<PaymentCallbackRequest> paymentCallbackRequest;
    }

    private final Map<PaymentRequest, PaymentGatewayMockBuilder.PaymentResponseAndCallback> interactions;

    public PaymentGatewayMockBuilder(Map<PaymentRequest, PaymentGatewayMockBuilder.PaymentResponseAndCallback> interactions) {
        this.interactions = interactions;
    }

    public PaymentGatewayMockBuilder.PaymentInteractionBuilder when(PaymentRequest paymentRequest) {
        return new PaymentGatewayMockBuilder.PaymentInteractionBuilder(this, paymentRequest);
    }

    void addCallback(PaymentRequest paymentRequest, Supplier<PaymentCallbackRequest> paymentCallbackRequest) {
        interactions.compute(paymentRequest, (key, oldValue) -> {
            if (oldValue == null) {
                PaymentGatewayMockBuilder.PaymentResponseAndCallback paymentResponseAndCallback = new PaymentGatewayMockBuilder.PaymentResponseAndCallback();
                paymentResponseAndCallback.paymentCallbackRequest = paymentCallbackRequest;
                return paymentResponseAndCallback;
            } else {
                oldValue.paymentCallbackRequest = paymentCallbackRequest;
                return oldValue;
            }
        });
    }

    void addRequest(PaymentRequest paymentRequest, Supplier<PaymentResponse> paymenResponse) {
        interactions.compute(paymentRequest, (key, oldValue) -> {
            if (oldValue == null) {
                PaymentGatewayMockBuilder.PaymentResponseAndCallback paymentResponseAndCallback = new PaymentGatewayMockBuilder.PaymentResponseAndCallback();
                paymentResponseAndCallback.paymentResponse = paymenResponse;
                return paymentResponseAndCallback;
            } else {
                oldValue.paymentResponse = paymenResponse;
                return oldValue;
            }
        });
    }
}