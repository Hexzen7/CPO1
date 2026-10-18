import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BarangayRecordManagementSystem {
    static Scanner input = new Scanner(System.in);

    static final int MAX_RESIDENTS = 100;
    static final int MAX_USERS = 100;
    static final int MAX_HOUSEHOLDS = 100;
    static final int MAX_REQUESTS = 200;
    static final int MAX_INCIDENTS = 200;

    // Values at the same array index belong to the same resident record.
    static int[] residentId = new int[MAX_RESIDENTS];
    static String[] residentName = new String[MAX_RESIDENTS];
    static LocalDate[] residentBirthDate = new LocalDate[MAX_RESIDENTS];
    static String[] residentGender = new String[MAX_RESIDENTS];
    static String[] residentAddress = new String[MAX_RESIDENTS];
    static String[] residentCivilStatus = new String[MAX_RESIDENTS];
    static String[] residentOccupation = new String[MAX_RESIDENTS];
    static String[] residentContact = new String[MAX_RESIDENTS];
    static boolean[] registeredVoter = new boolean[MAX_RESIDENTS];
    static int[] residentHouseholdId = new int[MAX_RESIDENTS];
    static boolean[] active = new boolean[MAX_RESIDENTS];

    // User accounts and records exist only while the program is running.
    static String[] usernames = new String[MAX_USERS];
    static String[] passwords = new String[MAX_USERS];
    static String[] userRoles = new String[MAX_USERS];
    static int userCount = 1;

    static int[] householdId = new int[MAX_HOUSEHOLDS];
    static String[] householdAddress = new String[MAX_HOUSEHOLDS];
    static int[] householdHeadId = new int[MAX_HOUSEHOLDS];
    static boolean[] householdActive = new boolean[MAX_HOUSEHOLDS];
    static int householdCount = 0;

    static int[] requestId = new int[MAX_REQUESTS];
    static int[] requestResidentId = new int[MAX_REQUESTS];
    static String[] requestType = new String[MAX_REQUESTS];
    static LocalDate[] requestDate = new LocalDate[MAX_REQUESTS];
    static String[] requestStatus = new String[MAX_REQUESTS];
    static boolean[] requestActive = new boolean[MAX_REQUESTS];
    static int requestCount = 0;

    static int[] incidentId = new int[MAX_INCIDENTS];
    static String[] incidentComplainant = new String[MAX_INCIDENTS];
    static String[] incidentRespondent = new String[MAX_INCIDENTS];
    static String[] incidentDetails = new String[MAX_INCIDENTS];
    static LocalDate[] incidentDate = new LocalDate[MAX_INCIDENTS];
    static String[] incidentStatus = new String[MAX_INCIDENTS];
    static boolean[] incidentActive = new boolean[MAX_INCIDENTS];
    static int incidentCount = 0;

    static int residentCount = 0;
    static int nextId = 1001;
    static int nextHouseholdId = 1;
    static int nextRequestId = 1;
    static int nextIncidentId = 1;
    static int currentUserIndex = -1;

    static {
        usernames[0] = "admin";
        passwords[0] = "admin123";
        userRoles[0] = "Administrator";
    }

    // Starts the application, handles account access, and opens the main menu.
    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println(" BARANGAY RECORD MANAGEMENT SYSTEM");
        System.out.println("==================================");

        if (login()) {
            showMenu();
        } else {
            System.out.println("Login cancelled or too many failed attempts. Access denied.");
        }

        System.out.println("Program ended.");
    }

    // Provides login, sign-up, and exit options, allowing up to three login attempts.
    public static boolean login() {
        while (true) {
            System.out.println("\n========== ACCOUNT ACCESS ==========");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("3. Exit");
            int choice = readInt("Enter your choice: ");

            if (choice == 1) {
                int attempts = 0;
                while (attempts < 3) {
                    System.out.print("\nUsername: ");
                    String username = input.nextLine().trim();

                    System.out.print("Password: ");
                    String password = input.nextLine();

                    int userIndex = findUser(username, password);
                    if (userIndex != -1) {
                        currentUserIndex = userIndex;
                        System.out.println("\nLogin successful. Role: " + userRoles[userIndex]);
                        return true;
                    }

                    attempts++;
                    System.out.println("Incorrect username or password. Attempts left: " + (3 - attempts));
                }
                return false;
            } else if (choice == 2) {
                signUp();
            } else if (choice == 3) {
                return false;
            } else {
                System.out.println("Invalid choice. Please choose from 1 to 3.");
            }
        }
    }

    // Registers a new Barangay Staff account after validating its credentials.
    public static void signUp() {
        System.out.println("\n------ CREATE ACCOUNT ------");
        if (userCount >= MAX_USERS) {
            System.out.println("The account list is full.");
            return;
        }

        System.out.print("Choose a username: ");
        String username = input.nextLine().trim();
        while (username.isEmpty() || usernameExists(username)) {
            if (username.isEmpty()) {
                System.out.print("Username cannot be empty. Choose a username: ");
            } else {
                System.out.print("That username is already taken. Choose another: ");
            }
            username = input.nextLine().trim();
        }

        System.out.print("Choose a password: ");
        String password = input.nextLine();
        while (password.trim().isEmpty()) {
            System.out.print("Password cannot be empty. Choose a password: ");
            password = input.nextLine();
        }

        usernames[userCount] = username;
        passwords[userCount] = password;
        userRoles[userCount] = "Barangay Staff";
        userCount++;

        System.out.println("Barangay Staff account created. You can now log in.");
    }

    // Returns the index of the account matching the supplied username and password.
    public static int findUser(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equalsIgnoreCase(username) && passwords[i].equals(password)) {
                return i;
            }
        }
        return -1;
    }

    // Checks whether a username is already assigned to an account.
    public static boolean usernameExists(String username) {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Displays the main menu and routes users to features allowed by their roles.
    public static void showMenu() {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("Signed in as: " + usernames[currentUserIndex] + " (" + userRoles[currentUserIndex] + ")");
            System.out.println("1. Resident Management");
            System.out.println("2. Household Management");
            System.out.println("3. Certificate Requests");
            System.out.println("4. Blotter Management (Administrator)");
            System.out.println("5. User Management (Administrator)");
            System.out.println("6. Reports (Administrator / Barangay Secretary)");
            System.out.println("7. Logout");
            System.out.println("8. Exit");
            System.out.println("===============================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    if (hasRole("Administrator", "Barangay Staff")) {
                        residentMenu();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 2:
                    if (hasRole("Administrator", "Barangay Staff")) {
                        householdMenu();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 3:
                    if (hasRole("Administrator", "Barangay Staff")) {
                        certificateMenu();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 4:
                    if (hasRole("Administrator")) {
                        blotterMenu();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 5:
                    if (hasRole("Administrator")) {
                        userMenu();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 6:
                    if (hasRole("Administrator", "Barangay Secretary")) {
                        showReports();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case 7:
                    System.out.println("You have logged out.");
                    currentUserIndex = -1;
                    if (login()) {
                        break;
                    } else {
                        System.out.println("Login cancelled or failed. Exiting system.");
                        return;
                    }
                case 8:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 8.");
            }
        }
    }

    // Checks whether the signed-in user's role is among the permitted roles.
    public static boolean hasRole(String... allowedRoles) {
        for (String role : allowedRoles) {
            if (userRoles[currentUserIndex].equals(role)) {
                return true;
            }
        }
        return false;
    }

    // Displays a message when the signed-in role cannot access a feature.
    public static void showAccessDenied() {
        System.out.println("Access denied. Your account role cannot use this feature.");
    }

    // Provides the menu for adding, viewing, searching, updating, and deleting residents.
    public static void residentMenu() {
        while (true) {
            System.out.println("\n------ RESIDENT MANAGEMENT ------");
            System.out.println("1. Add resident");
            System.out.println("2. View all residents");
            System.out.println("3. Search / view resident profile");
            System.out.println("4. Update resident");
            System.out.println("5. Delete resident");
            System.out.println("6. Return to main menu");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addResident();
                    break;
                case 2:
                    viewResidents();
                    break;
                case 3:
                    searchResident();
                    break;
                case 4:
                    updateResident();
                    break;
                case 5:
                    deleteResident();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 6.");
            }
        }
    }

    // Provides the menu for creating households, adding members, and viewing records.
    public static void householdMenu() {
        while (true) {
            System.out.println("\n------ HOUSEHOLD MANAGEMENT ------");
            System.out.println("1. Create household");
            System.out.println("2. Add resident to household");
            System.out.println("3. View households");
            System.out.println("4. Return to main menu");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addHousehold();
                    break;
                case 2:
                    addHouseholdMember();
                    break;
                case 3:
                    viewHouseholds();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 4.");
            }
        }
    }

    // Provides options for filing, viewing, and processing certificate requests.
    public static void certificateMenu() {
        while (true) {
            System.out.println("\n------ CERTIFICATE REQUESTS ------");
            System.out.println("1. File certificate request");
            System.out.println("2. View / track requests");
            System.out.println("3. Process and print certificate");
            System.out.println("4. Return to main menu");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addCertificateRequest();
                    break;
                case 2:
                    viewCertificateRequests();
                    break;
                case 3:
                    processCertificateRequest();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 4.");
            }
        }
    }

    // Provides options for recording incidents, viewing them, and updating their status.
    public static void blotterMenu() {
        while (true) {
            System.out.println("\n------ BLOTTER MANAGEMENT ------");
            System.out.println("1. Record incident");
            System.out.println("2. View incidents");
            System.out.println("3. Update incident status");
            System.out.println("4. Return to main menu");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addIncident();
                    break;
                case 2:
                    viewIncidents();
                    break;
                case 3:
                    updateIncidentStatus();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 4.");
            }
        }
    }

    // Provides options for creating accounts, changing roles, and viewing users.
    public static void userMenu() {
        while (true) {
            System.out.println("\n------ USER MANAGEMENT ------");
            System.out.println("1. Create user account");
            System.out.println("2. Change user role");
            System.out.println("3. View users");
            System.out.println("4. Return to main menu");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    createManagedUser();
                    break;
                case 2:
                    changeUserRole();
                    break;
                case 3:
                    viewUsers();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1 to 4.");
            }
        }
    }

    // Collects and stores a new resident record in an available slot.
    public static void addResident() {
        if (residentCount >= MAX_RESIDENTS) {
            System.out.println("The resident list is full.");
            return;
        }

        System.out.println("\n------ ADD RESIDENT ------");
        int index = findFreeIndex();

        if (index == -1) {
            System.out.println("No available space for another resident.");
            return;
        }

        residentHouseholdId[index] = 0;
        residentId[index] = nextId++;

        residentName[index] = readRequiredText("Enter full name: ");
        residentBirthDate[index] = readDate("Enter birth date (YYYY-MM-DD): ");
        residentGender[index] = readGender();
        residentAddress[index] = readRequiredText("Enter full address: ");
        residentCivilStatus[index] = readRequiredText("Enter civil status: ");
        residentOccupation[index] = readRequiredText("Enter occupation: ");
        residentContact[index] = readRequiredText("Enter contact number: ");
        registeredVoter[index] = readYesNo("Is this resident a registered voter? (Y/N): ");

        active[index] = true;
        residentCount++;

        System.out.println("Resident added successfully. Resident ID: " + residentId[index]);
    }

    // Prints all currently active resident records.
    public static void viewResidents() {
        System.out.println("\n------ RESIDENT RECORDS ------");

        if (residentCount == 0) {
            System.out.println("No resident records found.");
            return;
        }

        for (int i = 0; i < MAX_RESIDENTS; i++) {
            if (!active[i]) {
                continue;
            }
            printResident(i);
        }
    }

    // Finds a resident by ID and displays their profile.
    public static void searchResident() {
        System.out.println("\n------ SEARCH RESIDENT ------");
        int id = readInt("Enter resident ID: ");
        int index = findResidentById(id);

        if (index == -1) {
            System.out.println("Resident not found.");
        } else {
            printResident(index);
        }
    }

    // Updates the entered fields of an existing resident record.
    public static void updateResident() {
        System.out.println("\n------ UPDATE RESIDENT ------");
        int id = readInt("Enter resident ID to update: ");
        int index = findResidentById(id);

        if (index == -1) {
            System.out.println("Resident not found.");
            return;
        }

        System.out.println("Enter the updated information.");
        System.out.print("Enter full name: ");
        String name = input.nextLine().trim();
        if (!name.isEmpty()) {
            residentName[index] = name;
        }

        System.out.print("Enter birth date (YYYY-MM-DD, blank to keep current): ");
        String birthDate = input.nextLine().trim();
        if (!birthDate.isEmpty()) {
            try {
                LocalDate parsedDate = LocalDate.parse(birthDate);
                if (!parsedDate.isAfter(LocalDate.now())) {
                    residentBirthDate[index] = parsedDate;
                } else {
                    System.out.println("Birth date cannot be in the future. Keeping current date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Keeping current date.");
            }
        }

        System.out.print("Enter gender (blank to keep current): ");
        String gender = input.nextLine().trim();
        if (!gender.isEmpty()) {
            if (gender.equalsIgnoreCase("Male")) {
                residentGender[index] = "Male";
            } else if (gender.equalsIgnoreCase("Female")) {
                residentGender[index] = "Female";
            } else if (gender.equalsIgnoreCase("Other")) {
                residentGender[index] = "Other";
            } else {
                System.out.println("Invalid gender. Keeping the current value.");
            }
        }

        System.out.print("Enter address (blank to keep current): ");
        String address = input.nextLine().trim();
        if (!address.isEmpty()) {
            residentAddress[index] = address;
        }

        System.out.print("Enter civil status (blank to keep current): ");
        String civilStatus = input.nextLine().trim();
        if (!civilStatus.isEmpty()) {
            residentCivilStatus[index] = civilStatus;
        }

        System.out.print("Enter occupation (blank to keep current): ");
        String occupation = input.nextLine().trim();
        if (!occupation.isEmpty()) {
            residentOccupation[index] = occupation;
        }

        System.out.print("Enter contact number (blank to keep current): ");
        String contact = input.nextLine().trim();
        if (!contact.isEmpty()) {
            residentContact[index] = contact;
        }

        if (readYesNo("Change registered voter status? (Y/N): ")) {
            registeredVoter[index] = readYesNo("Registered voter? (Y/N): ");
        }

        System.out.println("Resident record updated successfully.");
    }

    // Confirms and deactivates the selected resident record.
    public static void deleteResident() {
        System.out.println("\n------ DELETE RESIDENT ------");
        int id = readInt("Enter resident ID to delete: ");
        int index = findResidentById(id);

        if (index == -1) {
            System.out.println("Resident not found.");
            return;
        }

        printResident(index);
        System.out.print("Are you sure you want to delete this record? (Y/N): ");
        String answer = input.nextLine().trim();

        if (answer.equalsIgnoreCase("Y")) {
            active[index] = false;
            residentHouseholdId[index] = 0;
            residentCount--;
            System.out.println("Resident record deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Displays the details of the resident stored at the specified array index.
    public static void printResident(int index) {
        System.out.println("------------------------------");
        System.out.println("Resident ID: " + residentId[index]);
        System.out.println("Full name: " + residentName[index]);
        System.out.println("Birth date: " + residentBirthDate[index]);
        System.out.println("Age: " + calculateAge(index));
        System.out.println("Gender: " + residentGender[index]);
        System.out.println("Address: " + residentAddress[index]);
        System.out.println("Civil status: " + residentCivilStatus[index]);
        System.out.println("Occupation: " + residentOccupation[index]);
        System.out.println("Contact number: " + residentContact[index]);
        System.out.println("Registered voter: " + (registeredVoter[index] ? "Yes" : "No"));
        if (residentHouseholdId[index] != 0) {
            System.out.println("Household ID: " + residentHouseholdId[index]);
        }
    }

    // Returns the active resident's array index for an ID, or -1 if not found.
    public static int findResidentById(int id) {
        for (int i = 0; i < MAX_RESIDENTS; i++) {
            if (active[i] && residentId[i] == id) {
                return i;
            }
        }
        return -1;
    }

    // Returns the first inactive resident slot, or -1 if none is available.
    public static int findFreeIndex() {
        for (int i = 0; i < MAX_RESIDENTS; i++) {
            if (!active[i]) {
                return i;
            }
        }
        return -1;
    }

    // Repeatedly prompts until the user enters a valid whole number.
    public static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String value = input.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Reads a valid date that is not in the future.
    public static LocalDate readDate(String message) {
            while (true) {
                System.out.print(message);
                String value = input.nextLine().trim();
                try {
                    LocalDate date = LocalDate.parse(value);
                    if (!date.isAfter(LocalDate.now())) {
                        return date;
                    }
                    System.out.println("Date cannot be in the future.");
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Use YYYY-MM-DD.");
                }
            }
        }

    // Reads a yes/no response and returns true for Y or false for N.
    public static boolean readYesNo(String message) {
            while (true) {
                System.out.print(message);
                String answer = input.nextLine().trim();
                if (answer.equalsIgnoreCase("Y")) {
                    return true;
                } else if (answer.equalsIgnoreCase("N")) {
                    return false;
                }
                System.out.println("Please enter Y or N.");
            }
        }

        // Reads and returns one of the supported gender values.
        public static String readGender() {
            while (true) {
                System.out.print("Enter gender (Male/Female/Other): ");
                String gender = input.nextLine().trim();
                if (gender.equalsIgnoreCase("Male")) {
                    return "Male";
                } else if (gender.equalsIgnoreCase("Female")) {
                    return "Female";
                } else if (gender.equalsIgnoreCase("Other")) {
                    return "Other";
                }
                System.out.println("Please enter Male, Female, or Other.");
            }
        }

    // Calculates a resident's age in years from their recorded birth date.
    public static int calculateAge(int residentIndex) {
            return java.time.Period.between(residentBirthDate[residentIndex], LocalDate.now()).getYears();
        }

    // Creates a household with an eligible resident as its head.
    public static void addHousehold() {
            if (householdCount >= MAX_HOUSEHOLDS) {
                System.out.println("Household storage is full.");
                return;
            }
            int headId = readInt("Enter the resident ID of the household head: ");
            int headIndex = findResidentById(headId);
            if (headIndex == -1) {
                System.out.println("Resident not found.");
                return;
            }
            if (residentHouseholdId[headIndex] != 0) {
                System.out.println("This resident already belongs to a household.");
                return;
            }

            int index = findInactiveHouseholdIndex();
            if (index == -1) {
                System.out.println("No household storage slot is available.");
                return;
            }
            householdId[index] = nextHouseholdId++;
            System.out.print("Enter household address: ");
            householdAddress[index] = input.nextLine().trim();
            householdHeadId[index] = headId;
            householdActive[index] = true;
            residentHouseholdId[headIndex] = householdId[index];
            householdCount++;
            System.out.println("Household created. Household ID: " + householdId[index]);
        }

    // Assigns an eligible resident to an existing household.
    public static void addHouseholdMember() {
            int id = readInt("Enter household ID: ");
            int householdIndex = findHouseholdIndex(id);
            if (householdIndex == -1) {
                System.out.println("Household not found.");
                return;
            }
            int memberId = readInt("Enter resident ID to add: ");
            int residentIndex = findResidentById(memberId);
            if (residentIndex == -1) {
                System.out.println("Resident not found.");
                return;
            }
            if (residentHouseholdId[residentIndex] != 0) {
                System.out.println("This resident already belongs to a household.");
                return;
            }
            residentHouseholdId[residentIndex] = id;
            System.out.println("Resident added to household.");
        }

    // Displays active households, their heads, and their assigned residents.
    public static void viewHouseholds() {
            if (householdCount == 0) {
                System.out.println("No household records found.");
                return;
            }
            for (int i = 0; i < MAX_HOUSEHOLDS; i++) {
                if (!householdActive[i]) {
                    continue;
                }
                System.out.println("\nHousehold ID: " + householdId[i]);
                System.out.println("Address: " + householdAddress[i]);
                int headIndex = findResidentById(householdHeadId[i]);
                System.out.println("Household head: " + (headIndex == -1 ? "Resident record unavailable" : residentName[headIndex]));
                System.out.println("Family members:");
                for (int j = 0; j < MAX_RESIDENTS; j++) {
                    if (active[j] && residentHouseholdId[j] == householdId[i]) {
                        System.out.println("- " + residentName[j] + " (ID: " + residentId[j] + ")");
                    }
                }
            }
        }

    // Returns the first inactive household slot, or -1 if none is available.
    public static int findInactiveHouseholdIndex() {
            for (int i = 0; i < MAX_HOUSEHOLDS; i++) {
                if (!householdActive[i]) {
                    return i;
                }
            }
            return -1;
        }

    // Returns the active household's array index for an ID, or -1 if not found.
    public static int findHouseholdIndex(int id) {
            for (int i = 0; i < MAX_HOUSEHOLDS; i++) {
                if (householdActive[i] && householdId[i] == id) {
                    return i;
                }
            }
            return -1;
        }

    // Records a new certificate request for an existing resident.
    public static void addCertificateRequest() {
            if (requestCount >= MAX_REQUESTS) {
                System.out.println("Certificate request storage is full.");
                return;
            }
            int resident = readInt("Enter resident ID: ");
            if (findResidentById(resident) == -1) {
                System.out.println("Resident not found.");
                return;
            }
            System.out.println("1. Barangay Clearance");
            System.out.println("2. Certificate of Residency");
            System.out.println("3. Certificate of Indigency");
            System.out.println("4. Business Clearance");
            int type = readInt("Select certificate type: ");
            if (type < 1 || type > 4) {
                System.out.println("Invalid certificate type.");
                return;
            }
            int index = findInactiveRequestIndex();
            if (index == -1) {
                System.out.println("No request storage slot is available.");
                return;
            }
            String[] types = {"Barangay Clearance", "Certificate of Residency", "Certificate of Indigency", "Business Clearance"};
            requestId[index] = nextRequestId++;
            requestResidentId[index] = resident;
            requestType[index] = types[type - 1];
            requestDate[index] = LocalDate.now();
            requestStatus[index] = "Pending";
            requestActive[index] = true;
            requestCount++;
            System.out.println("Request filed. Request ID: " + requestId[index] + ". Status: Pending.");
        }

    // Displays all active certificate requests and their current statuses.
    public static void viewCertificateRequests() {
            if (requestCount == 0) {
                System.out.println("No certificate requests found.");
                return;
            }
            for (int i = 0; i < MAX_REQUESTS; i++) {
                if (requestActive[i]) {
                    System.out.println("Request ID: " + requestId[i] + " | " + requestType[i]
                            + " | Resident ID: " + requestResidentId[i] + " | Date: " + requestDate[i]
                            + " | Status: " + requestStatus[i]);
                }
            }
        }

    // Marks a pending request as processed and displays the certificate details.
    public static void processCertificateRequest() {
            int id = readInt("Enter request ID to process: ");
            int index = findRequestIndex(id);
            if (index == -1) {
                System.out.println("Request not found.");
                return;
            }
            if (requestStatus[index].equals("Processed")) {
                System.out.println("This request has already been processed.");
                return;
            }
            int residentIndex = findResidentById(requestResidentId[index]);
            if (residentIndex == -1) {
                System.out.println("Resident record not found; request cannot be processed.");
                return;
            }
            requestStatus[index] = "Processed";
            System.out.println("\n========== " + requestType[index].toUpperCase() + " ==========");
            System.out.println("Request ID: " + requestId[index]);
            System.out.println("Resident ID: " + residentId[residentIndex]);
            System.out.println("Name: " + residentName[residentIndex]);
            System.out.println("Address: " + residentAddress[residentIndex]);
            System.out.println("Age: " + calculateAge(residentIndex));
            System.out.println("Civil status: " + residentCivilStatus[residentIndex]);
            System.out.println();
            System.out.println("This is to certify that the above-named resident is recorded");
            System.out.println("in this barangay. This certificate is issued upon request.");
            System.out.println("Date issued: " + LocalDate.now());
            System.out.println("Status: PROCESSED");
            System.out.println();
            System.out.println("__________________________");
            System.out.println("Authorized Barangay Official");
            System.out.println("========================================");
            System.out.println("Certificate displayed for printing.");
        }

    // Returns the first inactive request slot, or -1 if none is available.
    public static int findInactiveRequestIndex() {
            for (int i = 0; i < MAX_REQUESTS; i++) {
                if (!requestActive[i]) {
                    return i;
                }
            }
            return -1;
        }

    // Returns the active request's array index for an ID, or -1 if not found.
    public static int findRequestIndex(int id) {
            for (int i = 0; i < MAX_REQUESTS; i++) {
                if (requestActive[i] && requestId[i] == id) {
                    return i;
                }
            }
            return -1;
        }

    // Records a new blotter incident with an initial open status.
    public static void addIncident() {
            if (incidentCount >= MAX_INCIDENTS) {
                System.out.println("Blotter storage is full.");
                return;
            }
            int index = findInactiveIncidentIndex();
            if (index == -1) {
                System.out.println("No incident storage slot is available.");
                return;
            }
            incidentId[index] = nextIncidentId++;
            incidentComplainant[index] = readRequiredText("Enter complainant: ");
            incidentRespondent[index] = readRequiredText("Enter respondent: ");
            incidentDetails[index] = readRequiredText("Enter incident details: ");
            incidentDate[index] = readDate("Enter incident date (YYYY-MM-DD): ");
            incidentStatus[index] = "Open";
            incidentActive[index] = true;
            incidentCount++;
            System.out.println("Incident recorded. Blotter ID: " + incidentId[index]);
        }

    // Displays the details and statuses of all active incidents.
    public static void viewIncidents() {
            if (incidentCount == 0) {
                System.out.println("No incidents recorded.");
                return;
            }
            for (int i = 0; i < MAX_INCIDENTS; i++) {
                if (incidentActive[i]) {
                    System.out.println("\nBlotter ID: " + incidentId[i] + " | Date: " + incidentDate[i]
                            + " | Status: " + incidentStatus[i]);
                    System.out.println("Complainant: " + incidentComplainant[i]);
                    System.out.println("Respondent: " + incidentRespondent[i]);
                    System.out.println("Details: " + incidentDetails[i]);
                }
            }
        }

    // Changes an incident's status to open or resolved.
    public static void updateIncidentStatus() {
            int id = readInt("Enter blotter ID: ");
            int index = findIncidentIndex(id);
            if (index == -1) {
                System.out.println("Incident not found.");
                return;
            }
            System.out.println("1. Open");
            System.out.println("2. Resolved");
            int status = readInt("Select status: ");
            if (status == 1) {
                incidentStatus[index] = "Open";
            } else if (status == 2) {
                incidentStatus[index] = "Resolved";
            } else {
                System.out.println("Invalid status.");
                return;
            }
            System.out.println("Incident status updated.");
        }

    // Returns the first inactive incident slot, or -1 if none is available.
    public static int findInactiveIncidentIndex() {
            for (int i = 0; i < MAX_INCIDENTS; i++) {
                if (!incidentActive[i]) {
                    return i;
                }
            }
            return -1;
        }

    // Returns the active incident's array index for an ID, or -1 if not found.
    public static int findIncidentIndex(int id) {
            for (int i = 0; i < MAX_INCIDENTS; i++) {
                if (incidentActive[i] && incidentId[i] == id) {
                    return i;
                }
            }
            return -1;
        }

    // Creates an account and assigns one of the supported user roles.
    public static void createManagedUser() {
            if (userCount >= MAX_USERS) {
                System.out.println("User storage is full.");
                return;
            }
            System.out.print("Enter username: ");
            String username = input.nextLine().trim();
            if (username.isEmpty() || usernameExists(username)) {
                System.out.println("Username cannot be empty or already in use.");
                return;
            }
            System.out.print("Enter password: ");
            String password = input.nextLine();
            if (password.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }
            System.out.println("1. Administrator");
            System.out.println("2. Barangay Staff");
            System.out.println("3. Barangay Secretary");
            int role = readInt("Select role: ");
            if (role < 1 || role > 3) {
                System.out.println("Invalid role.");
                return;
            }
            String[] roles = {"Administrator", "Barangay Staff", "Barangay Secretary"};
            usernames[userCount] = username;
            passwords[userCount] = password;
            userRoles[userCount] = roles[role - 1];
            userCount++;
            System.out.println("User account created.");
        }

    // Changes another user's role after validating the account and selection.
    public static void changeUserRole() {
            System.out.print("Enter username: ");
            String username = input.nextLine().trim();
            int index = findUsernameIndex(username);
            if (index == -1) {
                System.out.println("User not found.");
                return;
            }
            if (index == currentUserIndex) {
                System.out.println("You cannot change your own role.");
                return;
            }
            System.out.println("1. Administrator");
            System.out.println("2. Barangay Staff");
            System.out.println("3. Barangay Secretary");
            int role = readInt("Select new role: ");
            if (role < 1 || role > 3) {
                System.out.println("Invalid role.");
                return;
            }
            String[] roles = {"Administrator", "Barangay Staff", "Barangay Secretary"};
            userRoles[index] = roles[role - 1];
            System.out.println("User role updated.");
        }

    // Lists all registered usernames and their assigned roles.
    public static void viewUsers() {
            for (int i = 0; i < userCount; i++) {
                System.out.println(usernames[i] + " - " + userRoles[i]);
            }
        }

    // Returns the account index matching a username, or -1 if not found.
    public static int findUsernameIndex(String username) {
            for (int i = 0; i < userCount; i++) {
                if (usernames[i].equalsIgnoreCase(username)) {
                    return i;
                }
            }
            return -1;
        }

    // Calculates and displays resident demographics and current-month request totals.
    public static void showReports() {
            int maleCount = 0;
            int femaleCount = 0;
            int seniorCount = 0;
            int minorCount = 0;
            int voterCount = 0;
            int currentMonthRequests = 0;

            for (int i = 0; i < MAX_RESIDENTS; i++) {
                if (!active[i]) {
                    continue;
                }
                if (residentGender[i].equalsIgnoreCase("Male")) {
                    maleCount++;
                } else if (residentGender[i].equalsIgnoreCase("Female")) {
                    femaleCount++;
                }
                int age = calculateAge(i);
                if (age >= 60) {
                    seniorCount++;
                }
                if (age < 18) {
                    minorCount++;
                }
                if (registeredVoter[i]) {
                    voterCount++;
                }
            }
            for (int i = 0; i < MAX_REQUESTS; i++) {
                if (requestActive[i] && requestDate[i].getMonth() == LocalDate.now().getMonth()
                        && requestDate[i].getYear() == LocalDate.now().getYear()) {
                    currentMonthRequests++;
                }
            }

            System.out.println("\n------ BARANGAY REPORTS ------");
            System.out.println("Total residents: " + residentCount);
            System.out.println("Male residents: " + maleCount);
            System.out.println("Female residents: " + femaleCount);
            System.out.println("Senior citizens (60+): " + seniorCount);
            System.out.println("Minors (under 18): " + minorCount);
            System.out.println("Registered voters: " + voterCount);
            System.out.println("Certificate requests this month: " + currentMonthRequests);
        }

    // Prompts until the user enters non-empty text, then returns the trimmed value.
    public static String readRequiredText(String message) {
        System.out.print(message);
        String value = input.nextLine().trim();
        while (value.isEmpty()) {
            System.out.print("This field cannot be empty. " + message);
            value = input.nextLine().trim();
        }
        return value;
    }
}
