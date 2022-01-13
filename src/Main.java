import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static DataOperations dataOperations;
    private static Menu menu;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        menu = new Menu();
        dataOperations = new DataOperations();
        String op;
        var dataOperations = new DataOperations();
        var rows = CsvOperations.readFromCsv(Path.of("./Large_Passenger_Plane_Crashes_1933_to_2009.csv"), true);
        var crashes = Bootstrap.loadData(rows);

        dataOperations.setDataSet(crashes);
        menu.welcomeMessage();

        do {
            menu.mainScreen();
            op = scanner.nextLine();
            switch (op) {
                case "1": {
                    menu.listingOptions();
                    String listOp = scanner.nextLine();
                    var all = dataOperations.allEntities(); // get all data
                    listingType(listOp, all);
                    break;
                } // list
                case "2": {
                    menu.searchOptions();
                    String searchOp = scanner.nextLine();
                    var all = dataOperations.allEntities(); // get all data
                    switch (searchOp) {
                        case "1": {
                            search(all);
                        }
                        case "2": {
                            break;
                        }
                        default:
                            System.err.println("Invalid option");
                    } // field to search
                    break;
                } // search
                case "3": {
                    menu.filterScreen();
                    String filterOp = scanner.nextLine();
                    var all = dataOperations.allEntities(); // get all data
                    switch (filterOp) {
                        case "1": {
                            filterInput(all);
                        }
                        case "2": {
                            break;
                        }
                        default:
                            System.err.println("Invalid option");
                    }
                    break;
                } // filter
                case "4": {
                    dataOperations.printDataSetInfo();
                    break;
                } // data info
            }
        } while (!op.equals("exit"));
        System.out.println("Bye!");
        scanner.close();
    }

    private static void search(List<Crash> all) {
        var scanner = new Scanner(System.in);
        System.out.print("field=value: ");
        String searchQuery = scanner.nextLine();
        var result = dataOperations.search(all, searchQuery);
        menu.listingOptions();
        String listingOption = scanner.nextLine();
        listingType(listingOption, result);
    }

    private static void filterInput(List<Crash> all) {
        var scanner = new Scanner(System.in);
        System.out.print("Filter query: ");
        String filterQuery = scanner.nextLine();
        var result = dataOperations.filter(all, filterQuery);
        menu.listingOptions();
        String listOp = scanner.nextLine();
        listingType(listOp, result);
    }

    private static void fieldInput(List<Crash> all) {
        var scanner = new Scanner(System.in);
        System.out.print("Fields: ");
        String fields = scanner.nextLine();
        var requestFields = fields.split(",");
        var list = dataOperations.listFields(all, requestFields);
        sortSettingsFields(list, requestFields);
    }

    private static void listingType(String listingOption, List<Crash> result) {
        switch (listingOption) {
            case "1": {
                sortSettings(result);
                break;
            } // list all fields (columns)
            case "2": {
                fieldInput(result);
                break;
            } // list some fields (columns)
            case "3": {
                break;
            }
            default:
                System.err.println("Invalid option");
        }
    }

    private static void sortSettings(List<Crash> all) {
        var scanner = new Scanner(System.in);
        String sortBy;
        String sortOrder;
        String sortOption;
        menu.sortPrompt();
        sortOption = scanner.nextLine(); // sort or not
        switch (sortOption) {
            case "1": {
                menu.sortScreen();
                String sortOp = scanner.nextLine();
                switch (sortOp) {
                    case "1": {
                        System.out.print("Field: ");
                        sortBy = scanner.nextLine(); // field to sort
                        menu.sortOrderOptions();
                        sortOrder = scanner.nextLine();
                        switch (sortOrder) {
                            case "1": {
                                var sorted = dataOperations.sortBy(all, sortBy, Sort.ASC);
                                rangeSettings(sorted);
                                break;
                            } // ascending
                            case "2": {
                                var sorted = dataOperations.sortBy(all, sortBy, Sort.DESC);
                                rangeSettings(sorted);
                                break;
                            } // descending
                            default:
                                System.err.println("Invalid option");
                        } // asc or desc
                        break;
                    } // sort
                    case "2": {
                        break;
                    } // menu
                    default:
                        System.err.println("Invalid option");
                } // sort or menu
                break;
            } // sort
            case "2": {
                rangeSettings(all);
                break;
            } // no sort
            case "3": {
                break;
            } // menu
            default:
                System.err.println("Invalid option");
        }
    }

    private static void sortSettingsFields(List<Map<String, String>> all, String[] fields) {
        var scanner = new Scanner(System.in);
        String sortBy;
        String sortOrder;
        String sortOption;
        menu.sortPrompt();
        sortOption = scanner.nextLine(); // sort or not
        switch (sortOption) {
            case "1": {
                menu.sortScreen();
                String sortOp = scanner.nextLine();
                switch (sortOp) {
                    case "1": {
                        System.out.print("Field: ");
                        sortBy = scanner.nextLine(); // field to sort by
                        menu.sortOrderOptions();
                        sortOrder = scanner.nextLine();
                        switch (sortOrder) {
                            case "1": {
                                var sorted = dataOperations.sortByFields(all, sortBy, Sort.ASC);
                                rangeSettings(sorted, fields);
                                break;
                            } // ascending
                            case "2": {
                                var sorted = dataOperations.sortByFields(all, sortBy, Sort.DESC);
                                rangeSettings(sorted, fields);
                                break;
                            } // descending
                            case "3": {
                                break;
                            } // menu
                            default:
                                System.err.println("Invalid option");
                        } // asc or desc
                        break;
                    } // field to sort
                    case "2": {
                        break;
                    } // menu
                    default:
                        System.err.println("Invalid option");
                }
                break;
            } // sort
            case "2": {
                rangeSettings(all, fields);
                break;
            } // no sort
            case "3": { // menu
                break;
            }
            default:
                System.err.println("Invalid option");
        }
    }

    private static void rangeSettings(List<?> list, String... fields) {
        var scanner = new Scanner(System.in);
        String range;
        menu.rangeOptions();
        range = scanner.nextLine();
        switch (range) {
            case "1": {
                var content = printResultSet(list, fields);
                csvPrompt(content);
                break;
            } // all
            case "2": {
                var inRange = rangePrompt(list);
                var content = printResultSet(inRange, fields);
                csvPrompt(content);
                break;
            } // in range
            case "3": {
                break;
            } // menu
            default:
                System.err.println("Invalid option");
        }
    }

    private static void csvPrompt(String list) {
        var scanner = new Scanner(System.in);
        menu.csvExportScreen();
        String export = scanner.nextLine();
        switch (export) {
            case "1": { // csv
                try {
                    CsvOperations.writeToCsv(list);
                } catch (IOException e) {
                    System.err.println("Error writing to file");
                }
                break;
            }
            case "2": {
                break;
            }
            default:
                System.err.println("Invalid option");
        }
    }

    private static List<?> rangePrompt(List<?> list) {
        var scanner = new Scanner(System.in);
        int from, to;
        System.out.print("From: ");
        from = scanner.nextInt();
        System.out.print("To: ");
        to = scanner.nextInt();
        return dataOperations.inRange(from, to, list);
    }

    private static String printResultSet(List<?> list, String... fields) {
        Pair<String, Integer> toPrint;
        if (fields.length == 0) {
            toPrint = dataOperations.getAll(list);
        } else {
            toPrint = dataOperations.getSpecified(list, fields);
        }
        System.out.println(toPrint.getKey());

        System.out.println("Total: " + toPrint.getValue());

        return toPrint.getKey();
    }

}
