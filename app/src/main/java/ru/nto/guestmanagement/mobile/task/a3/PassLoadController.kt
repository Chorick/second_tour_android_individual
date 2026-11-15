package ru.nto.guestmanagement.mobile.task.a3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState

class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow<PassScreenState>(PassScreenState.InitialLoading)
    val state: StateFlow<PassScreenState> = _state.asStateFlow()

    private var started = false
    private var inFlight = false

    fun start() {
        if (started || inFlight) return
        if (_state.value !is PassScreenState.InitialLoading) return
        started = true
        launchInitialLoad()
    }

    fun retry() {
        if (inFlight) return
        if (_state.value !is PassScreenState.InitialError) return
        _state.value = PassScreenState.InitialLoading
        launchInitialLoad()
    }

    fun refresh() {
        if (inFlight) return
        val current = _state.value as? PassScreenState.Content ?: return
        inFlight = true
        _state.value = current.copy(isRefreshing = true)
        scope.launch {
            val result = try {
                repository.load()
            } finally {
                inFlight = false
            }
            result.fold(
                onSuccess = { data ->
                    _state.value = PassScreenState.Content(
                        data = data,
                        isRefreshing = false,
                        isOffline = false,
                    )
                },
                onFailure = {
                    val latest = (_state.value as? PassScreenState.Content) ?: current
                    _state.value = latest.copy(
                        isRefreshing = false,
                        isOffline = true,
                    )
                },
            )
        }
    }

    private fun launchInitialLoad() {
        inFlight = true
        scope.launch {
            val result = try {
                repository.load()
            } finally {
                inFlight = false
            }
            result.fold(
                onSuccess = { data ->
                    _state.value = PassScreenState.Content(
                        data = data,
                        isRefreshing = false,
                        isOffline = false,
                    )
                },
                onFailure = { throwable ->
                    val message = throwable.message?.takeIf { it.isNotBlank() }
                        ?: "Не удалось загрузить пропуск"
                    _state.value = PassScreenState.InitialError(message)
                },
            )
        }
    }
}
