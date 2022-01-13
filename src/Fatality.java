import java.util.Arrays;

public enum Fatality {
    HIGH("High Fatality"),
    LOW("Low Fatality");

    private final String fatalityType;

    Fatality(String fatalityType) {
        this.fatalityType = fatalityType;
    }

    public String getFatalityType() {
        return fatalityType;
    }

    public static Fatality fromString(String fatalityType) {
        return Arrays.stream(Fatality.values()).filter(e -> e.getFatalityType().equalsIgnoreCase(fatalityType))
                .findFirst()
                .get();
    }
}
