package com.library.controller;

import com.library.model.Book;
import com.library.model.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class LibraryController {
    private List<Book> books;
    private List<Member> members;
    
    private static final String BOOKS_FILE = "books.dat";
    private static final String MEMBERS_FILE = "members.dat";
    
    public LibraryController() {
        boolean dataLoaded = loadData();
         
        if (!dataLoaded) {
            this.books = new ArrayList<>();
            this.members = new ArrayList<>();
            
            addSampleData();
        }
    }
    
    private void addSampleData() {
        // Sample books
        books.add(new Book("B001", "Java Programming", "John Smith"));
        books.add(new Book("B002", "Data Structures", "Jane Johnson"));
        books.add(new Book("B003", "Algorithm Design", "Robert Martin"));
        
        // Sample members
        members.add(new Member("M001", "Ali Yilmaz", "ali@example.com"));
        members.add(new Member("M002", "Ayşe Demir", "ayse@example.com"));
        members.add(new Member("M003", "Sevgi Saygın", "sevgi@example.com"));
        members.add(new Member("M004", "Baran Sonmez", "baran@example.com"));
        
        saveData();
    }
    
    public boolean saveData() {
        boolean result = true;
        
        // Save Books
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.err.println("Kitap verisi kaydedilemedi: " + e.getMessage());
            result = false;
        }
        
        // Save Members
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.err.println("Üye verisi kaydedilemedi: " + e.getMessage());
            result = false;
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private boolean loadData() {
        boolean result = true;
        
        // Load the books
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Book data could not be loaded: " + e.getMessage());
            result = false;
        }
        
        // Load the members
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
            members = (List<Member>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Member data could not be loaded: " + e.getMessage());
            result = false;
        }
        
        return result;
    }
    
    public boolean addBook(Book book) {
        // Check if book with same ID already exists
        for (Book existingBook : books) {
            if (existingBook.getId().equals(book.getId())) {
                return false; 
            }
        }
        boolean result = books.add(book);
        if (result) {
            saveData();
        }
        return result;
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books); 
    }
    
    public Book getBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }
    
    public boolean deleteBook(String id) {
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getId().equals(id)) {
                bookToRemove = book;
                break;
            }
        }
        
        if (bookToRemove != null) {
            boolean result = books.remove(bookToRemove);
            if (result) {
                saveData(); 
            }
            return result;
        }
        return false;
    }
    
    public List<Book> searchBooks1(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        List<Book> result = new ArrayList<>();
        
        for (Book book : books) {
            if (book.getId().toLowerCase().contains(lowercaseQuery) || 
                book.getTitle().toLowerCase().contains(lowercaseQuery) ||
                book.getAuthor().toLowerCase().contains(lowercaseQuery)) {
                result.add(book);
            }
        }
    
        return result;
    }

    public List<Member> searchMembers1(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        List<Member> result = new ArrayList<>();
        
        for (Member member : members) {
            if (member.getId().toLowerCase().contains(lowercaseQuery) || 
                member.getName().toLowerCase().contains(lowercaseQuery) ||
                member.getEmail().toLowerCase().contains(lowercaseQuery)) {
                result.add(member);
            }
        }
        
        return result;
    }
    
    public boolean addMember(Member member) {
        // Check if member with same ID already exists
        for (Member existingMember : members) {
            if (existingMember.getId().equals(member.getId())) {
                return false; // Member with this ID already exists
            }
        }
        boolean result = members.add(member);
        if (result) {
            saveData(); 
        }
        return result;
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
    
    public Member getMemberById(String id) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }
    
    public boolean deleteMember(String id) {
        Member memberToRemove = null;
        for (Member member : members) {
            if (member.getId().equals(id)) {
                memberToRemove = member;
                break;
            }
        }
        
        if (memberToRemove != null) {
            boolean result = members.remove(memberToRemove);
            if (result) {
                saveData();
            }
            return result;
        }
        return false;
    }
    
    public boolean borrowBook(String bookId) {
        Book book = getBookById(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            saveData(); 
            return true;
        }
        return false;
    }
    
    public boolean returnBook(String bookId) {
        Book book = getBookById(bookId);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            saveData(); 
            return true;
        }
        return false;
    }
 
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        List<Book> result = new ArrayList<>();
        
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowercaseQuery) || 
                book.getAuthor().toLowerCase().contains(lowercaseQuery) ||
                book.getId().toLowerCase().contains(lowercaseQuery)) {
                result.add(book);
            }
        }
        
        return result;
    }
    
    public List<Member> searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        List<Member> result = new ArrayList<>();
        
        for (Member member : members) {
            if (member.getName().toLowerCase().contains(lowercaseQuery) || 
                member.getEmail().toLowerCase().contains(lowercaseQuery) ||
                member.getId().toLowerCase().contains(lowercaseQuery)) {
                result.add(member);
            }
        }
        
        return result;
    }
}