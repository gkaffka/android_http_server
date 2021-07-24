package com.kaffka.httpserver.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.kaffka.httpserver.R
import com.kaffka.httpserver.databinding.FragmentServerControlBinding
import com.kaffka.httpserver.domain.ServerStatus
import com.kaffka.httpserver.ui.ServerControlViewModel.ViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ServerControlFragment : Fragment() {

    private var _binding: FragmentServerControlBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ServerControlViewModel by viewModels()

    @Inject
    lateinit var status: LiveData<ServerStatus>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServerControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                ViewState.Loading -> onLoading()
                is ViewState.Success -> onSuccess(it.calls, it.fullAddress)
                is ViewState.Error -> onError(it.message)
            }
        }
    }

    private fun onSuccess(calls: List<String>, fullAddress: String) {
        val displayAddress = getString(R.string.ip_address, fullAddress)
        binding.ipAddressTxt.text = displayAddress
        binding.callLogList.adapter = CallLogAdapter(calls)
        initServerButton()
        binding.progressCircular.hide()
    }

    private fun initServerButton() {
        status.observe(viewLifecycleOwner) {
            with(binding.ctrServerBtn) {
                val isServerRunning = it == ServerStatus.ONLINE
                isEnabled = true
                setText(if (isServerRunning) R.string.stop_server else R.string.start_server)
                setOnClickListener { toggleServer(isServerRunning) }
            }
        }
    }

    private fun onError(message: String) {
        binding.progressCircular.hide()
        binding.callLogList.isVisible = false
        Snackbar.make(binding.ctrServerBtn, message, Snackbar.LENGTH_INDEFINITE).show()
    }

    private fun onLoading() {
        binding.progressCircular.show()
    }

    private fun toggleServer(isServerRunning: Boolean) {
        binding.ctrServerBtn.isEnabled = false
        val serviceIntent = Intent(requireActivity(), ServerService::class.java)
        if (isServerRunning) {
            requireActivity().stopService(serviceIntent)
        } else {
            ContextCompat.startForegroundService(requireActivity(), serviceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
