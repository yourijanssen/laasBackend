package com.projectLaas.projectLaas.modelDTO;

public class BookCopyDTO {

    private Long bookId;
    private String condition;
    private String status;

    // Constructors
    public BookCopyDTO() {}

    public BookCopyDTO(Long bookId, String condition, String status) {
        this.bookId = bookId;
        this.condition = condition;
        this.status = status;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

