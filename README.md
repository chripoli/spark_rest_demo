# README

This demo is used to demonstrates an Apache Spark application, in which data is retrieved from a REST endpoint for further calculation.
This project is fully runnable since everything is created in a docker-compose.yml file.

Within the docker-compose.yml the following services are included:

* Spark Master
* Three Spark slaves, which represent the workers.
* One WireMock node, which serves as the primary datasource.
* One driver program, which is responsible for executing the Spark program.

In order to run the program, do the following:

* Build the Maven project found under the spark_demo folder with `mvn package`
* Afterwards, build and execute the docker compose ensemble with `docker-compose up`