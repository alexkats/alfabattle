package com.katsman.alfabattle.task1

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode

@Configuration
class Task1Configuration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().also {
            it.registerModule(ParameterNamesModule())
            it.registerModule(Jdk8Module())
            it.registerModule(SimpleModule().also {
                it.addSerializer(BigDecimal::class.java, object : JsonSerializer<BigDecimal>() {
                    override fun serialize(value: BigDecimal, gen: JsonGenerator, provider: SerializerProvider) {
                        gen.writeString(value.setScale(2, RoundingMode.HALF_EVEN).toString())
                    }
                })
            })
            it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}