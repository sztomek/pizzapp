package hu.sztomek.pizzapp.domain.model

data class OpeningHour(val daysOfWeek: DaysOfWeek, val fromMs: Long, val toMs: Long)