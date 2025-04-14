package com.example.hotel.controller;

import com.example.hotel.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/availability")
    public ResponseEntity<?> getAvailability(
            @RequestParam String hotelId,
            @RequestParam String roomType,
            @RequestParam String dateRange) {
        try {
            int availability = hotelService.checkAvailability(hotelId, roomType, dateRange);
            return ResponseEntity.ok(availability); // HTTP 200 with availability count
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400 for invalid input
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage()); // HTTP 500 for unexpected errors
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAvailability(
            @RequestParam String hotelId,
            @RequestParam int daysAhead,
            @RequestParam String roomType) {
        try {
            List<String> results = hotelService.searchAvailability(hotelId, daysAhead, roomType);
            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No availability found."); // HTTP 404 if no results
            }
            return ResponseEntity.ok(results); // HTTP 200 with search results
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400 for invalid input
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage()); // HTTP 500 for unexpected errors
        }
    }
}