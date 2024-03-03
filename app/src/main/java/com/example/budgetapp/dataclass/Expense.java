package com.example.budgetapp.dataclass;

public class Expense {

    String note, date, type, amount, expenseType;

    public Expense() {
    }

    public Expense(String note, String date, String type, String amount, String expenseType) {
        this.note = note;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.expenseType = expenseType;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
