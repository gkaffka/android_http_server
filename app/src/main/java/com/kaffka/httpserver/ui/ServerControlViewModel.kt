package com.kaffka.httpserver.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kaffka.httpserver.domain.usecases.ReadableCallLogUseCase
import com.kaffka.httpserver.domain.usecases.ReadableServerAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServerControlViewModel @Inject constructor(
    private val readableCallLogUseCase: ReadableCallLogUseCase,
    private val readableServerAddressUseCase: ReadableServerAddressUseCase
) :
    ViewModel() {

    val viewState: LiveData<ViewState> = liveData {
        emit(ViewState.Loading)
        try {
            emit(
                ViewState.Success(
                    readableCallLogUseCase.getCallLog(),
                    readableServerAddressUseCase.getReadableAddress()
                )
            )
        } catch (e: Exception) {
            emit(ViewState.Error("Ops! ${e.localizedMessage}"))
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Success(val calls: List<String>, val fullAddress: String) : ViewState()
        data class Error(val message: String) : ViewState()
    }

}
