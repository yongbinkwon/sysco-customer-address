package no.sysco.customer

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.persistence.*

//should just use crud or jparepo here
@Repository
internal class CustomerRepository(
    private val entityManager: EntityManager
) {

    @Transactional
    fun insertIntoCustomerRepository(
        customerId: String,
        email: String,
        physicalAddress: String
    ) {
        entityManager.find(Customer::class.java, customerId)?.apply {
            this.email = email
            this.physicalAddress = physicalAddress
            this.lastUpdated = ZonedDateTime.now()
        } ?: entityManager.persist(
            Customer(
                customerId = customerId,
                email = email,
                physicalAddress = physicalAddress
            )
        )
    }

}

@Entity
@Table(name = "CUSTOMERS")
internal open class Customer(
    @Id
    @Column(name = "CUSTOMER_ID")
    open val customerId: String = "",

    @Column(name = "EMAIL")
    open var email: String = "",

    @Column(name = "PHYSICAL_ADDRESS")
    open var physicalAddress: String = "",

    @Column(name = "CREATED")
    open val created: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "LAST_UPDATED")
    open var lastUpdated: ZonedDateTime = ZonedDateTime.now()
)