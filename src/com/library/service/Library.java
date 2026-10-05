package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;

import java.util.ArrayList;
import java.util.List;


public class Library {
    
    private List<Book> books;
    private List<Member> members;
    private List<BorrowRecord> borrowRecords;
    private int recordIdSequence;

    public Library() {
        this.books = new ArrayList<Book>();
        this.members = new ArrayList<Member>();
        this.borrowRecords = new ArrayList<BorrowRecord>();
        this.recordIdSequence = 1000;
        seedSampleData();
    }

    // ==========================================
    // Book Management -- (BookService walata dannaona ewa) -- theekshana dasan 
    // ==========================================

    /*
      Adds a new book  - 
     */

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book details cannot be null.");
        }
        if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (findBookById(book.getBookId().trim()) != null) {
            throw new IllegalArgumentException("A book with ID '" + book.getBookId().trim() + "' already exists.");
        }
        
        book.setBookId(book.getBookId().trim());
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor() != null ? book.getAuthor().trim() : "Unknown");
        book.setCategory(book.getCategory() != null ? book.getCategory().trim() : "General");
        
        books.add(book);
    }

    /*
      Removes a book by ID.
     */

    public boolean removeBook(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Book ID to remove.");
        }
        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' not found.");
        }
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Cannot remove Book '" + book.getTitle() + "' because it is currently borrowed.");
        }
        return books.remove(book);
    }

    /*
     Searches for books matching the keyword in ID, Title, Author, or Category
     */

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<Book>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<Book>(books);
        }
        String lowerKey = keyword.trim().toLowerCase();
        for (Book b : books) {
            if (b.getBookId().toLowerCase().contains(lowerKey) ||
                b.getTitle().toLowerCase().contains(lowerKey) ||
                b.getAuthor().toLowerCase().contains(lowerKey) ||
                b.getCategory().toLowerCase().contains(lowerKey)) {
                results.add(b);
            }
        }
        return results;
    }

    /*
      Finds a single book by exact ID.
     */

    public Book findBookById(String bookId) {
        if (bookId == null) return null;
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId.trim())) {
                return b;
            }
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<Book>(books);
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<Book>();
        for (Book b : books) {
            if (b.isAvailable()) {
                available.add(b);
            }
        }
        return available;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowed = new ArrayList<Book>();
        for (Book b : books) {
            if (!b.isAvailable()) {
                borrowed.add(b);
            }
        }
        return borrowed;
    }

    // ==========================================
    // Member Management -- (MemberService walata dannaona ewa) -- aloka dulmi
    // ==========================================

    /*
     Adds a new member after validation.
     */

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member details cannot be null.");
        }
        if (member.getMemberId() == null || member.getMemberId().trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        if (findMemberById(member.getMemberId().trim()) != null) {
            throw new IllegalArgumentException("A member with ID '" + member.getMemberId().trim() + "' already exists.");
        }

        member.setMemberId(member.getMemberId().trim());
        member.setName(member.getName().trim());
        member.setContactNumber(member.getContactNumber() != null ? member.getContactNumber().trim() : "-");

        members.add(member);
    }

    /*
     Removes a member by ID.
     */

    public boolean removeMember(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Member ID to remove.");
        }
        Member member = findMemberById(memberId.trim());
        if (member == null) {
            throw new IllegalArgumentException("Member with ID '" + memberId.trim() + "' not found.");
        }
        // Check if member has active borrowed books
        for (BorrowRecord record : borrowRecords) {
            if (record.isActive() && record.getMember().getMemberId().equalsIgnoreCase(member.getMemberId())) {
                throw new IllegalArgumentException("Cannot remove Member '" + member.getName() + "' because they currently have borrowed books.");
            }
        }
        return members.remove(member);
    }

    /*
      Searches for members matching keyword in ID, Name, or Contact.
     */

    public List<Member> searchMembers(String keyword) {
        List<Member> results = new ArrayList<Member>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<Member>(members);
        }
        String lowerKey = keyword.trim().toLowerCase();
        for (Member m : members) {
            if (m.getMemberId().toLowerCase().contains(lowerKey) ||
                m.getName().toLowerCase().contains(lowerKey) ||
                m.getContactNumber().toLowerCase().contains(lowerKey)) {
                results.add(m);
            }
        }
        return results;
    }

    /*
      Finds a single member by ID.
     */

    public Member findMemberById(String memberId) {
        if (memberId == null) return null;
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId.trim())) {
                return m;
            }
        }
        return null;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<Member>(members);
    }

    // ==========================================
    // Borrow & Return Management -- (BorrowService walata dannaona ewa) -- samindi tharushi
    // ==========================================

    /*
     Borrows a book for a member.
     Validates availability and existence.
     */

    public BorrowRecord borrowBook(String bookId, String memberId, String borrowDate) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Book ID.");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Member ID.");
        }

        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' does not exist.");
        }

        Member member = findMemberById(memberId.trim());
        if (member == null) {
            throw new IllegalArgumentException("Member with ID '" + memberId.trim() + "' does not exist.");
        }

        // Validate availability
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book '" + book.getTitle() + "' is already BORROWED and cannot be borrowed again.");
        }

        // Update book status
        book.markAsBorrowed();

        // Create and register borrow record
        recordIdSequence++;
        String recId = "REC-" + recordIdSequence;
        String date = (borrowDate != null && !borrowDate.trim().isEmpty()) ? borrowDate.trim() : java.time.LocalDate.now().toString();

        BorrowRecord record = new BorrowRecord(recId, book, member, date);
        borrowRecords.add(0, record); // add most recent at top

        return record;
    }

    /*
      Returns a borrowed book.
      Updates status to Available and updates the borrowing record.
     */

    public BorrowRecord returnBook(String bookId, String returnDate) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select or enter a Book ID to return.");
        }

        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' does not exist.");
        }

        if (book.isAvailable()) {
            throw new IllegalArgumentException("Book '" + book.getTitle() + "' is already AVAILABLE. You cannot return a book that is not borrowed.");
        }

        // Find active borrow record for this book
        BorrowRecord targetRecord = null;
        for (BorrowRecord record : borrowRecords) {
            if (record.isActive() && record.getBook().getBookId().equalsIgnoreCase(book.getBookId())) {
                targetRecord = record;
                break;
            }
        }

        String date = (returnDate != null && !returnDate.trim().isEmpty()) ? returnDate.trim() : java.time.LocalDate.now().toString();

        if (targetRecord != null) {
            targetRecord.markAsReturned(date);
        } else {
            // In case no record found, still restore book status
            book.markAsAvailable();
        }

        return targetRecord;
    }

    /*
     Finds active borrow record for a book.
     */

    public BorrowRecord getActiveRecordForBook(String bookId) {
        if (bookId == null) return null;
        for (BorrowRecord r : borrowRecords) {
            if (r.isActive() && r.getBook().getBookId().equalsIgnoreCase(bookId.trim())) {
                return r;
            }
        }
        return null;
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<BorrowRecord>(borrowRecords);
    }

    // ==========================================
    // Dashboard & Statistics -- (DashboardService walata dannaona ewa) -- shehara
    // ==========================================

    public int getTotalBooksCount() {
        return books.size();
    }

    public int getAvailableBooksCount() {
        int count = 0;
        for (Book b : books) {
            if (b.isAvailable()) count++;
        }
        return count;
    }

    public int getBorrowedBooksCount() {
        int count = 0;
        for (Book b : books) {
            if (!b.isAvailable()) count++;
        }
        return count;
    }

    public int getTotalMembersCount() {
        return members.size();
    }

    // ==========================================
    // Seed Sample Data    --- thinuka
    // ==========================================

    private void seedSampleData() {
        // Books
        books.add(new Book("B001", "Introduction to Java Programming", "Y. Daniel Liang", "Technology", Book.STATUS_AVAILABLE));
        books.add(new Book("B002", "Clean Code: Agile Software Craftsmanship", "Robert C. Martin", "Technology", Book.STATUS_AVAILABLE));
        books.add(new Book("B003", "Design Patterns: Elements of Reusable Object-Oriented Software", "Erich Gamma et al.", "Computer Science", Book.STATUS_AVAILABLE));
        books.add(new Book("B004", "Effective Java", "Joshua Bloch", "Technology", Book.STATUS_BORROWED));
        books.add(new Book("B005", "Database System Concepts", "Abraham Silberschatz", "Database", Book.STATUS_AVAILABLE));
        books.add(new Book("B006", "Head First Java", "Kathy Sierra & Bert Bates", "Technology", Book.STATUS_BORROWED));
        books.add(new Book("B007", "Artificial Intelligence: A Modern Approach", "Stuart Russell", "AI & Data", Book.STATUS_AVAILABLE));
        books.add(new Book("B008", "Object-Oriented Analysis and Design", "Grady Booch", "Software Engineering", Book.STATUS_AVAILABLE));

        // Members
        members.add(new Member("M001", "Kasun Perera", "0771234567"));
        members.add(new Member("M002", "Nimal Fernando", "0719876543"));
        members.add(new Member("M003", "Dilini Jayawardena", "0765554321"));
        members.add(new Member("M004", "Sachithra Silva", "0751122334"));
        members.add(new Member("M005", "Anuki Bandara", "0789988776"));

        // Borrow Records
        BorrowRecord rec1 = new BorrowRecord("REC-1001", books.get(3), members.get(0), "2026-09-25", "-", BorrowRecord.STATUS_BORROWED);
        BorrowRecord rec2 = new BorrowRecord("REC-1002", books.get(5), members.get(2), "2026-09-28", "-", BorrowRecord.STATUS_BORROWED);
        BorrowRecord rec3 = new BorrowRecord("REC-1003", books.get(0), members.get(1), "2026-09-10", "2026-09-20", BorrowRecord.STATUS_RETURNED);

        borrowRecords.add(rec1);
        borrowRecords.add(rec2);
        borrowRecords.add(rec3);
        recordIdSequence = 1003;
    }
}
