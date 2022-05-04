package no.sysco.customeraddress

import no.sysco.customeraddress.dto.CustomerAddressDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer/address")
class CustomerAddressController(
    @Value("\${http-ok-message.suspicious-email}")
    private val suspiciousEmailMsg: String,

    @Value("\${http-ok-message.valid}")
    private val validMsg: String
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateCustomerAddress(
        @RequestParam(name = "id", required = true) customerId: String,
        @RequestBody customerAddress: CustomerAddressDto
    ): ResponseEntity<String> {
        if(customerAddress.suspiciousMail()) {
            return ResponseEntity(suspiciousEmailMsg, HttpStatus.OK)
        }
        return ResponseEntity(validMsg, HttpStatus.OK)
    }

}