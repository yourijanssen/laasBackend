package com.projectLaas.projectLaas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_copies")
public class BookCopy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Establishing many-to-one relationship
    @ManyToOne(fetch = FetchType.LAZY) // Optional: LAZY is good for performance
    @JoinColumn(name = "book_id", nullable = false) // Defines the foreign key
    private Book book;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String status;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
