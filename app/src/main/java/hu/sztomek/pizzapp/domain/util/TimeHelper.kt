package hu.sztomek.pizzapp.domain.util

import hu.sztomek.pizzapp.domain.model.DaysOfWeek
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.lang.IllegalArgumentException
import java.util.*

object TimeHelper {

    private const val MS_TO_SEC = 1000L
    private const val SEC_TO_MIN = 60L
    private const val MIN_TO_HOUR = 60L

    private val OPENING_TIME_FORMATTER = DateTimeFormat.forPattern("hh:mm a")

    fun currentDayOfWeek(): DaysOfWeek {
        val calendar = Calendar.getInstance()
        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> DaysOfWeek.MONDAY
            Calendar.TUESDAY -> DaysOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DaysOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DaysOfWeek.THURSDAY
            Calendar.FRIDAY -> DaysOfWeek.FRIDAY
            Calendar.SATURDAY -> DaysOfWeek.SATURDAY
            Calendar.SUNDAY -> DaysOfWeek.SUNDAY
            else -> throw IllegalStateException("Invalid day of week received")
        }
    }

    fun dayTextToDaysOfWeek(day: String): DaysOfWeek {
        return when (day) {
            "Monday" -> DaysOfWeek.MONDAY
            "Tuesday" -> DaysOfWeek.TUESDAY
            "Wednesday" -> DaysOfWeek.WEDNESDAY
            "Thursday" -> DaysOfWeek.THURSDAY
            "Friday" -> DaysOfWeek.FRIDAY
            "Saturday" -> DaysOfWeek.SATURDAY
            "Sunday" -> DaysOfWeek.SUNDAY
            else -> throw IllegalArgumentException("invalid day received: $day")
        }
    }

    fun parseTimeToMs(time: String): Long {
        return DateTime.parse(time, OPENING_TIME_FORMATTER).millisOfDay().get().toLong()
    }

    private fun msToHours(timeMs: Long): Int {
        return (timeMs.toDouble() / MS_TO_SEC / SEC_TO_MIN / MIN_TO_HOUR).toInt()
    }

    private fun msToMinsRemaining(timeMs: Long): Int {
        return (timeMs.toDouble() / MS_TO_SEC / SEC_TO_MIN % MIN_TO_HOUR).toInt()
    }

    fun isNowBetween(startMs: Long, endMs: Long): Boolean {
        val opens = DateTime.now().withHourOfDay(msToHours(startMs)).withMinuteOfHour(msToMinsRemaining(startMs))
        val closes = DateTime.now().withHourOfDay(msToHours(endMs)).withMinuteOfHour(msToMinsRemaining(endMs))

        return opens.isBeforeNow && closes.isAfterNow
    }

}