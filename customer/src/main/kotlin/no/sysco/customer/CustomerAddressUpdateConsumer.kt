package no.sysco.customer

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CustomerAddressUpdateConsumer {

    companion object {
        private val log = LoggerFactory.getLogger(CustomerAddressUpdateConsumer::class.java)
    }

    @KafkaListener(
        topics = ["sysco-customer-address-v1"],
        containerFactory = "customerAddressContainerFactory"
    )
    fun receive(record: ConsumerRecord<String, GenericRecord?>) {
        log.info("RECEIVED CUSTOMER ADDRESS UPDATE")
    }
}