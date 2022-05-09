package no.sysco.customer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest(includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Repository::class])])
internal class CustomerRepositoryTest @Autowired constructor(
    private val customerRepository: CustomerRepository,
    @PersistenceContext private val entityManager: EntityManager
) {

    private val whitespaceRegex = Regex("(\\s+)")

    private val getAllMessages =
        """
            SELECT p FROM Customers p
        """.replace(whitespaceRegex, " ")

    private fun getNumberOfCustomerEntries() = entityManager.createQuery(getAllMessages).resultList.size
    private fun getCustomerFromId(customerId: String) = entityManager.find(Customers::class.java, customerId)

    @Test
    fun `customer is inserted when no entries have same customer_id`() {
        val customer = Customers(
            customerId = "valid_id",
            email = "email@gmail.com",
            physicalAddress = "Vollsveien 2B"
        )
        customerRepository.insertIntoCustomerRepository(
            customerId = customer.customerId,
            email = customer.email,
            physicalAddress = customer.physicalAddress
        )
        assertAll(
            { assertEquals(customer, getCustomerFromId(customer.customerId)) },
            { assertEquals(1, getNumberOfCustomerEntries()) }
        )
    }

    @Test
    fun `multiple scheduled messages are inserted into cache given their customer ids are unique`() {
        val customer1 = Customers(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        val customer2 = Customers(
            customerId = "valid_id_2",
            email = "testy@cegal.com",
            physicalAddress = "TD Garden"
        )
        val customers = listOf(customer1, customer2)
        customers.forEach {
            customerRepository.insertIntoCustomerRepository(
                customerId = it.customerId,
                email = it.email,
                physicalAddress = it.physicalAddress
            )
        }

        assertAll(
            { assertEquals(2, getNumberOfCustomerEntries()) },
            { assertEquals(customer1, getCustomerFromId(customer1.customerId)) },
            { assertEquals(customer2, getCustomerFromId(customer2.customerId)) },
        )
    }

    @Test
    fun `scheduled kafka messages updates instead of inserts when an entry in cache has the same customer_id`() {
        val customer1 = Customers(
            customerId = "valid_id",
            email = "hello@cegal.com",
            physicalAddress = "Madison Square Garden"
        )
        val customer2 = Customers(
            customerId = "valid_id",
            email = "testy@cegal.com",
            physicalAddress = "TD Garden"
        )
        val customers = listOf(customer1, customer2)
        customers.forEach {
            customerRepository.insertIntoCustomerRepository(
                customerId = it.customerId,
                email = it.email,
                physicalAddress = it.physicalAddress
            )
        }

        assertAll(
            { assertEquals(1, getNumberOfCustomerEntries()) },
            { assertEquals(customer2, getCustomerFromId(customer1.customerId)) }
        )
    }
}