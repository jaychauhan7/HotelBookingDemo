package com.example.hotel;

import com.example.hotel.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelReservationApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(HotelReservationApplication.class);

    private final HotelService hotelService;

    public HotelReservationApplication(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HotelReservationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String hotelsFile = null;
        String bookingsFile = null;

        // Parse command-line arguments
        for (String arg : args) {
            if (arg.startsWith("--hotels=")) {
                hotelsFile = arg.substring("--hotels=".length());
            } else if (arg.startsWith("--bookings=")) {
                bookingsFile = arg.substring("--bookings=".length());
            }
        }

        // Validate arguments
        if (hotelsFile == null || bookingsFile == null) {
            logger.error("Missing required arguments. Usage: myapp --hotels=<hotels.json> --bookings=<bookings.json>");
            System.exit(1);
        }

        logger.info("Loading hotels from: {}", hotelsFile);
        logger.info("Loading bookings from: {}", bookingsFile);

        // Initialize the service with the provided file paths
        hotelService.loadData(hotelsFile, bookingsFile);

        logger.info("Application started successfully!");
    }
}