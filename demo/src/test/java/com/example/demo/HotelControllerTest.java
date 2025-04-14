package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.example.hotel.controller.HotelController;
import com.example.hotel.service.HotelService;



class HotelControllerTest {

    private HotelService hotelService;
    private HotelController hotelController;

    @BeforeEach
    void setUp() {
        hotelService = mock(HotelService.class);
        hotelController = new HotelController(hotelService);
    }

    @Test
    void testGetAvailability_Success() {
        String hotelId = "H123";
        String roomType = "Deluxe";
        String dateRange = "2023-10-01 to 2023-10-05";

        when(hotelService.checkAvailability(hotelId, roomType, dateRange)).thenReturn(1);

        ResponseEntity<?> response = hotelController.getAvailability(hotelId, roomType, dateRange);

        assertEquals(OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(hotelService, times(1)).checkAvailability(hotelId, roomType, dateRange);
    }

    @Test
    void testGetAvailability_BadRequest() {
        String hotelId = "H123";
        String roomType = "Deluxe";
        String dateRange = "invalid-date";

        when(hotelService.checkAvailability(hotelId, roomType, dateRange))
                .thenThrow(new IllegalArgumentException("Invalid date range"));

        ResponseEntity<?> response = hotelController.getAvailability(hotelId, roomType, dateRange);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date range", response.getBody());
        verify(hotelService, times(1)).checkAvailability(hotelId, roomType, dateRange);
    }

    @Test
    void testGetAvailability_InternalServerError() {
        String hotelId = "H123";
        String roomType = "Deluxe";
        String dateRange = "2023-10-01 to 2023-10-05";

        when(hotelService.checkAvailability(hotelId, roomType, dateRange))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = hotelController.getAvailability(hotelId, roomType, dateRange);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Unexpected error", response.getBody());
        verify(hotelService, times(1)).checkAvailability(hotelId, roomType, dateRange);
    }

    @Test
    void testSearchAvailability_Success() {
        String hotelId = "H123";
        int daysAhead = 7;
        String roomType = "Deluxe";

        List<String> mockResults = Arrays.asList("2023-10-08", "2023-10-09");
        when(hotelService.searchAvailability(hotelId, daysAhead, roomType)).thenReturn(mockResults);

        ResponseEntity<?> response = hotelController.searchAvailability(hotelId, daysAhead, roomType);

        assertEquals(OK, response.getStatusCode());
        assertEquals(mockResults, response.getBody());
        verify(hotelService, times(1)).searchAvailability(hotelId, daysAhead, roomType);
    }

    @Test
    void testSearchAvailability_NotFound() {
        String hotelId = "H123";
        int daysAhead = 7;
        String roomType = "Deluxe";

        when(hotelService.searchAvailability(hotelId, daysAhead, roomType)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = hotelController.searchAvailability(hotelId, daysAhead, roomType);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("No availability found.", response.getBody());
        verify(hotelService, times(1)).searchAvailability(hotelId, daysAhead, roomType);
    }

    @Test
    void testSearchAvailability_BadRequest() {
        String hotelId = "H123";
        int daysAhead = -1;
        String roomType = "Deluxe";

        when(hotelService.searchAvailability(hotelId, daysAhead, roomType))
                .thenThrow(new IllegalArgumentException("Days ahead must be positive"));

        ResponseEntity<?> response = hotelController.searchAvailability(hotelId, daysAhead, roomType);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Days ahead must be positive", response.getBody());
        verify(hotelService, times(1)).searchAvailability(hotelId, daysAhead, roomType);
    }

    @Test
    void testSearchAvailability_InternalServerError() {
        String hotelId = "H123";
        int daysAhead = 7;
        String roomType = "Deluxe";

        when(hotelService.searchAvailability(hotelId, daysAhead, roomType))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = hotelController.searchAvailability(hotelId, daysAhead, roomType);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Unexpected error", response.getBody());
        verify(hotelService, times(1)).searchAvailability(hotelId, daysAhead, roomType);
    }
}