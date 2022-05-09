# CEGAL PRACTICAL

http://localhost:8080/swagger-ui/ for swagger

To run the program locally:
* go to project root (sysco-customer-address)
* run "./gradlew build"
* run "./pipeline.sh" and just let it finish running. Connection could not be established-error might/will pop up but this is due to broker not being up yet while the script attempts to create a kafka topic, it will resolve itself.
* run "./gradlew bootRun"
* you can now do post requests on "localhost:8080/customer/address". Details are found in the swagger.
