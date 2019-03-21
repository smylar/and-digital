# and-digital
Response to exercise

This is a Spring Boot - JPA application written to given instructions (PDF in project root)

When started it will run on port 3050, if you navigate to http://localhost:3050/and-digital/swagger-ui.html you will find the endpoints that are available and a way to test them.

The app will load the data within init.sql within the resources folder, so you can play about with the data there. It loads that data to a h2 database (normally used in integration testing), you will not need to install anything for it.

I have followed this approach being the closest to what it might be in the real world and should keep quite close to the keep it simple directive.

This was also built with Java 11, it's the first Spring Boot app I've done with Java 11 and there were a couple of times where I had to do a Maven Force Update, hopefully you won't have a problem. The Integation test failsafe also didn't seem to work, so renamed with a Tests suffix so it gets picked up by surefire when doing a maven test or build.
