package no.sysco.customeraddress.dto

import no.sysco.customeraddress.kafka.ScheduledKafkaMessage

internal data class CustomerAddressDto(
    val email: String,
    val physicalAddress: String

) {
    companion object {
        private val EMAIL_PATTERN = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    }

    internal fun suspiciousMail() = !EMAIL_PATTERN.containsMatchIn(email)

    internal fun toScheduledKafkaMessage(customerId: String) =
        ScheduledKafkaMessage(
            customerId = customerId,
            email = email,
            physicalAddress = physicalAddress,
        )
}
