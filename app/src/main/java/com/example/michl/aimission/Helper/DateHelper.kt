package com.example.michl.aimission.Helper

import java.time.LocalDate

class DateHelper {
    companion object DateHelper {
        var current = LocalDate.now()

        fun getCurrentMonth(): Int {
            return current.month.value
        }

        fun getCurrentYear(): Int {
            return current.year
        }
    }
}
