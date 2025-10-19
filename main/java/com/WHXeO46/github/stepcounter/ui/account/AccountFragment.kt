package com.WHXeO46.github.stepcounter.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.WHXeO46.github.stepcounter.R
import com.WHXeO46.github.stepcounter.databinding.FragmentAccountBinding
import android.content.Context
import com.WHXeO46.github.stepcounter.data.Blob
import com.WHXeO46.github.stepcounter.data.StepService

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAccount
        accountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLogoutListener()
    }

    private fun setupLogoutListener() {
        binding.logoutButton.setOnClickListener {
            // Clear last user data to prevent auto-login
            context?.openFileOutput("last_user.txt", Context.MODE_PRIVATE)?.use {
                it.write("".toByteArray())
            }
            val blob: Blob = Blob(StepService.getStep())
            blob.save(context, true)
            // Navigate back to the login screen
            findNavController().navigate(R.id.action_nav_account_to_nav_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}