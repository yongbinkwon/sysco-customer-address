package no.sysco.customeraddress

import no.sysco.customeraddress.kafka.ScheduledKafkaMessageCache
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [CustomerAddressController::class])
internal class CustomerAddressControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,

    @Value("\${http-ok-message.suspicious-email}")
    private val suspiciousEmailMsg: String,

    @Value("\${http-ok-message.valid}")
    private val validMsg: String,
) {

    @MockBean
    private lateinit var scheduledKafkaMessageCacheMock: ScheduledKafkaMessageCache

    private fun MockMvc.mockPostCall(
        payloadPath: String,
        customerId: String? = "valid_id",
    ) = perform(
        MockMvcRequestBuilders
            .post("/customer/address")
            .queryParam("id", customerId)
            .content(CustomerAddressControllerTest::class.java.getResource(payloadPath)?.readText() ?: "")
            .contentType(MediaType.APPLICATION_JSON)
    )

    @Test
    internal fun `valid payload is correctly deserialized to dto`() {
        mockMvc.mockPostCall("/ValidPayload.json")
            .andExpect(status().isOk)
            .andExpect(content().string(validMsg))
    }

    @Test
    internal fun `payload missing any field returns 400 bad request`() {
        mockMvc.mockPostCall("/InvalidPayloadNoEmail.json")
            .andExpect(status().isBadRequest)

        mockMvc.mockPostCall("/InvalidPayloadNoPhysicalAddress.json")
            .andExpect(status().isBadRequest)
    }

    @Test
    internal fun `invalid email returns 200 with a warning message`() {
        mockMvc.mockPostCall("/ValidPayloadTypoEmail.json")
            .andExpect(status().isOk)
            .andExpect(content().string(suspiciousEmailMsg))
    }

    @Test
    internal fun `no id in query param returns 400 bad request`() {
        mockMvc.mockPostCall("/ValidPayload.json", null)
            .andExpect(status().isBadRequest)

    }
}
