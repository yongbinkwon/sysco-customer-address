package no.sysco.customer.kafka

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.*

@Configuration
@EnableKafka
internal class KafkaConsumerConfig {

    @Bean(name = ["customerAddressContainerFactory"])
    internal fun customerAddressContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GenericRecord>? =

        ConcurrentKafkaListenerContainerFactory<String, GenericRecord>().apply {
            setConcurrency(1)
            consumerFactory = DefaultKafkaConsumerFactory(defaultConsumerConfigs())
            containerProperties.pollTimeout = Long.MAX_VALUE
        }

    private fun defaultConsumerConfigs() =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "http://localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "sysco-customer-v1",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java,
            SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:8081"
        )

}