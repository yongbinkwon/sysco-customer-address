package no.sysco.customeraddress.kafka

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class CustomerAddressPublisher(
    private val scheduledKafkaMessageCache: ScheduledKafkaMessageCache,
    private val customerAddressProducer: CustomerAddressUpdateProducer
) {

    companion object {
        private val log = LoggerFactory.getLogger(CustomerAddressPublisher::class.java)
    }

    @Transactional
    @Scheduled(initialDelay = 1000*20*1, fixedDelay = 1000*20*1)
    fun publishCustomerAddressUpdates() {
        log.info("SCHEDULED TASK STARTED")
        val unprocessedCustomerAddressUpdates = scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates()
        log.info("NUMBER OF UNPROCESSED CUSTOMER ADDRESS UPDATES: ${unprocessedCustomerAddressUpdates.size}")
        unprocessedCustomerAddressUpdates.forEach {
            it.processed = customerAddressProducer.publishCustomerAddressUpdate(it)
        }
    }

}