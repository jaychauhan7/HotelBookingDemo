package com.example.hotel.service;

import com.example.hotel.model.Booking;
import com.example.hotel.model.Hotel;
import com.example.hotel.util.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    private final DataLoader dataLoader;
    private List<Hotel> hotels;
    private List<Booking> bookings;

    public HotelService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Load hotel and booking data from the specified files.
     *
     * @param hotelsFile   Path to the hotels JSON file.
     * @param bookingsFile Path to the bookings JSON file.
     */
    public void loadData(String hotelsFile, String bookingsFile) {
        try {
            logger.info("Loading hotels from file: {}", hotelsFile);
            this.hotels = dataLoader.loadHotels(hotelsFile);

            logger.info("Loading bookings from file: {}", bookingsFile);
            this.bookings = dataLoader.loadBookings(bookingsFile);

            logger.info("Loaded {} hotels and {} bookings", hotels.size(), bookings.size());
        } catch (Exception e) {
            logger.error("Failed to load data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load data", e);
        }
    }

    /**
     * Check availability for a specific hotel, room type, and date range.
     *
     * @param hotelId   The ID of the hotel.
     * @param roomType  The type of room.
     * @param dateRange The date range (e.g., "20240901" or "20240901-20240903").
     * @return The availability count (can be negative if overbooked).
     */
    public int checkAvailability(String hotelId, String roomType, String dateRange) {
        logger.debug("Checking availability for hotelId: {}, roomType: {}, dateRange: {}", hotelId, roomType, dateRange);

        // Find the hotel by ID
        Hotel hotel = hotels.stream()
                .filter(h -> h.getId().equals(hotelId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Hotel with ID {} not found", hotelId);
                    return new IllegalArgumentException("Hotel not found: " + hotelId);
                });

        // Count total rooms of the specified type
        long totalRooms = hotel.getRooms().stream()
                .filter(r -> r.getRoomType().equals(roomType))
                .count();
        
        logger.info("Total No. of rooms for selected date Range:"+ totalRooms);

        if (totalRooms == 0) {
            logger.warn("No rooms of type {} found in hotel {}", roomType, hotelId);
            throw new IllegalArgumentException("No rooms of type " + roomType + " found in hotel " + hotelId);
        }

        // Parse the date range
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String[] dates = dateRange.split("-");
        LocalDate startDate = LocalDate.parse(dates[0], formatter);
        LocalDate endDate = dates.length > 1 ? LocalDate.parse(dates[1], formatter) : startDate;

        // Count booked rooms for the specified date range
        long bookedRooms = bookings.stream()
                .filter(b -> b.getHotelId().equals(hotelId) && b.getRoomType().equals(roomType))
                .filter(b -> {
                    LocalDate arrival = LocalDate.parse(b.getArrival(), formatter);
                    LocalDate departure = LocalDate.parse(b.getDeparture(), formatter);
                    return !(departure.isBefore(startDate) || arrival.isAfter(endDate));
                })
                .count();
        
        logger.info("Total No. of booked rooms for selected date Range:"+ bookedRooms);

        int availability = (int) (totalRooms - bookedRooms);
        logger.info("Availability for hotelId: {}, roomType: {}, dateRange: {} is {}", hotelId, roomType, dateRange, availability);
        return availability;
    }

    /**
     * Search for availability in the next specified number of days.
     *
     * @param hotelId   The ID of the hotel.
     * @param daysAhead The number of days ahead to search.
     * @param roomType  The type of room.
     * @return A list of date ranges and availability.
     */
    public List<String> searchAvailability(String hotelId, int daysAhead, String roomType) {
        logger.debug("Searching availability for hotelId: {}, roomType: {}, daysAhead: {}", hotelId, roomType, daysAhead);

        // Find the hotel by ID
        Hotel hotel = hotels.stream()
                .filter(h -> h.getId().equals(hotelId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Hotel with ID {} not found", hotelId);
                    return new IllegalArgumentException("Hotel not found: " + hotelId);
                });

        // Check if the hotel has rooms of the specified type
        boolean hasRoomType = hotel.getRooms().stream()
                .anyMatch(r -> r.getRoomType().equals(roomType));
        if (!hasRoomType) {
            logger.warn("No rooms of type {} found in hotel {}", roomType, hotelId);
            throw new IllegalArgumentException("No rooms of type " + roomType + " found in hotel " + hotelId);
        }

        // Search for availability in the next specified number of days
        List<String> results = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();

        for (int i = 0; i < daysAhead; i++) {
            LocalDate date = today.plusDays(i);
            String dateStr = date.format(formatter);

            int availability = checkAvailability(hotelId, roomType, dateStr);
            if (availability > 0) {
                results.add("(" + dateStr + ", " + availability + ")");
            }
        }

        logger.info("Search results for hotelId: {}, roomType: {}, daysAhead: {}: {}", hotelId, roomType, daysAhead, results);
        return results;
    }
}