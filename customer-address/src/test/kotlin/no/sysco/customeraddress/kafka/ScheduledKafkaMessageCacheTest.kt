package no.sysco.customeraddress.kafka

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest(includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Repository::class])])
internal class ScheduledKafkaMessageCacheTest @Autowired constructor(
    private val scheduledKafkaMessageCache: ScheduledKafkaMessageCache,
    @PersistenceContext private val entityManager: EntityManager
) {

    private val whitespaceRegex = Regex("(\\s+)")

    private val getAllMessages =
        """
            SELECT p FROM ScheduledKafkaMessages p
        """.replace(whitespaceRegex, " ")

    private fun getNumberOfScheduledKafkaMessages() = entityManager.createQuery(getAllMessages).resultList.size

    //this is tested in other tests as well but just to make it more explicit
    @Test
    fun `processed field of scheduled kafka messages are false by default`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        assertAll(
            { assertEquals(1, scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().size) },
            { assertEquals(1, getNumberOfScheduledKafkaMessages()) }
        )
    }

    @Test
    fun `scheduled kafka message is inserted when no entries have same customer_id`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        val unprocessedCustomerAddressUpdates = scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates()
        assertAll(
            { assertEquals(1, unprocessedCustomerAddressUpdates.size) },
            { assertEquals(scheduledKafkaMessage, unprocessedCustomerAddressUpdates.first()) }
        )
    }

    @Test
    fun `multiple scheduled messages are inserted into cache given their customer ids are unique`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        val scheduledKafkaMessage2 = ScheduledKafkaMessages(
            customerId = "valid_id_2",
            email = "testy@cegal.com",
            physicalAddress = "TD Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage2)
        val unprocessedCustomerAddressUpdates = scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates()
        assertAll(
            { assertEquals(2, unprocessedCustomerAddressUpdates.size) },
            { assertTrue(unprocessedCustomerAddressUpdates.contains(scheduledKafkaMessage)) },
            { assertTrue(unprocessedCustomerAddressUpdates.contains(scheduledKafkaMessage2)) }
        )
    }

    @Test
    fun `scheduled kafka messages updates instead of inserts when an entry in cache has the same customer_id`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "popola@sysco.com",
            physicalAddress = "Madison Square Garden"
        )
        val updatedScheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "devola@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(updatedScheduledKafkaMessage)
        val unprocessedCustomerAddressUpdates = scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates()
        assertAll(
            { assertEquals(1, unprocessedCustomerAddressUpdates.size) },
            { assertEquals(1, getNumberOfScheduledKafkaMessages()) },
            { assertEquals(updatedScheduledKafkaMessage, unprocessedCustomerAddressUpdates.first()) }
        )
    }

    @Test
    fun `processed field of ScheduledKafkaMessage is correctly updated`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Machine Village"
        )
        val updatedScheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Amusement Park"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().first().processed = true
        assertAll(
            { assertEquals(0, scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().size) },
            { assertEquals(1, getNumberOfScheduledKafkaMessages()) }
        )

        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(updatedScheduledKafkaMessage)
        assertAll(
            { assertEquals(1, scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().size) },
            { assertEquals(1, getNumberOfScheduledKafkaMessages()) }
        )
    }

    @Test
    fun `purgeScheduledKafkaCache() works`() {
        val scheduledKafkaMessage = ScheduledKafkaMessages(
            customerId = "valid_id",
            email = "popola@sysco.com",
            physicalAddress = "Madison Square Garden"
        )
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(scheduledKafkaMessage)
        assertEquals(1, scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().size)

        scheduledKafkaMessageCache.getUnprocessedCustomerAddressUpdates().first().processed = true
        assertEquals(1, getNumberOfScheduledKafkaMessages())

        scheduledKafkaMessageCache.purgeScheduledKafkaMessageCache()
        assertEquals(0, getNumberOfScheduledKafkaMessages())
    }


}