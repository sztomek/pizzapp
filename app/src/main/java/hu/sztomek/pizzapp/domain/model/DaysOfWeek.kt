package hu.sztomek.pizzapp.domain.model

sealed class DaysOfWeek {

    object MONDAY: DaysOfWeek()
    object TUESDAY: DaysOfWeek()
    object WEDNESDAY: DaysOfWeek()
    object THURSDAY: DaysOfWeek()
    object FRIDAY: DaysOfWeek()
    object SATURDAY: DaysOfWeek()
    object SUNDAY: DaysOfWeek()

}