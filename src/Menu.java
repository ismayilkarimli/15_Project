public class Menu {

    public void welcomeMessage() {
        System.out.println("Welcome to the Airline Crash database");
    }

    public void mainScreen() {
        System.out.println("Available commands:");
        System.out.println("1. List crashes");
        System.out.println("2. Search crashes");
        System.out.println("3. Filter crashes");
        System.out.println("4. Data info");
        System.out.println("Type \"exit\" to exit");
        System.out.print("> ");
    }

    public void csvExportScreen() {
        System.out.println("Export to CSV?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("> ");
    }

    public void listingOptions() {
        System.out.println("Available options:");
        System.out.println("1. List all fields");
        System.out.println("2. Enter names of the fields to list (comma separated)");
        System.out.println("3. Back to main menu");
        System.out.print("> ");
    }

    public void sortPrompt() {
        System.out.println("Available options:");
        System.out.println("1. Sort");
        System.out.println("2. No sort");
        System.out.println("3. Back to main menu");
        System.out.print("> ");
    }

    public void sortScreen() {
        System.out.println("Available options:");
        System.out.println("1. Enter field name to sort by");
        System.out.println("2. Back to main menu");
        System.out.print("> ");
    }

    public void sortOrderOptions() {
        System.out.println("Available options:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.println("3. Back to main menu");
        System.out.print("> ");
    }

    public void rangeOptions() {
        System.out.println("Available options:");
        System.out.println("1. List all");
        System.out.println("2. List in range");
        System.out.println("3. Back to main menu");
        System.out.print("> ");
    }

    public void filterScreen() {
        System.out.println("Available options:");
        System.out.println("1. Enter filter(s) (separated with ampersand &)");
        System.out.println("2. Back to main menu");
        System.out.print("> ");
    }

    public void searchOptions() {
        System.out.println("Available options:");
        System.out.println("1. Enter field name(s) to search by");
        System.out.println("2. Back to main menu");
        System.out.print("> ");
    }
}
