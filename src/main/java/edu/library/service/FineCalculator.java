package edu.library.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class FineCalculator {
    public static final BigDecimal RATE_PER_DAY = new BigDecimal("5.00");
    private FineCalculator() {}
    public static int overdueDays(LocalDate dueDate, LocalDate returnDate) {
        return (int) Math.max(0, ChronoUnit.DAYS.between(dueDate, returnDate));
    }
    public static BigDecimal amount(int overdueDays) {
        if (overdueDays < 0) throw new IllegalArgumentException("Overdue days cannot be negative");
        return RATE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
    }
}
