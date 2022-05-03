package no.sysco.customeraddress

import no.sysco.customeraddress.dto.CustomerAddressDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerAddressControllerTest {

    @LocalServerPort
    private var port: Int = 0

    private val restTemplate = TestRestTemplate()

    private val baseUrl = "http://localhost:$port"

    private val customerAddressUrl = "$baseUrl/customer/address"


    @Test
    fun `valid payload is correctly deserialized to dto`() {
        val payload = CustomerAddressDto(email = "test@mail.com", physicalAddress = "test street 123C")
        Assertions.assertEquals(ResponseEntity<HttpStatus>(HttpStatus.OK), restTemplate.postForEntity<HttpStatus>(customerAddressUrl, payload, mapOf("id" to "valid_id")))
    }

    @Test
    fun `payload missing any field returns 400 bad request`() {

    }

    @Test
    fun `invalid email returns 200 with a warning message`() {

    }
}