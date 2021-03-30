package ru.kmikhails.accountcare.util;

public class RowNumberHolder {
    private final ThreadLocal<Integer> rowNumberThreadLocal = new ThreadLocal<>();

    public void setRowNumber(Integer rowNumber) {
        rowNumberThreadLocal.set(rowNumber);
    }

    public void resetRowNumber() {
        rowNumberThreadLocal.remove();
    }

    public Integer getRowNumber() {
        return rowNumberThreadLocal.get();
    }
}
