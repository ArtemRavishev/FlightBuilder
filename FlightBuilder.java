package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  Фабричный класс для получения списка примеров перелетов.
 */
class FlightBuilder {
    static List<Flight> createFlights() {
        // Получаем текущую дату и прибавляем 3 дня
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // Обычный рейс продолжительностью два часа
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                // Обычный многосегментный полет
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
                // Рейс, вылетавший в прошлом
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                // Рейс, который вылетает раньше, чем прибывает
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                // Полет с более чем двухчасовым наземным временем
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
                // Еще один рейс с более чем двухчасовым наземным временем
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                        threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7))
        );
    }


    private static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "Вы должны передать четное количество дат");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}

/**
 *  Класс, представляющий перелет.
 */
class Flight {
    private final List<Segment> segments;

    Flight(final List<Segment> segs) {
        segments = segs;
    }

    List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}

/**
 * Класс, представляющий сегмент перелета.
 */
class Segment {
    private final LocalDateTime departureDate;

    private final LocalDateTime arrivalDate;

    Segment(final LocalDateTime dep, final LocalDateTime arr) {
        departureDate = Objects.requireNonNull(dep);
        arrivalDate = Objects.requireNonNull(arr);
    }

    LocalDateTime getDepartureDate() {
        return departureDate;
    }

    LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return '[' + departureDate.format(fmt) + '|' + arrivalDate.format(fmt)
                + ']';
    }

    public long getGroundTime() {
        Duration duration = Duration.between(departureDate, arrivalDate);
        return duration.toHours();
    }
}


