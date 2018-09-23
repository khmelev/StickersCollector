package ru.av3969.stickerscollector.ui.vo;

import java.util.Date;

import ru.av3969.stickerscollector.data.db.entity.Transaction;

public class TransactionVO {

    private Long id;

    private Date date;

    private String title;

    private Integer quantity;

    private Boolean active;

    private Transaction transaction;

    private String transStickersText;

    public TransactionVO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.title = transaction.getTitle();
        this.quantity = transaction.getQuantity();
        this.active = transaction.getActive();
        this.transaction = transaction;
        this.transStickersText = "";
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getActive() {
        return active;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getTransStickersText() {
        return transStickersText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setTransStickersText(String transStickersText) {
        this.transStickersText = transStickersText;
    }
}
