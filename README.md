# Library Management System (Java & Java Swing)

**Course:** CST 124-2 Object Oriented Programming — Final Assessment  
**Institution:** Uva Wellassa University of Sri Lanka — Faculty of Applied Sciences, Department of Computer Science and Informatics  

---

## 📌 Project Overview
This is a desktop-based **Library Management System** developed strictly according to the CST 124-2 assessment specification. The application provides a clean, modern, and user-friendly graphical user interface built with standard **Java Swing** and pure **Java Standard Edition (Java 8+)**, without requiring external libraries or database installations. All application data is managed using Java collections (`ArrayList`) adhering to Object-Oriented Programming (OOP) best practices.

---

## 🏗️ Architecture & OOP Implementation

The codebase is organized into modular packages separating the Presentation Layer, Business Logic Layer, and Data Models:

```
src/
└── com/
    └── library/
        ├── Main.java                 # Application entry point (L&F and EDT setup)
        ├── model/                    # Domain Entities & Encapsulation
        │   ├── Book.java             # Book entity (ID, Title, Author, Category, Status)
        │   ├── Member.java           # Member entity (ID, Name, Contact)
        │   └── BorrowRecord.java     # Borrow transaction entity (Associates Book & Member)
        ├── service/                  # Business Logic & Collections Management
        │   └── Library.java          # Core logic (ArrayList storage, validations, borrow/return)
        └── ui/                       # Java Swing Presentation Layer
            ├── MainFrame.java        # Main window with dark sidebar & CardLayout navigation
            ├── UIUtils.java          # Modern color palette, custom buttons & table formatters
            ├── DashboardPanel.java   # Stat cards (Total, Available, Borrowed, Members) & recent logs
            ├── BookPanel.java        # Add, search, view, category filter, and remove books
            ├── MemberPanel.java      # Register, search, view, and remove library members
            ├── BorrowPanel.java      # Issue books with real-time preview & availability validation
            ├── ReturnPanel.java      # Process returns, update records & restore book availability
            └── HistoryPanel.java     # Full searchable & filterable borrowing history
```

---

## 💡 Key OOP Concepts Demonstrated (For Assessment & Video Defense)

| OOP Concept | How It Is Implemented in This System |
| :--- | :--- |
| **Classes & Objects** | Dedicated domain classes (`Book`, `Member`, `BorrowRecord`, `Library`) instantiated as real-world objects representing library entities. |
| **Encapsulation** | All entity fields are declared `private`. Access and modifications are strictly governed via public getters, setters, and domain methods (`markAsBorrowed()`, `markAsAvailable()`, `markAsReturned()`). |
| **Constructors & Methods** | Default and parameterized constructors initialize objects with sensible defaults. Methods implement business rules rather than leaving logic inside button listeners. |
| **Object Relationships** | **Association / Aggregation:** `BorrowRecord` has-a `Book` and has-a `Member`.<br>**Composition / Aggregation:** `Library` manages collections of `Book`, `Member`, and `BorrowRecord`. |
| **Collections (`ArrayList`)** | `books`, `members`, and `borrowRecords` are managed in memory via `ArrayList`, fulfilling the requirement that a database is not required. |
| **Separation of Concerns** | Application logic resides entirely inside `Library.java`. Swing UI panels only capture input and display results, satisfying instruction requirement #5. |
| **Event Handling** | Comprehensive action listeners, table selection listeners, and document filters. |

---

## ✨ System Features & Assessment Compliance

1. **Dashboard:**
   - 4 live statistic cards: Total Books, Available Books, Borrowed Books, Registered Members.
   - Quick activity table showing currently active loans.
2. **Book Management:**
   - Form to add new books (Book ID, Title, Author, Category).
   - Real-time catalog table with alternating rows and colored availability badges.
   - Keyword search across ID, Title, Author, and Category.
   - Category filtering (Technology, Computer Science, Database, AI, etc.).
   - Book removal with prevention if book is currently borrowed.
3. **Member Management:**
   - Register member (Member ID, Full Name, Contact Number).
   - Search members by ID, name, or phone.
   - Remove member (with protection preventing deletion of members who have unreturned books).
4. **Borrow Book Operation:**
   - Selection of active member and available book from dropdowns or by clicking table rows.
   - Real-time preview card displaying details of selected book and member.
   - Automatic availability check: Rejects already borrowed books.
   - Changes book status to "Borrowed" and creates a `BorrowRecord`.
5. **Return Book Operation:**
   - Select currently borrowed book.
   - Displays loan details (Borrower, Record ID, Issue Date).
   - Updates book status back to "Available".
   - Updates borrowing record with return date and status "Returned".
6. **Borrowing History Log:**
   - Complete log of all past and active borrowing transactions.
   - Filter by status ("Borrowed" or "Returned") and keyword search.

---

## 🛡️ Input Validations Handled

- **Empty Field Validation:** Warns if Book ID, Member ID, Title, or Name are left blank.
- **Duplicate ID Validation:** Rejects adding duplicate Book IDs or duplicate Member IDs with informative error dialogs.
- **Double Borrowing Prevention:** Prevents borrowing an already borrowed book.
- **Invalid Return Prevention:** Prevents returning a book that is already available.
- **Non-Existent Search Notification:** Informs user if search yields no results.
- **Active Loan Protection:** Prevents removing a book or deleting a member while an active loan is ongoing.

---

## 🚀 How to Run the Application

### Option 1: Quick Launch (Double-click `run.bat`)
Simply double-click the **`run.bat`** file located in the root directory. It will automatically compile all source files and launch the application!

### Option 2: Run in IntelliJ IDEA / NetBeans / Eclipse
1. Open the project directory.
2. Navigate to `src` → `com` → `library` → **`Main.java`**.
3. Right-click **`Main.java`** and click **Run 'Main.main()'** (or press the green **`▶`** button).

### Option 3: Run with Terminal (PowerShell)
```powershell
# 1. Compile all source files into the bin folder
javac -d bin (Get-ChildItem -Path src -Recurse -Filter *.java).FullName

# 2. Launch the application
java -cp bin com.library.Main
```
