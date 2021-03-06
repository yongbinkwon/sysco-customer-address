package no.sysco.customer

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.persistence.*

//should just use crud or jparepo here
@Repository
internal class CustomerRepository(
    @PersistenceContext private val entityManager: EntityManager
) {

    @Transactional
    fun insertIntoCustomerRepository(
        customerId: String,
        email: String,
        physicalAddress: String
    ) {
        entityManager.find(Customers::class.java, customerId)?.apply {
            this.email = email
            this.physicalAddress = physicalAddress
            this.lastUpdated = ZonedDateTime.now()
        } ?: entityManager.persist(
            Customers(
                customerId = customerId,
                email = email,
                physicalAddress = physicalAddress
            )
        )
    }

}

@Entity
@Table(name = "CUSTOMERS")
internal open class Customers(
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
) {
    //not sure whether to include created and lastUpdated here
    override fun equals(other: Any?) =
        other is Customers &&
                customerId == other.customerId &&
                email == other.email &&
                physicalAddress == other.physicalAddress

    //autogenerated. not sure whether to include created and lastUpdated
    override fun hashCode(): Int {
        var result = customerId.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + physicalAddress.hashCode()
        return result
    }
}