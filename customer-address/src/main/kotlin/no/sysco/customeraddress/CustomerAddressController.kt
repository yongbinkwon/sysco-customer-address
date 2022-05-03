package no.sysco.customeraddress

import no.sysco.customeraddress.dto.CustomerAddressDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer/address")
class CustomerAddressController {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateCustomerAddress(
        @RequestParam(name = "id", required = true) customerId: String,
        @RequestBody customerAddress: CustomerAddressDto
    ): ResponseEntity<String> {
        return ResponseEntity("yay", HttpStatus.OK)
    }

}