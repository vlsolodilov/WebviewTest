package com.example.webviewtest.screen.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.webviewtest.R
import com.example.webviewtest.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentStartBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartBinding.inflate(layoutInflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {

        binding.start.setOnClickListener {
            startWheelFragment()
        }
    }

    private fun startWheelFragment() {
        findNavController().navigate(R.id.action_startFragment_to_wheelFragment)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}