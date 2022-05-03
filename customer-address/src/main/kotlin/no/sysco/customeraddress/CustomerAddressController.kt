package no.sysco.customeraddress

import no.sysco.customeraddress.dto.CustomerAddressDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer/address")
class CustomerAddressController {

    @PostMapping
    fun updateCustomerAddress(
        @RequestParam(name = "id", required = true) customerId: String,
        @RequestBody customerAddress: CustomerAddressDto
    ) {

    }

}