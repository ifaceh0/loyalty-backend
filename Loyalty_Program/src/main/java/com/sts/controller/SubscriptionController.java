//package com.sts.controller;
//
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.model.Invoice;
//import com.stripe.net.Webhook;
//import com.sts.dto.PlanChangeRequestDto;
//import com.sts.dto.SubscriptionRequestDto;
//import com.sts.dto.SubscriptionResponseDto;
//import com.sts.service.SubscriptionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/subscriptions")
//public class SubscriptionController {
//    @Autowired
//    private SubscriptionService subscriptionService;
//
//    @PostMapping("/ceateSubscription")
//    public ResponseEntity<SubscriptionResponseDto> subscribe(@RequestBody SubscriptionRequestDto dto) {
//        return ResponseEntity.ok(subscriptionService.createSubscription(dto));
//    }
//
//    // @PostMapping("/api/stripe/webhook")
//    // public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
//    //                                                 @RequestHeader("Stripe-Signature") String sigHeader) {
//    //     String endpointSecret = "whsec_..."; // From Stripe CLI or Dashboard
//    //     Event event;
//
//    //     try {
//    //         event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
//    //     } catch (SignatureVerificationException e) {
//    //         return ResponseEntity.badRequest().build();
//    //     }
//
//    //     switch (event.getType()) {
//    //         case "invoice.paid" -> handleInvoicePaid(event);
//    //         case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
//    //         // Add more events as needed
//    //     }
//
//    //     return ResponseEntity.ok("Webhook received");
//    // }
//
//    // private void handleInvoicePaid(Event event) {
//    //     Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().get();
//    //     String subscriptionId = invoice.getSubscription();
//    //     // Update subscription status to ACTIVE
//    // }
//
//    // private void handleSubscriptionDeleted(Event event) {
//    //     Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().get();
//    //     String subId = subscription.getId();
//    //     // Update your database to mark as CANCELLED
//    // }
//
//
//    @PutMapping("/cancelSubscription")
//    public ResponseEntity<String> cancelSubscription(@RequestParam Long shopId) {
//        try {
//            String result = subscriptionService.cancelSubscription(shopId);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/change-plan")
//    public ResponseEntity<String> changeSubscriptionPlan(@RequestBody PlanChangeRequestDto dto) {
//        String result = subscriptionService.schedulePlanChange(dto);
//        return ResponseEntity.ok(result);
//    }
//
//}
