# Device Tracking Processor

This is a spring boot based application that provides a solution to manage different type of smart devices, hubs, 
and space concept(Dwellings). The application provides various REST APIs for the CRUD operations about those entities
and manage the relationship between them.

# Main supported Features:
1. Device Management: CRUD operations for the device. (Device could be Thermostat, lock, light, etc.)
2. Hub Management: CRUD operations for the hubs, as well as the smartDevice pairing and unpairing.
3. Space Management: Supports managing the occupancy level of the dwelling, and pair and unpair the hub with the space.

# Test
1. I wrote a unit test for the SmartDeviceController. It basically test all the endpoints from this controller, you can run
   it directly by any Java IDE.
2. Due to the time limitation, I didn't write the all unit test for hub controller and dwelling controller. But I created those
    test files in the test directory.

# Future Implementation/Improvement
1. This project is currently mainly supports the "infrastructure" level of logic between different main 
   entities. In the future, we might need to complete the main business logic, for example, create different "commandAdapters"
   for issuing different commands to the smart devices.(Set temperature, unlock, change dimmer level.etc.).
2. We could also need to add a real DB layer to store all our data. (Caching layer is also a good to have).
3. We could also add different metrics for our operations and command to better monitor our system.