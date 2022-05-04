package no.sysco.customeraddress.kafka

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
internal class CustomerAddressPublisher(
    private val scheduledKafkaMessageCache: ScheduledKafkaMessageCache,
    private val customerAddressProducer: CustomerAddressUpdateProducer
) {

    @Scheduled(initialDelay = 1000*60*1, fixedDelay = 1000*60*2)
    fun publishCustomerAddressUpdates() {
        val unprocessedCustomerAddressUpdates = scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates()
        unprocessedCustomerAddressUpdates.forEach {
            it.processed = customerAddressProducer.publishCustomerAddressUpdate(it)
        }
    }

}