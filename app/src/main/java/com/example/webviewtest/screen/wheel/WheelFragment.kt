package com.example.webviewtest.screen.wheel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.webviewtest.R
import com.example.webviewtest.databinding.FragmentWheelBinding

class WheelFragment : Fragment() {

    private var _binding: FragmentWheelBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentWheelBinding is null")

    private lateinit var viewModel: WheelViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWheelBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this)[WheelViewModel::class.java]
        observeViewModel()
        initViews()

        return binding.root
    }

    private fun initViews() {
        binding.spinWheel.setOnClickListener { viewModel.startSpin() }
    }

    private fun observeViewModel() {
        viewModel.spinWheelAvailable.observe(viewLifecycleOwner, ::toggleProgress)
        viewModel.rotation.observe(viewLifecycleOwner) { rotation ->
            binding.wheel.startAnimation(rotation)
        }
        viewModel.score.observe(viewLifecycleOwner) { score ->
            if (score == 0) binding.score.isVisible = false
            binding.score.text = getString(R.string.score, score)
        }
    }

    private fun toggleProgress(visible: Boolean) {
        binding.score.isVisible = visible
        binding.spinWheel.isVisible = visible
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}