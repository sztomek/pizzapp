package hu.sztomek.pizzapp.domain.model

import hu.sztomek.pizzapp.domain.util.TimeHelper

data class Place(val id: String,
                 val name: String,
                 val location: Location,
                 val thumbnail: String,
                 val openingHours: List<OpeningHour>) {

    val open: Boolean
        get() {
            val openingThisDay = openingHours.firstOrNull { it.daysOfWeek == TimeHelper.currentDayOfWeek() }
            return openingThisDay != null && TimeHelper.isNowBetween(openingThisDay.fromMs, openingThisDay.toMs)

        }

}