import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap {

    private final static String regex = "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*\\Z),(?!\\s)";
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yy");
    private final static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

    public static List<Crash> loadData(List<String> rows) {
        List<Crash> crashes = new ArrayList<>();
        for (String row : rows) {
            var columns = row.split(regex);
            var date = LocalDate.parse(columns[0], dateFormat);
            if (date.getYear() > 2009) date = date.minusYears(100);
            var time = columns[1].isBlank() ? null : LocalTime.parse(columns[1], timeFormat);
            var location = columns[2];
            var operator = columns[3];
            var flight = columns[4].isBlank() ? null : columns[4];
            var route = columns[5].isBlank() ? null : columns[5];
            var type = columns[6];
            var registration = columns[7].isBlank() ? null : columns[7];
            var number = columns[8].isBlank() ? null : columns[8];
            var passengers = Integer.parseInt(columns[9]);
            var fatalities = Integer.parseInt(columns[10]);
            var groundDeaths = Integer.parseInt(columns[11]);
            var survivors = Integer.parseInt(columns[12]);
            var survivalRate = Double.parseDouble(columns[13]);
            var summary = columns[14];
            var fatalityType = Fatality.fromString(columns[15]);

            var crash = new Crash.Builder()
                    .date(date)
                    .location(location)
                    .operator(operator)
                    .type(type)
                    .passengers(passengers)
                    .fatalities(fatalities)
                    .groundDeaths(groundDeaths)
                    .survivors(survivors)
                    .survivalRate(survivalRate)
                    .summary(summary)
                    .fatalityType(fatalityType)
                    .time(time)
                    .route(route)
                    .flight(flight)
                    .registration(registration)
                    .number(number)
                    .build();

            crashes.add(crash);
        }
        return crashes;
    }

}
