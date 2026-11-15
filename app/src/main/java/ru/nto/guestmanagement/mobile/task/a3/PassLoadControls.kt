package ru.nto.guestmanagement.mobile.task.a3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.ui.TestIds

@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (state) {
            is PassScreenState.InitialLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(TestIds.PASS_LOADING),
                )
            }
            is PassScreenState.InitialError -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag(TestIds.PASS_ERROR),
                )
                Button(
                    onClick = onRetry,
                    modifier = Modifier.testTag(TestIds.PASS_RETRY),
                ) {
                    Text(text = stringResource(R.string.action_retry))
                }
            }
            is PassScreenState.Content -> {
                if (state.isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(TestIds.PASS_LOADING),
                    )
                    Button(
                        onClick = onRefresh,
                        enabled = false,
                        modifier = Modifier.testTag(TestIds.PASS_REFRESH),
                    ) {
                        Text(text = stringResource(R.string.action_refresh))
                    }
                } else {
                    if (state.isOffline) {
                        Text(
                            text = stringResource(R.string.offline),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.testTag(TestIds.PASS_OFFLINE),
                        )
                    }
                    Button(
                        onClick = onRefresh,
                        enabled = true,
                        modifier = Modifier.testTag(TestIds.PASS_REFRESH),
                    ) {
                        Text(text = stringResource(R.string.action_refresh))
                    }
                }
            }
        }
    }
}
