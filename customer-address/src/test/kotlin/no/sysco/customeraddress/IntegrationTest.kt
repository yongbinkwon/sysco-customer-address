package no.sysco.customeraddress

import no.sysco.customeraddress.kafka.ScheduledKafkaMessageCache
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [CustomerAddressController::class])
internal class IntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockBean
    private lateinit var scheduledKafkaMessageCacheMock: ScheduledKafkaMessageCache

    @Test
    internal fun `valid payload is correctly deserialized to dto`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/customer/address")
                .queryParam("id", "valid_id")
                .content(IntegrationTest::class.java.getResource("/ValidPayload.json")?.readText() ?: "")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

    }
}
