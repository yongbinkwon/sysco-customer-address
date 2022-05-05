package no.sysco.customeraddress

import no.sysco.customeraddress.dto.CustomerAddressDto
import no.sysco.customeraddress.kafka.ScheduledKafkaMessageCache
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer/address")
internal class CustomerAddressController(
    @Value("\${http-ok-message.suspicious-email}")
    private val suspiciousEmailMsg: String,

    @Value("\${http-ok-message.valid}")
    private val validMsg: String,

    private val scheduledKafkaMessageCache: ScheduledKafkaMessageCache
) {

    companion object {
        private val log = LoggerFactory.getLogger(CustomerAddressController::class.java)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    internal fun updateCustomerAddress(
        @RequestParam(name = "id", required = true) customerId: String,
        @RequestBody customerAddress: CustomerAddressDto
    ): ResponseEntity<String> {
        log.info("POST REQUEST RECEIVED FOR CUSTOMER ID: $customerId")
        scheduledKafkaMessageCache.scheduleCustomerAddressUpdate(
            customerAddress.toScheduledKafkaMessage(customerId)
        )
        if(customerAddress.suspiciousMail()) {
            return ResponseEntity(suspiciousEmailMsg, HttpStatus.OK)
        }
        return ResponseEntity(validMsg, HttpStatus.OK)
    }

}