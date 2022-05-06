package no.sysco.customeraddress.kafka

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@DataJpaTest(includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Repository::class])])
internal class ScheduledKafkaMessageCacheTest @Autowired constructor(
    private val scheduledKafkaMessageCache: ScheduledKafkaMessageCache,
    private val entityManager: EntityManager
) {

    @Test
    fun `scheduled kafka message is inserted when no entries have same customer_id`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        assertEquals(scheduledKafkaMessage, entityManager.find(ScheduledKafkaMessages::class.java, "valid_id"))
    }

}