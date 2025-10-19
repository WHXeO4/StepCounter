package com.WHXeO46.github.stepcounter.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.WHXeO46.github.stepcounter.R;
import com.WHXeO46.github.stepcounter.databinding.FragmentLoginBinding;
import com.WHXeO46.github.stepcounter.databinding.FragmentWelcomeBinding;
import com.WHXeO46.github.stepcounter.ui.login.LoginViewModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class WelcomeFragment extends Fragment {
    private FragmentWelcomeBinding _binding;
    private WelcomeViewModel welcomeViewModel;
    private Button welcomeButton;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
        TextView textView = _binding.textWelcome;
        welcomeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        welcomeButton = _binding.buttonJumpToHome;

        setupWelcomeListener();
    }

    private FragmentWelcomeBinding getBinding() {
        return _binding;
    }
    private void setupWelcomeListener() {
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToHome();
            }
        });
    }

    private void navToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registered);
    }
}