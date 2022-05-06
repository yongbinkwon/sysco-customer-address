package no.sysco.customeraddress.kafka

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.*


@Repository
internal class ScheduledKafkaMessageCache(
    @PersistenceContext private val entityManager: EntityManager
) {
    private val whitespaceRegex = Regex("(\\s+)")

    private val getUnprocessed =
        """
            SELECT p FROM ScheduledKafkaMessages p
            WHERE p.processed = FALSE
        """.replace(whitespaceRegex, " ")

    @Transactional(readOnly = true)
    fun getUnprocessedCustomerAddressUpdates(): List<ScheduledKafkaMessages> =
        entityManager.createQuery(getUnprocessed, ScheduledKafkaMessages::class.java)
            .resultList

    @Transactional
    fun scheduleCustomerAddressUpdate(updatedCustomerAddress: ScheduledKafkaMessages) {
        entityManager.find(
            ScheduledKafkaMessages::class.java,
            updatedCustomerAddress.customerId
        )?.apply {
            if (updatedCustomerAddress isDifferentTo this) {
                email = updatedCustomerAddress.email
                physicalAddress = updatedCustomerAddress.physicalAddress
                processed = false
            }
        } ?: entityManager.persist(updatedCustomerAddress)
    }

    private val deleteProcessed =
        """
            DELETE ScheduledKafkaMessages p
            WHERE p.processed = TRUE
        """.replace(whitespaceRegex, " ")

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    fun purgeScheduledKafkaMessageCache() {
        entityManager.createQuery(deleteProcessed).executeUpdate()
    }

}

@Entity
@Table(name = "SCHEDULED_KAFKA_MESSAGES")
internal open class ScheduledKafkaMessages(
    @Id
    @Column(name = "CUSTOMER_ID")
    open val customerId: String = "",

    @Column(name = "EMAIL")
    open var email: String = "",

    @Column(name = "PHYSICAL_ADDRESS")
    open var physicalAddress: String = "",

    @Column(name = "PROCESSED")
    open var processed: Boolean = false
) {
    override fun equals(other: Any?) =
        other is ScheduledKafkaMessages &&
                customerId == other.customerId &&
                email == other.email &&
                physicalAddress == other.physicalAddress &&
                processed == other.processed

    infix fun isDifferentTo(other: ScheduledKafkaMessages) =
        email != other.email || physicalAddress != other.physicalAddress
}
