package no.sysco.customer

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
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
        entityManager.persist(
            Customer(customerId, email, physicalAddress)
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
    open var physicalAddress: String = ""
)