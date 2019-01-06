package hu.sztomek.pizzapp.data.converter

import hu.sztomek.pizzapp.data.model.FriendWebModel
import hu.sztomek.pizzapp.data.model.PlaceDbModel
import hu.sztomek.pizzapp.data.model.PlaceWebModel
import hu.sztomek.pizzapp.domain.model.*
import hu.sztomek.pizzapp.domain.util.TimeHelper
import java.lang.IllegalArgumentException

private const val DEFAULT_EMPTY_FIELD = "n/a"
private const val CLOSED = "Closed"
private const val OPENING_HOUR_SEPARATOR = "â€“"
//private const val OPENING_HOUR_SEPARATOR = "–"
private const val PATTERN_REGEX_TIME = "\\d{1,2}:\\d{2}( AM)? $OPENING_HOUR_SEPARATOR \\d{1,2}:\\d{2} PM"
private val REGEX_TIME = PATTERN_REGEX_TIME.toRegex()

private fun defaultStringWhenNull(input: String?): String {
    return input ?: DEFAULT_EMPTY_FIELD
}

private fun extractDayOfOpeningHour(openingHour: String): String {
    return openingHour.substring(0, openingHour.indexOf(":"))
}

private fun extractOpenFromOpeningHour(openingHour: String): String {
    return findTimeInString(openingHour, 0)
}

private fun extractCloseFromOpeningHour(openingHour: String): String {
    return findTimeInString(openingHour, 1)
}

private fun findTimeInString(openingHour: String, index: Int): String {
    return when {
        openingHour.contains(CLOSED) -> "12:00 PM"
        REGEX_TIME.containsMatchIn(openingHour) -> {
            val foundTime = REGEX_TIME.find(openingHour)?.value
            if (foundTime == null) throw IllegalArgumentException("Failed to parse opening hours")
            else foundTime.split(" $OPENING_HOUR_SEPARATOR ")[index]
        }
        else -> throw IllegalArgumentException("Failed to parse opening hours")
    }
}

private fun addFirstDigitTimeWhenNecessary(time: String): String {
    return if (time.split(":")[0].length == 1) "0$time" else time
}

private fun addAmMarkerTimeWhenNecessary(time: String): String {
    return if (!time.contains("PM") && !time.contains("AM")) "$time AM" else time
}

private fun fixTimeFormat(time: String): String {
    return addAmMarkerTimeWhenNecessary(addFirstDigitTimeWhenNecessary(time))
}

private fun parseOpeningHours(openingHours: List<String>): List<OpeningHour> {
    return openingHours.map {
        val dayText = extractDayOfOpeningHour(it)
        val dayOfWeek = TimeHelper.dayTextToDaysOfWeek(dayText)
        val opensText = fixTimeFormat(extractOpenFromOpeningHour(it))
        val opensMs = TimeHelper.parseTimeToMs(opensText)
        val closesText = fixTimeFormat(extractCloseFromOpeningHour(it))
        val closesMs = TimeHelper.parseTimeToMs(closesText)
        OpeningHour(dayOfWeek, opensMs, closesMs)
    }
}

fun PlaceWebModel.toPlace() = Place(
    defaultStringWhenNull(id),
    defaultStringWhenNull(name),
    Location(latitude ?: 0.0, longitude ?: 0.0),
    defaultStringWhenNull(images.firstOrNull()?.url),
    parseOpeningHours(openingHours)
)

fun PlaceWebModel.toPlaceDetails() = PlaceDetails(
    defaultStringWhenNull(id),
    defaultStringWhenNull(name),
    3.0f,
    2,
    false,
    defaultStringWhenNull(images.firstOrNull()?.url)
)

fun PlaceWebModel.toDb(): PlaceDbModel {
    val placeDbModel = PlaceDbModel()
    placeDbModel.id = 0L
    placeDbModel.externalId = defaultStringWhenNull(id)
    placeDbModel.name = defaultStringWhenNull(name)
    placeDbModel.latitude = latitude ?: 0.0
    placeDbModel.longitude = longitude ?: 0.0
    placeDbModel.imageUrl = defaultStringWhenNull(images.firstOrNull()?.url)
    placeDbModel.fetched = System.currentTimeMillis()

    return placeDbModel
}

fun PlaceDbModel.toPlace() = Place(
    externalId,
    name,
    Location(latitude, longitude),
    imageUrl,
    parseOpeningHours(openingHours.map { it.rawOpening })
)

fun FriendWebModel.toDomain() = Friend(
    id,
    defaultStringWhenNull(name),
    defaultStringWhenNull(avatarUrl)
)
