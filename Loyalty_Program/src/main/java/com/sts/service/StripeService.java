package com.sts.service;

import org.springframework.stereotype.Service;

import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;

@Service
public class StripeService {
    public Customer createCustomer(String email, String name) throws Exception {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
        return Customer.create(params);
    }

    public Subscription createStripeSubscription(String customerId, String priceId) throws Exception {
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(
                        SubscriptionCreateParams.Item.builder()
                                .setPrice(priceId)
                                .build()
                )
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .setPaymentSettings(
                        SubscriptionCreateParams.PaymentSettings.builder()
                                .setSaveDefaultPaymentMethod(
                                        SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod.ON_SUBSCRIPTION
                                )
                                .build()
                )
                .build();

        java.util.Map<String, Object> createParams = new java.util.HashMap<>(params.toMap());
        createParams.put("expand", java.util.List.of("latest_invoice.payment_intent"));

        return Subscription.create(createParams, null);
    }
}
