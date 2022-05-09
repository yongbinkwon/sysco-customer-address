#./gradlew clean build not sure if needed
docker-compose down
docker-compose up -d
docker-compose exec broker kafka-topics --create --bootstrap-server broker:9092 --replication-factor 1 --partitions 1 --topic sysco-customer-address-v1