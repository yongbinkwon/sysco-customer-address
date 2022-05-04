package no.sysco.customeraddress

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerAddressControllerTest(
    @Value("\${local.server.port}")
    val port: Int,

    @Value("\${http-ok-message.valid}")
    private val validMsg: String,

    @Value("\${http-ok-message.suspicious-email}")
    private val suspiciousEmailMsg: String

) {

    private val restTemplate = TestRestTemplate()

    private val baseUrl = "http://localhost:$port"

    private val customerAddressUrl = "$baseUrl/customer/address"

    private val validUri = UriComponentsBuilder.fromHttpUrl(customerAddressUrl)
        .queryParam("id", "valid_id")
        .toUriString()


    @Test
    fun `valid payload is correctly deserialized to dto`() {
        val payload = mapOf(
            "email" to "test@sysco.com",
            "physicalAddress" to "Vollsveien 2B"
        )
        assertAll(
            {
                assertEquals(
                    HttpStatus.OK,
                    restTemplate.postForEntity<String>(validUri, payload).statusCode
                )
            },
            {
                assertEquals(
                    validMsg,
                    restTemplate.postForEntity<String>(validUri, payload).body
                )
            },
        )
    }

    @Test
    fun `payload missing any field returns 400 bad request`() {
        val payloadWithoutPhysicalAddress = mapOf("email" to "invalid@sysco.com")
        val payloadWithoutEmailAddress = mapOf("physicalAddress" to "Høgskoleringen 1")

        assertAll(
            {
                assertEquals(
                    HttpStatus.BAD_REQUEST,
                    restTemplate.postForEntity<String>(validUri, payloadWithoutPhysicalAddress).statusCode
                )
            },
            {
                assertEquals(
                    HttpStatus.BAD_REQUEST,
                    restTemplate.postForEntity<String>(validUri, payloadWithoutEmailAddress).statusCode
                )
            },
        )
    }

    @Test
    fun `invalid email returns 200 with a warning message`() {
        val payload = mapOf(
            "email" to "invalidmail.com",
            "physicalAddress" to "Vollsveien 2B"
        )
        assertAll(
            {
                assertEquals(
                    HttpStatus.OK,
                    restTemplate.postForEntity<String>(validUri, payload).statusCode
                )
            },
            {
                assertEquals(
                    suspiciousEmailMsg,
                    restTemplate.postForEntity<String>(validUri, payload).body
                )
            },
        )
    }

    @Test
    fun `no id in query param returns 400 bad request`() {
        val payload = mapOf(
            "email" to "test@sysco.com",
            "physicalAddress" to "Vollsveien 2B"
        )

        assertEquals(
            HttpStatus.BAD_REQUEST,
            restTemplate.postForEntity<String>(customerAddressUrl, payload).statusCode
        )

    }


}