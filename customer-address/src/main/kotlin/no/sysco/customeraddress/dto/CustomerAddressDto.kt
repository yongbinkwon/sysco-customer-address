package no.sysco.customeraddress.dto

data class CustomerAddressDto(
    val email: String,
    val physicalAddress: String

) {
    companion object {
        val EMAIL_PATTERN = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    }
}
