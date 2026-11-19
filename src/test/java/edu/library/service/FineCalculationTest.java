package edu.library.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FineCalculationTest {
    @Test void onTimeReturnHasNoFine(){assertEquals(0,FineCalculator.overdueDays(LocalDate.of(2025,1,15),LocalDate.of(2025,1,15)));assertEquals(new BigDecimal("0.00"),FineCalculator.amount(0));}
    @Test void lateReturnChargesFivePerOverdueDay(){int days=FineCalculator.overdueDays(LocalDate.of(2025,1,15),LocalDate.of(2025,1,18));assertEquals(3,days);assertEquals(new BigDecimal("15.00"),FineCalculator.amount(days));}
    @Test void earlyReturnHasNoFine(){assertEquals(0,FineCalculator.overdueDays(LocalDate.of(2025,1,15),LocalDate.of(2025,1,10)));}
}
