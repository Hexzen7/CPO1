# Running the Barangay Record Management System

## Requirements

- A computer with Java Development Kit (JDK) 8 or later installed.
- The source file `BarangayRecordManagementSystem.java`.

The JDK provides both `javac` (the Java compiler) and `java` (the program launcher).

## Open a terminal in the project folder

On Windows, open **PowerShell** or **Command Prompt** and change to the folder containing the source file:

```powershell
cd "C:\Users\rain3\Downloads\CPO1"
```

If you saved the project in a different folder, use that folder's path instead.

## Compile the program

Run:

```powershell
javac BarangayRecordManagementSystem.java
```

If compilation succeeds, Java creates `BarangayRecordManagementSystem.class` in the same folder. If the terminal says `javac` is not recognized, install a JDK and make sure its `bin` folder is available in your `PATH`.

## Start the program

Run:

```powershell
java BarangayRecordManagementSystem
```

## Log in or sign up

At the account access screen:

- Enter `1` to log in.
- Enter `2` to sign up for a new Barangay Staff account.
- Enter `3` to exit.

The default Administrator account is:

- **Username:** `admin`
- **Password:** `admin123`

Login allows up to three incorrect attempts. Sign-up asks for a username and password. After creating an account, choose the login option and enter the new credentials.

## Use the menus

After login, enter the number shown next to the feature you want, then follow the prompts. For example, use the Resident Management menu to add, view, search, update, or delete resident records. Use the logout option to return to account access, or the exit option to end the program.

Some options depend on the logged-in account's role. Sign-up creates Barangay Staff accounts; the default `admin` account has the Administrator role.

## Important data note

The program currently keeps accounts, residents, households, certificate requests, and blotter incidents in memory while it is running. **They are not saved to a database or file.** Closing the program loses data entered during that run.
