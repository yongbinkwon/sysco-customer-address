package dto

data class CustomerAddressDto(
    val customerId: String,
    val email: String,
    val physicalAddress: String

) {
    companion object {
        val EMAIL_PATTERN = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    }
}
