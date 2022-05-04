package no.sysco.customeraddress.dto

import no.sysco.customeraddress.kafka.ScheduledKafkaMessage

data class CustomerAddressDto(
    val email: String,
    val physicalAddress: String

) {
    companion object {
        private val EMAIL_PATTERN = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    }

    fun suspiciousMail() = !EMAIL_PATTERN.containsMatchIn(email)
}
