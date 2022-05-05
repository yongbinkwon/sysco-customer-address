package no.sysco.customer.kafka

import no.sysco.customer.CustomerRepository
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
internal class CustomerAddressUpdateConsumer(
    private val customerRepository: CustomerRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(CustomerAddressUpdateConsumer::class.java)
    }

    //record-value cant be null
    @KafkaListener(
        topics = ["sysco-customer-address-v1"],
        containerFactory = "customerAddressContainerFactory"
    )
    fun receive(customerUpdate: ConsumerRecord<String, GenericRecord>) {
        log.info("RECEIVED CUSTOMER ADDRESS UPDATE")
        val updatedValues = customerUpdate.value()
        customerRepository.insertIntoCustomerRepository(
            customerId = customerUpdate.key(),
            email = updatedValues.email(),
            physicalAddress = updatedValues.physicalAddress()
        )
    }

    fun GenericRecord.email() = get("email").toString()
    fun GenericRecord.physicalAddress() = get("physicalAddress").toString()
}