import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataOperations {

    private List<Crash> dataSet;

    public DataOperations() {
        this.dataSet = new ArrayList<>();
    }

    public void setDataSet(List<Crash> ds) {
        dataSet = ds;
    }

    public void printDataSetInfo() {
        var fields = Crash.class.getDeclaredFields();
        System.out.println("Total number of fields: " + fields.length);
        Arrays.asList(fields).forEach(field -> System.out.printf("%s : (%s)\n", field.getName(), field.getType()));
        System.out.println("Data set size: " + dataSet.size());
        System.out.println();
    }

    public List<Crash> allEntities() {
        System.out.println("Results: " + dataSet.size());
        return dataSet;
    }

    public List<?> inRange(int from, int to, List<?> dataSet) {
        var result = dataSet.subList(from, to);
        System.out.println("Results: " + result.size());
        return result;
    }

    public List<Map<String, String>> listFields(List<Crash> dataSet, String... fields) {
        List<Map<String, String>> fieldValues = new ArrayList<>();
        Map<String, String> fieldValueMap = new HashMap<>();
        var allFields = Crash.class.getDeclaredFields();
        var names = Arrays.stream(allFields).collect(Collectors.toList());
        var validFields = names.stream().filter(e -> Arrays.asList(fields).contains(e.getName())).collect(Collectors.toList());
        try {
            for (Crash crash : dataSet) {
                for (Field validField : validFields) {
                    validField.setAccessible(true);
                    var field = validField.getName();
                    var value = validField.get(crash);

                    if (value == null) fieldValueMap.put(field, null);
                    else fieldValueMap.put(field, validField.get(crash).toString());
                }
                fieldValues.add(fieldValueMap);
                fieldValueMap = new HashMap<>();
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error during accessing fields");
        }

        System.out.println("Results: " + fieldValues.size());
        return fieldValues;
    }

    public List<Crash> sortBy(List<Crash> dataSet, String field, Sort order) {
        var crashFields = Crash.class.getDeclaredFields();
        var crashFieldsNames = Arrays.stream(crashFields).map(Field::getName).collect(Collectors.toList());
        if (!crashFieldsNames.contains(field)) {
            System.err.println("Invalid field name. Returning original list");
            System.out.println("Results: " + dataSet.size());
            return dataSet; // if the field is not valid, return the original list
        }
        var crashFieldsList = Arrays.stream(crashFields).collect(Collectors.toList());
        var crashField = crashFieldsList.stream().filter(e -> e.getName().equals(field)).findFirst().orElse(null);
        if (crashField == null) {
            System.err.println("Invalid field name. Returning original list");
            System.out.println("Results: " + dataSet.size());
            return dataSet; // if the field is not valid, return the original list
        }
        crashField.setAccessible(true);
        var result = dataSet.stream().sorted((first, second) -> {
            try {
                if (crashField.getType().equals(int.class)) {
                    int a = (int) crashField.get(first);
                    int b = (int) crashField.get(second);
                    return order == Sort.ASC ? a - b : b - a;
                } else if (crashField.getType().equals(double.class)) {
                    double a = (double) crashField.get(first);
                    double b = (double) crashField.get(second);
                    return order == Sort.ASC ? Double.compare(a, b) : Double.compare(b, a);
                } else if (crashField.getType().equals(String.class)) {
                    var a = (String) crashField.get(first);
                    var b = (String) crashField.get(second);
                    if (a == null) a = ""; // dummy value for if null, put at the beginning
                    if (b == null) b = "";
                    return order == Sort.ASC ? a.compareTo(b) : b.compareTo(a);
                } else if (crashField.getType().equals(LocalDate.class)) {
                    var a = (LocalDate) crashField.get(first);
                    var b = (LocalDate) crashField.get(second);
                    if (a == null) a = LocalDate.of(1900, 1, 1); // dummy value for if null, put at the beginning
                    if (b == null) b = LocalDate.of(1900, 1, 1);
                    return order == Sort.ASC ? a.compareTo(b) : b.compareTo(a);
                } else if (crashField.getType().equals(LocalTime.class)) {
                    var a = (LocalTime) crashField.get(first);
                    var b = (LocalTime) crashField.get(second);
                    if (a == null) a = LocalTime.of(0, 0); // dummy value for if null, put at the beginning
                    if (b == null) b = LocalTime.of(0, 0);
                    return order == Sort.ASC ? a.compareTo(b) : b.compareTo(a);
                } else {
                    Fatality a = (Fatality) crashField.get(first);
                    Fatality b = (Fatality) crashField.get(second);
                    if (a == null) a = Fatality.HIGH; // dummy value for if null, put at the beginning
                    if (b == null) b = Fatality.HIGH;
                    return order == Sort.ASC ? a.compareTo(b) : b.compareTo(a);
                }
            } catch (IllegalAccessException e) {
                System.err.println("Error during accessing fields");
            }
            return 1;
        }).collect(Collectors.toList());
        System.out.println("Results: " + result.size());
        return result;
    }

    public List<?> sortByFields(List<Map<String, String>> dataSet, String field, Sort order) {
        var fields = dataSet.get(0).keySet();
        List<Crash> crashes = new ArrayList<>();
        var crashFields = Arrays.asList(Crash.class.getDeclaredFields());
        var crashFieldNames = Arrays.stream(Crash.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        crashFields.forEach(f -> f.setAccessible(true));

        if (!fields.contains(field)) {
            System.err.println("Invalid field name. Returning original list");
            System.out.println("Results: " + dataSet.size());
            return dataSet; // if the field is not valid, return the original list
        }

        for (Map<String, String> record : dataSet) {
            var crash = new Crash.Builder().build();
            for (String key : fields) {
                if (crashFieldNames.contains(key)) {
                    try {
                        var crashField = Crash.class.getDeclaredField(key);
                        crashField.setAccessible(true);
                        if (crashField.getType().equals(int.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, Integer.parseInt(record.get(key)));
                        } else if (crashField.getType().equals(double.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, Double.parseDouble(record.get(key)));
                        } else if (crashField.getType().equals(String.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, record.get(key));
                        } else if (crashField.getType().equals(LocalDate.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, LocalDate.parse(record.get(key)));
                        } else if (crashField.getType().equals(LocalTime.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, LocalTime.parse(record.get(key)));
                        } else if (crashField.getType().equals(Fatality.class)) {
                            if (record.get(key) == null) crashField.set(crash, null);
                            else crashField.set(crash, Fatality.valueOf(record.get(key)));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.err.println("Error during accessing fields");
                    }
                }
            }
            crashes.add(crash);
        }

        return sortBy(crashes, field, order);
    }

    public List<Crash> search(List<Crash> crashes, String searchQuery) {
        if (!searchQuery.contains("=")) {
            System.err.println("Invalid search query. Returning original list");
            System.out.println("Results: " + crashes.size());
            return crashes;
        }
        var fieldValue = searchQuery.split("=");
        if (fieldValue.length != 2) {
            System.err.println("Invalid search query. Returning original list");
            System.out.println("Results: " + crashes.size());
            return crashes;
        }
        var field = fieldValue[0];
        var value = fieldValue[1];
        List<Crash> searchResult = new ArrayList<>();

        try {
            Field crashField = Crash.class.getDeclaredField(field);
            crashField.setAccessible(true);
            searchResult = crashes.stream().filter(crash -> {
                try {
                    if (crashField.getType().equals(int.class)) {
                        var intValue = Integer.parseInt(value);
                        var val = (int) crashField.get(crash);
                        return val == intValue;
                    } else if (crashField.getType().equals(double.class)) {
                        var doubleValue = Double.parseDouble(value);
                        var val = (double) crashField.get(crash);
                        return val == doubleValue;
                    } else if (crashField.getType().equals(String.class)) {
                        var val = (String) crashField.get(crash);
                        if (val == null) return false;
                        return val.toLowerCase().contains(value.toLowerCase());
                    } else if (crashField.getType().equals(LocalDate.class)) {
                        var dateValue = LocalDate.parse(value);
                        var val = (LocalDate) crashField.get(crash);
                        if (val == null) return false;
                        return val.equals(dateValue);
                    } else if (crashField.getType().equals(LocalTime.class)) {
                        var timeValue = LocalTime.parse(value);
                        var val = (LocalTime) crashField.get(crash);
                        if (val == null) return false;
                        return val.equals(timeValue);
                    } else if (crashField.getType().equals(Fatality.class)) {
                        var fatalityValue = Fatality.valueOf(value);
                        var val = (Fatality) crashField.get(crash);
                        if (val == null) return false;
                        return val.getFatalityType().equalsIgnoreCase(fatalityValue.getFatalityType());
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Error during searching");
                }
                return false;
            }).collect(Collectors.toList());
        } catch (NoSuchFieldException e) {
            System.err.println("Error during searching");
        }

        System.out.println("Results: " + searchResult.size());
        return searchResult;
    }

    public List<Crash> filter(List<Crash> crashes, String filterQuery) {
        class Keyword {
            // string
            final static String SW = "SW";
            final static String EW = "EW";
            final static String CONTAINS = "CONTAINS";

            // date, time, numerics
            final static String GT = "GT";
            final static String LT = "LT";
            final static String GTE = "GTW";
            final static String LTE = "LTW";
            final static String BW = "BW";

            // date
            final static String YEAR = "YEAR";
            final static String MONTH = "MONTH";
            final static String DAY = "DAY";

            // date, time, numerics, fatality
            final static String EQ = "EQ";

            // all
            final static String NULL = "NULL";
        }
        var filters = filterQuery.split("&");
        for (var filter : filters) {
            var filterParts = filter.trim().split(" ");
            if (filterParts.length == 2 && !filterParts[1].equals(Keyword.NULL)) {
                System.err.println("Invalid filter query. Returning original list.");
                System.out.println("Results: " + crashes.size());
                return crashes;
            }
            if (filterParts.length == 4 && !filterParts[1].equals(Keyword.BW)) {
                System.err.println("Invalid filter query. Returning original list.");
                System.out.println("Results: " + crashes.size());
                return crashes;
            }
            if (filterParts.length <= 1 || filterParts.length > 4) {
                System.err.println("Invalid filter query. Returning original list.");
                System.out.println("Results: " + crashes.size());
                return crashes;
            }

            switch (filterParts[1]) {
                case Keyword.SW:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            if (!field.getType().equals(String.class)) return false;
                            field.setAccessible(true);
                            var value = (String) field.get(crash);
                            return value != null && value.toLowerCase().startsWith(filterParts[2].toLowerCase());
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.EW:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            if (!field.getType().equals(String.class)) return false;
                            field.setAccessible(true);
                            var value = (String) field.get(crash);
                            return value != null && value.toLowerCase().endsWith(filterParts[2].toLowerCase());
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.CONTAINS:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            if (!field.getType().equals(String.class)) return false;
                            field.setAccessible(true);
                            var value = (String) field.get(crash);
                            return value != null && value.toLowerCase().contains(filterParts[2].toLowerCase());
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.GT:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val != null && val.doubleValue() > Double.parseDouble(filterParts[2]);
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val != null && val.isAfter(LocalDate.parse(filterParts[2]));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val != null && val.isAfter(LocalTime.parse(filterParts[2]));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.LT:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val != null && val.doubleValue() < Double.parseDouble(filterParts[2]);
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val != null && val.isBefore(LocalDate.parse(filterParts[2]));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val != null && val.isBefore(LocalTime.parse(filterParts[2]));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.GTE:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val != null && val.doubleValue() >= Double.parseDouble(filterParts[2]);
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val != null && (val.equals(LocalDate.parse(filterParts[2])) || val.isAfter(LocalDate.parse(filterParts[2])));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val != null && (val.equals(LocalTime.parse(filterParts[2])) || val.isAfter(LocalTime.parse(filterParts[2])));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.LTE:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val != null && val.doubleValue() <= Double.parseDouble(filterParts[2]);
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val != null && (val.isEqual(LocalDate.parse(filterParts[2])) || val.isBefore(LocalDate.parse(filterParts[2])));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val != null && (val.equals(LocalTime.parse(filterParts[2])) || val.isBefore(LocalTime.parse(filterParts[2])));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.BW:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class)) {
                                var val = (Integer) field.get(crash);
                                var val2 = Integer.parseInt(filterParts[2]);
                                var val3 = Integer.parseInt(filterParts[3]);
                                return val != null && val >= val2 && val <= val3;
                            } else if (field.getType().equals(double.class)) {
                                var val = (Double) field.get(crash);
                                var val2 = Double.parseDouble(filterParts[2]);
                                var val3 = Double.parseDouble(filterParts[3]);
                                return val != null && val >= val2 && val <= val3;
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                var val2 = LocalDate.parse(filterParts[2]);
                                var val3 = LocalDate.parse(filterParts[3]);
                                return val != null && (val.isEqual(val2) || val.isAfter(val2)) && (val.isEqual(val3) || val.isBefore(val3));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                var val2 = LocalTime.parse(filterParts[2]);
                                var val3 = LocalTime.parse(filterParts[3]);
                                return val != null && (val.equals(val2) || val.isAfter(val2)) && (val.equals(val3) || val.isBefore(val3));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.YEAR:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            var year = ((LocalDate) field.get(crash)).getYear();
                            return year == Integer.parseInt(filterParts[2]);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.MONTH:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            var month = ((LocalDate) field.get(crash)).getMonthValue();
                            return month == Integer.parseInt(filterParts[2]);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.DAY:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            var dayOfMonth = ((LocalDate) field.get(crash)).getDayOfMonth();
                            return dayOfMonth == Integer.parseInt(filterParts[2]);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.EQ:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val != null && val.doubleValue() == Double.parseDouble(filterParts[2]);
                            } else if (field.getType().equals(String.class)) {
                                var val = (String) field.get(crash);
                                return val != null && val.equalsIgnoreCase(filterParts[2]);
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val != null && val.equals(LocalDate.parse(filterParts[2]));
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val != null && val.equals(LocalTime.parse(filterParts[2]));
                            } else if (field.getType().equals(Fatality.class)) {
                                var val = (Fatality) field.get(crash);
                                return val != null && val.equals(Fatality.valueOf(filterParts[2]));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                case Keyword.NULL:
                    crashes = crashes.stream().filter(crash -> {
                        try {
                            var field = crash.getClass().getDeclaredField(filterParts[0]);
                            field.setAccessible(true);
                            if (field.getType().equals(int.class) || field.getType().equals(double.class)) {
                                var val = (Number) field.get(crash);
                                return val == null;
                            } else if (field.getType().equals(String.class)) {
                                var val = (String) field.get(crash);
                                return val == null;
                            } else if (field.getType().equals(LocalDate.class)) {
                                var val = (LocalDate) field.get(crash);
                                return val == null;
                            } else if (field.getType().equals(LocalTime.class)) {
                                var val = (LocalTime) field.get(crash);
                                return val == null;
                            } else if (field.getType().equals(Fatality.class)) {
                                var val = (Fatality) field.get(crash);
                                return val == null;
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error during filtering");
                        }
                        return false;
                    }).collect(Collectors.toList());
                    break;
                default:
                    System.err.println("Invalid filter query. Returning original list.");
                    System.out.println("Results: " + crashes.size());
                    return crashes;
            }
        }

        System.out.println("Results: " + crashes.size());
        return crashes;
    }

    public Pair<String, Integer> getAll(List<?> list) {
        StringBuilder sb = new StringBuilder();
        List<Crash> crashes = new ArrayList<>();
        var fields = Arrays.stream(Crash.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        for (int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i));
            if (i != fields.size() - 1) {
                sb.append("\t"); // separating headers with tabs
            }
        }
        sb.append("\n");
        if (list.get(0) instanceof Crash) {
            crashes = (List<Crash>) list;
        }
        for (var crash : crashes) {
            for (int i = 0; i < fields.size(); i++) {
                try {
                    var val = crash.getClass().getDeclaredField(fields.get(i));
                    val.setAccessible(true);
                    sb.append(val.get(crash));
                    if (i != fields.size() - 1) {
                        sb.append("\t"); // separating fields with tabs
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Error during getting fields");
                }
            }
            sb.append("\n");
        }

        sb.deleteCharAt(sb.length() - 1); // deleting the last \n

        return new Pair<>(sb.toString(), crashes.size());
    }

    public Pair<String, Integer> getSpecified(List<?> list, String[] fields) {
        List<String> fieldList = Arrays.asList(fields);
        StringBuilder sb = new StringBuilder();
        List<Crash> crashes;
        int resultSize = 0;

        var neededFields = Arrays.stream(Crash.class.getDeclaredFields()).filter(f -> fieldList.contains(f.getName())).collect(Collectors.toList());
        for (int i = 0; i < neededFields.size(); i++) {
            sb.append(neededFields.get(i).getName());
            if (i != neededFields.size() - 1) {
                sb.append("\t"); // separating headers with tabs
            }
        }
        sb.append("\n");
        if (list.get(0) instanceof Crash) {
            crashes = (List<Crash>) list;
            resultSize = crashes.size();
            for (var crash : crashes) {
                for (int i = 0; i < neededFields.size(); i++) {
                    try {
                        var val = crash.getClass().getDeclaredField(neededFields.get(i).getName());
                        val.setAccessible(true);
                        sb.append(val.get(crash));
                        if (i != neededFields.size() - 1) {
                            sb.append("\t"); // separating fields with tabs
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.err.println("Error during getting fields");
                    }
                }
                sb.append("\n");
            }
        } else if (list.get(0) instanceof HashMap) {
            resultSize = list.size();
            for (var crash : (List<HashMap<String, Object>>) list) {
                for (int i = 0; i < neededFields.size(); i++) {
                    var val = crash.get(neededFields.get(i).getName());
                    sb.append(val);
                    if (i != neededFields.size() - 1) {
                        sb.append("\t"); // separating fields with tabs
                    }
                }
                sb.append("\n");
            }
        }

        sb.deleteCharAt(sb.length() - 1); // deleting the last \n

        return new Pair<>(sb.toString(), resultSize);
    }

}
