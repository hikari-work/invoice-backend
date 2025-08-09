package org.yann.integerasiorderkuota.orderkuota.service;

import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.orderkuota.dto.CallbackDTO;

@Service
public class SendCallbackService {

    private static final Logger log = LoggerFactory.getLogger(SendCallbackService.class);
    private final Retry retry;
    private final RestTemplateBuilder restTemplateBuilder;

    public SendCallbackService(Retry retry, RestTemplateBuilder restTemplateBuilder) {
        this.retry = retry;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public void sendCallback(String url, CallbackDTO data) {
        Runnable callbackTask = Retry.decorateRunnable(retry, () -> {
           restTemplateBuilder.build().postForEntity(url, data, String.class);
           log.info("Callback sent successfully to: {}", url);
        });
        try {
            callbackTask.run();
        } catch (Exception e) {
            log.error("Failed to send callback to {}: {}", url, e.getMessage());
        }
    }

}
