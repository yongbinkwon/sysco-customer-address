package no.sysco.customeraddress


/*
@Import(CustomerAddressControllerTestConfig::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CustomerAddressControllerTest(
    @Value("\${local.server.port}")
    private val port: Int,

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

    @Autowired
    @MockK(relaxUnitFun = true)
    lateinit var mockScheduledKafkaMessageCache: ScheduledKafkaMessageCache

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    internal fun `valid payload is correctly deserialized to dto`() {
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
    internal fun `payload missing any field returns 400 bad request`() {
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
    internal fun `invalid email returns 200 with a warning message`() {
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
    internal fun `no id in query param returns 400 bad request`() {
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

 */