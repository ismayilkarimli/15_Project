import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Crash {

    private final LocalDate date;
    private final LocalTime time;
    private final String location;
    private final String operator;
    private final String flight;
    private final String route;
    private final String type;
    private final String registration;
    private final String number;
    private final int passengers;
    private final int fatalities;
    private final int groundDeaths;
    private final int survivors;
    private final double survivalRate;
    private final double deathRate;
    private final String summary;
    private final Fatality fatalityType;

//    public Crash() { }

    private Crash(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.location = builder.location;
        this.operator = builder.operator;
        this.flight = builder.flight;
        this.route = builder.route;
        this.type = builder.type;
        this.registration = builder.registration;
        this.number = builder.number;
        this.passengers = builder.passengers;
        this.fatalities = builder.fatalities;
        this.groundDeaths = builder.groundDeaths;
        this.survivors = builder.survivors;
        this.survivalRate = builder.survivalRate;
        this.deathRate = 1 - builder.survivalRate;
        this.summary = builder.summary;
        this.fatalityType = builder.fatalityType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    public String getFlight() {
        return flight;
    }

    public String getRoute() {
        return route;
    }

    public String getType() {
        return type;
    }

    public String getRegistration() {
        return registration;
    }

    public String getNumber() {
        return number;
    }

    public int getPassengers() {
        return passengers;
    }

    public int getFatalities() {
        return fatalities;
    }

    public int getGroundDeaths() {
        return groundDeaths;
    }

    public int getSurvivors() {
        return survivors;
    }

    public double getSurvivalRate() {
        return survivalRate;
    }

    public double getDeathRate() {
        return this.deathRate;
    }

    public String getSummary() {
        return summary;
    }

    public Fatality isHighFatality() {
        return fatalityType;
    }

    @Override
    public String toString() {
        return "Crash{" +
                "date=" + date +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", operator='" + operator + '\'' +
                ", flight='" + flight + '\'' +
                ", route='" + route + '\'' +
                ", type='" + type + '\'' +
                ", registration='" + registration + '\'' +
                ", number='" + number + '\'' +
                ", passengers=" + passengers +
                ", fatalities=" + fatalities +
                ", groundDeaths=" + groundDeaths +
                ", survivors=" + survivors +
                ", survivalRate=" + survivalRate +
                ", summary='" + summary + '\'' +
                ", fatalityType=" + fatalityType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crash crash = (Crash) o;
        return passengers == crash.passengers && fatalities == crash.fatalities && groundDeaths == crash.groundDeaths && survivors == crash.survivors && Double.compare(crash.survivalRate, survivalRate) == 0 && Objects.equals(date, crash.date) && Objects.equals(time, crash.time) && Objects.equals(location, crash.location) && Objects.equals(operator, crash.operator) && Objects.equals(flight, crash.flight) && Objects.equals(route, crash.route) && Objects.equals(type, crash.type) && Objects.equals(registration, crash.registration) && Objects.equals(number, crash.number) && Objects.equals(summary, crash.summary) && fatalityType == crash.fatalityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, location, operator, flight, route, type, registration, number, passengers, fatalities, groundDeaths, survivors, survivalRate, summary, fatalityType);
    }

    public static class Builder {
        private LocalDate date;
        private LocalTime time;
        private String location;
        private String operator;
        private String flight;
        private String route;
        private String type;
        private String registration;
        private String number;
        private int passengers;
        private int fatalities;
        private int groundDeaths;
        private int survivors;
        private double survivalRate;
        private String summary;
        private Fatality fatalityType;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder passengers(int passengers) {
            this.passengers = passengers;
            return this;
        }

        public Builder fatalities(int fatalities) {
            this.fatalities = fatalities;
            return this;
        }

        public Builder groundDeaths(int groundDeaths) {
            this.groundDeaths = groundDeaths;
            return this;
        }

        public Builder survivors(int survivors) {
            this.survivors = survivors;
            return this;
        }

        public Builder survivalRate(double survivalRate) {
            this.survivalRate = survivalRate;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder fatalityType(Fatality fatalityType) {
            this.fatalityType = fatalityType;
            return this;
        }

        public Builder time(LocalTime time) {
            this.time = time;
            return this;
        }

        public Builder flight(String flight) {
            this.flight = flight;
            return this;
        }

        public Builder route(String route) {
            this.route = route;
            return this;
        }

        public Builder registration(String registration) {
            this.registration = registration;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Crash build() {
            return new Crash(this);
        }

    }

}
