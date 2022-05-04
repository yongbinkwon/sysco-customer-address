package no.sysco.customeraddress.kafka

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.*


@Repository
class ScheduledKafkaMessageCache(
    @PersistenceContext private val entityManager: EntityManager
) {
    private val whitespaceRegex =  Regex("(\\s+)")

    private val getUnprocessed =
        """
            SELECT p FROM ScheduledKafkaMessages p
            WHERE p.processed = FALSE
        """.replace(whitespaceRegex, " ")

    @Transactional(readOnly = true)
    fun getUnprocessedCustomerAddressUpdates() =
        entityManager.createQuery(getUnprocessed, ScheduledKafkaMessage::class.java)
            .resultList

    @Transactional
    fun scheduleCustomerAddressUpdate(scheduledKafkaMessage: ScheduledKafkaMessage) {
        scheduledKafkaMessage.let {
            entityManager.find(ScheduledKafkaMessage::class.java, it.customerId).apply {
                email = it.email
                physicalAddress = it.physicalAddress
                processed = false
            } ?: entityManager.persist(scheduledKafkaMessage)
        }
    }

}

@Entity
@Table(name = "SCHEDULED_KAFKA_MESSAGES")
open class ScheduledKafkaMessage(
    @Id
    @Column(name = "CUSTOMER_ID")
    val customerId: String = "",

    @Column(name = "EMAIL")
    var email: String = "",

    @Column(name = "PHYSICAL_ADDRESS")
    var physicalAddress: String = "",

    @Column(name = "PROCESSED")
    var processed: Boolean = false
)
