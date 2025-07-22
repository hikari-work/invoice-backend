package org.yann.integerasiorderkuota.orderkuota.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yann.integerasiorderkuota.orderkuota.interceptor.HistoryInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final HistoryInterceptor historyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(historyInterceptor)
                .addPathPatterns("/api/history/**")
                .addPathPatterns("/api/mutasi/**");
    }
}
