# How run the Calendar Application

First you need to run a database in postgres that is already configured with docker compose file `docker-compose.yml` that you can find in the root folder of this project.
To see more info about how to use docker compose see https://docs.docker.com/compose

Second run the Spring Boot application more information on how to run can be found on the spring framework site:
https://spring.io/guides/gs/spring-boot

# How to test the Calendar Application
Once the application is running you can do some tests with the Postman tool that you can download at https://www.postman.com.

Then open Postman and import the file `Calendar demo.postman_collection.json` to see all the tests you can do to the API.

# What are the requirements for the Calendar Application
To test the application at least you must know the requirements which are the following:

- Ability to check and show free time slots
- To book appointments. 
- Appointments have two allowed durations: 60 minutes and 15 minutes.
- Only one appointment is permitted at a given time slot.
- The appointment could be booked during the working hours of the day
- To cancel existing appointment and free the slot for further bookings.

