package com.example.hotel.util;

import com.example.hotel.model.Booking;
import com.example.hotel.model.Hotel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public List<Hotel> loadHotels(String filePath) throws Exception {
        logger.info("Loading hotels from file: {}", filePath);
        ObjectMapper mapper = new ObjectMapper();
        return List.of(mapper.readValue(new File(filePath), Hotel[].class));
    }

    public List<Booking> loadBookings(String filePath) throws Exception {
        logger.info("Loading bookings from file: {}", filePath);
        ObjectMapper mapper = new ObjectMapper();
        return List.of(mapper.readValue(new File(filePath), Booking[].class));
    }
}