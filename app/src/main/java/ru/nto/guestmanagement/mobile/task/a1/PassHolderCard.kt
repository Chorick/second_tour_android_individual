package ru.nto.guestmanagement.mobile.task.a1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.ui.TestIds
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@Composable
fun PassHolderCard(pass: PassDetails, modifier: Modifier = Modifier) {
    val organization = pass.visitor.organization
    val validPeriodText = formatValidPeriod(pass.validFrom, pass.validTo)
    val entriesLimitText = formatEntriesLimit(pass.maxEntries)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = pass.visitor.fullName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag(TestIds.PASS_HOLDER),
            )
            if (!organization.isNullOrBlank()) {
                Text(
                    text = organization,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.testTag(TestIds.PASS_ORGANIZATION),
                )
            }
            Text(
                text = validPeriodText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag(TestIds.PASS_VALID_PERIOD),
            )
            Text(
                text = entriesLimitText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag(TestIds.PASS_ENTRIES_LIMIT),
            )
        }
    }
}

private fun formatValidPeriod(validFrom: LocalDateTime, validTo: LocalDateTime): String {
    return if (validFrom.toLocalDate() == validTo.toLocalDate()) {
        val date = validFrom.format(DATE_FORMATTER)
        val fromTime = validFrom.format(TIME_FORMATTER)
        val toTime = validTo.format(TIME_FORMATTER)
        "Действует $date, $fromTime\u2013$toTime"
    } else {
        val fromStr = validFrom.format(DATE_TIME_FORMATTER)
        val toStr = validTo.format(DATE_TIME_FORMATTER)
        "Действует с $fromStr по $toStr"
    }
}

@Composable
private fun formatEntriesLimit(maxEntries: Int): String {
    if (maxEntries == 1) {
        return stringResource(R.string.pass_single_entry)
    }
    val mod100 = maxEntries % 100
    val mod10 = maxEntries % 10
    val resId = when {
        mod10 == 1 && mod100 != 11 -> R.string.pass_entries_limit_one
        mod10 in 2..4 && mod100 !in 12..14 -> R.string.pass_entries_limit_few
        else -> R.string.pass_entries_limit_many
    }
    return stringResource(resId, maxEntries)
}
