package io.krosslab.enhealthchecker

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter

@Configuration
class ApiConfig {
    @Bean
    fun stringMessageConverter(): StringHttpMessageConverter {
        return StringHttpMessageConverter()
    }
}