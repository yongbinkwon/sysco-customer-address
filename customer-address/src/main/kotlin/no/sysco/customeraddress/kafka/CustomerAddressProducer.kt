package no.sysco.customeraddress.kafka

import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.common.serialization.StringSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.*
import no.sysco.customeraddress.avro.CustomerAddress
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*

class CustomerAddressProducer {

    private val producer = KafkaProducer<String, CustomerAddress>(
        Properties().apply {
            this[CLIENT_ID_CONFIG] = "customer-address"
            this[BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
            this[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            this[VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
            this[SCHEMA_REGISTRY_URL_CONFIG] = "localhost:8081"
            this[ACKS_CONFIG] = "all"
        }
    )
}