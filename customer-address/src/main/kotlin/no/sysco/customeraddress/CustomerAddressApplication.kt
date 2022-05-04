package no.sysco.customeraddress

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class CustomerAddressApplication

fun main(args: Array<String>) {
    runApplication<CustomerAddressApplication>(*args)
}