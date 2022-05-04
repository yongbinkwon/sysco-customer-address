package no.sysco.customeraddress.kafka

import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.common.serialization.StringSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.*
import no.sysco.customeraddress.avro.CustomerAddress
import no.sysco.customeraddress.dto.CustomerAddressDto
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class CustomerAddressUpdateProducer {

    private val producer = KafkaProducer<String, CustomerAddress>(
        Properties().apply {
            this[CLIENT_ID_CONFIG] = "customer-address"
            this[BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:9092"
            this[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            this[VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
            this[SCHEMA_REGISTRY_URL_CONFIG] = "http://localhost:8081"
            this[ACKS_CONFIG] = "all"
        }
    )

    private val topic = "sysco-customer-address-v1"

    internal fun publishCustomerAddressUpdate(scheduledKafkaMessage: ScheduledKafkaMessage): Boolean {
        return try {
            val avroMessage = CustomerAddress(scheduledKafkaMessage.email, scheduledKafkaMessage.physicalAddress)
            producer.send(
                ProducerRecord(
                    topic,
                    scheduledKafkaMessage.customerId,
                    avroMessage
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }

}