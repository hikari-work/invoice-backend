package org.yann.integerasiorderkuota.bot.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/telegram")
    public ResponseEntity<String> telegramWebhook() {
        return ResponseEntity.ok("OK");
    }
}
