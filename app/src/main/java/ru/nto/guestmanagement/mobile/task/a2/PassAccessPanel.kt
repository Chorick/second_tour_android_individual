package ru.nto.guestmanagement.mobile.task.a2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.ui.PassQr
import ru.nto.guestmanagement.mobile.ui.TestIds
import ru.nto.guestmanagement.mobile.ui.statusColor

@Composable
fun PassAccessPanel(data: PassData, timeProvider: TimeProvider, modifier: Modifier = Modifier) {
    val now = timeProvider.now()
    val status = calculatePassStatus(data.pass, data.scans, now)

    val statusText = when (status) {
        PassStatus.ACTIVE -> stringResource(R.string.pass_status_active)
        PassStatus.NOT_STARTED -> stringResource(R.string.pass_status_not_started)
        PassStatus.EXPIRED -> stringResource(R.string.pass_status_expired)
        PassStatus.EXHAUSTED -> stringResource(R.string.pass_status_exhausted)
        PassStatus.BLOCKED -> stringResource(R.string.pass_status_blocked)
    }

    val statusColorRes = when (status) {
        PassStatus.ACTIVE -> R.color.status_active
        PassStatus.NOT_STARTED -> R.color.status_not_started
        PassStatus.EXPIRED -> R.color.status_expired
        PassStatus.EXHAUSTED -> R.color.status_exhausted
        PassStatus.BLOCKED -> R.color.status_blocked
    }
    val badgeColor = colorResource(statusColorRes)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(color = badgeColor, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = statusText,
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .testTag(TestIds.PASS_STATUS)
                        .statusColor(badgeColor),
                )
            }

            if (status == PassStatus.ACTIVE) {
                PassQr(content = data.pass.qrCode)
            } else {
                Text(
                    text = stringResource(R.string.qr_unavailable),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag(TestIds.PASS_QR_HIDDEN),
                )
            }
        }
    }
}
