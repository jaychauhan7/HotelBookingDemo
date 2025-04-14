# HotelBookingDemo

2 GET API's
1)./availability : Check availability for a specific hotel, room type, and date range.
2)./search: Search for availability in the next specified number of days.

It's collections URL with payload are:
1).http://localhost:8080/api/availability?hotelId=H1&roomType=DBL&dateRange=20250901-20250903
2).http://localhost:8080/api/search?hotelId=H1&daysAhead=365&roomType=SGL
