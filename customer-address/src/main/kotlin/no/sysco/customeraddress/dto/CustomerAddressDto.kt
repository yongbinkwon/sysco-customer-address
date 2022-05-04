package no.sysco.customeraddress.dto

data class CustomerAddressDto(
    val email: String,
    val physicalAddress: String

) {
    companion object {
        private val EMAIL_PATTERN = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    }

    fun suspiciousMail() = !EMAIL_PATTERN.containsMatchIn(email)
}
