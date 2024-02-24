package com.gridnine.testing;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        // Исключаем перелеты, которые не удовлетворяют заданным правилам и выводим их списки.
        // Метод деструктивный, так как выводит результат самолёта, который прилетел раньше своего времени (3 строчка).
        List<Flight> flightsExcludingDepartureBeforeNow = excludeFlightsDepartureBeforeNow(flights);
        System.out.println("Исключены перелеты с вылетом до текущего момента времени:");
        for (Flight flight : flightsExcludingDepartureBeforeNow) {
            System.out.println(flight);
        }
        System.out.println("---------------------------------------------");

        List<Flight> flightsExcludingSegmentsInverted = excludeFlightsSegmentsInverted(flights);
        System.out.println("Исключены перелеты с сегментами, где дата прилета раньше даты вылета:");
        for (Flight flight : flightsExcludingSegmentsInverted) {
            System.out.println(flight);
        }
        System.out.println("---------------------------------------------");

        // Метод деструктивный, так как выводит результат самолёта, который простоял несколько дней (3 строчка).
        // Метод деструктивные, так как выводит результат ровный 2 часам на земле, а не превышающий его
        List<Flight> flightsExcludingGroundTimeExceeded = excludeFlightsGroundTimeExceeded(flights);
        System.out.println("Исключены перелеты, где время проведенное на земле превышает 2 часа:");
        for (Flight flight : flightsExcludingGroundTimeExceeded) {
            System.out.println(flight);
        }
        System.out.println("---------------------------------------------");

        List<Flight> allFlights = FlightBuilder.createFlights();
        System.out.println("Выводим каждый перелет в командную строку");
        for (Flight flight : allFlights) {
            System.out.println(flight);
        }
        System.out.println("---------------------------------------------");


        // Для каждого сегмента перелета вычисляем общее время проведенное на земле и в перелете
        List<Flight> timeFlights = FlightBuilder.createFlights();
        for (Flight flight : timeFlights) {
            System.out.println("Сегмент полёта: " + flight.getSegments());
            for (Segment segment : flight.getSegments()) {
                long groundTime = segment.getGroundTime();
                if (groundTime < 0) {
                    System.out.println("Сегмент наземного времени: 0 часов");
                } else {
                    System.out.println("Сегмент наземного времени: " + groundTime + " часов");
                }
            }

            System.out.println();
        }
    }

        // Метод для исключения перелетов с вылетом до текущего момента времени
        private static List<Flight> excludeFlightsDepartureBeforeNow(List<Flight> flights) {
            LocalDateTime now = LocalDateTime.now();
            return flights.stream()
                    .filter(flight -> flight.getSegments()
                            .stream()
                            .allMatch(segment -> segment.getDepartureDate().isAfter(now)))
                    .collect(Collectors.toList());
        }

        // Метод для исключения перелетов с сегментами, где дата прилета раньше даты вылета
        private static List<Flight> excludeFlightsSegmentsInverted(List<Flight> flights) {
            return flights.stream()
                    .filter(flight -> flight.getSegments()
                            .stream()
                            .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())))
                    .collect(Collectors.toList());
        }

        // Метод для исключения перелетов, где время, проведенное на земле превышает 2 часа
        private static List<Flight> excludeFlightsGroundTimeExceeded(List<Flight> flights) {
            return flights.stream()
                    .filter(flight -> getTotalGroundTime(flight) <= 2)
                    .collect(Collectors.toList());
        }

        // Метод для вычисления общего времени проведенного на земле в перелете
        private static int getTotalGroundTime(Flight flight) {
            int totalGroundTime = 0;
            List<Segment> segments = flight.getSegments();

            for (int i = 0; i < segments.size() - 1; i++) {
                LocalDateTime arrivalDate = segments.get(i).getArrivalDate();
                LocalDateTime departureDate = segments.get(i + 1).getDepartureDate();
                totalGroundTime += departureDate.toLocalTime().toSecondOfDay() - arrivalDate.toLocalTime().toSecondOfDay();
            }

            return totalGroundTime / 3600; // переводим в часы

        }
    // 1) Можно добавить метод, что выводит ординарные рейсы.
    // 2) Можно добавить метод, что выводит самолёты с двумя или большим количеством рейсов.
    // 3) Можно добавить метод, что высчитывает время между полётами рейсов.
    // 4) Можно добавить метод, что выводит рейсы отправленные в прошлом.
    // 5) Можно добавить метод, что выводит рейсы отправленные в прошлом.
    // 6) Можно добавить метод, что выводит рейсы стоящие ровно 2 часа




}

