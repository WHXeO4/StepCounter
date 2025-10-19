package com.WHXeO46.github.stepcounter.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;
import com.WHXeO46.github.stepcounter.MainActivity;
import com.WHXeO46.github.stepcounter.R;
import com.WHXeO46.github.stepcounter.data.Blob;
import com.WHXeO46.github.stepcounter.data.StepService;
import com.WHXeO46.github.stepcounter.databinding.FragmentLoginBinding;
import com.WHXeO46.github.stepcounter.user.Account;
import com.WHXeO46.github.stepcounter.utils.ErrorMessage;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding _binding;
    private LoginViewModel loginViewModel;
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        _binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();



        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        TextView textView = _binding.textLogin;
        loginViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        try {
            String[] pair = hasLoggedIn();
            if (pair != null) {
                Account.setAccount(pair);
                Blob blob = null;
                try {
                    blob = Blob.load(getContext(), null, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                StepService.setStep(blob.getStep());
                Navigation.findNavController(requireView()).navigate(R.id.action_login_to_home);
            } else {
                initUI();
                setupLoginListener();
            }
        } catch (FileNotFoundException e) {
            initUI();
            setupLoginListener();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FragmentLoginBinding getBinding() {
        return _binding;
    }

    private void initUI() {
        getBinding().usernameEditText.requestFocus();
    }

    private void setupLoginListener() {
        getBinding().loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void login() throws IOException {
        String username = getBinding().usernameEditText.getText().toString().trim();
        String password = getBinding().passwordEditText.getText().toString().trim();

        ErrorMessage message = Account.login(requireContext(), username, password);
        System.out.println(message);
        switch (message) {
            case NEW_USER_REGISTERED:
                loginViewModel.setText("Welcome, " + username);
                Navigation.findNavController(requireView()).navigate(R.id.action_to_welcome);
                break;

            case PASSWORD_LENGTH_ERROR:
                loginViewModel.setText("Password length should be within 8-16 characters");
                if (getBinding().passwordEditText.getText() != null) {
                    getBinding().passwordEditText.getText().clear();
                }
                break;

            case PASSWORD_INVALID_ERROR:
                loginViewModel.setText("Password can only be composed by number, alphabeta, and _");
                if (getBinding().passwordEditText.getText() != null) {
                    getBinding().passwordEditText.getText().clear();
                }
                break;

            case USERNAME_LENGTH_ERROR:
                loginViewModel.setText("Username length should not be longer than 8 characters");
                if (getBinding().usernameEditText.getText() != null) {
                    getBinding().usernameEditText.getText().clear();
                }
                if (getBinding().passwordEditText.getText() != null) {
                    getBinding().passwordEditText.getText().clear();
                }
                break;

            case USERNAME_INVALID_ERROR:
                loginViewModel.setText("Username can only be composed by number and alphabeta");
                if (getBinding().usernameEditText.getText() != null) {
                    getBinding().usernameEditText.getText().clear();
                }
                if (getBinding().passwordEditText.getText() != null) {
                    getBinding().passwordEditText.getText().clear();
                }
                break;

            case LOGIN_ERROR:
                loginViewModel.setText("Unmatched username and password");
                if (getBinding().usernameEditText.getText() != null) {
                    getBinding().usernameEditText.getText().clear();
                }
                if (getBinding().passwordEditText.getText() != null) {
                    getBinding().passwordEditText.getText().clear();
                }
                break;

            case LOGIN_SUCCEED:
                loginViewModel.setText("Welcome back");
                Navigation.findNavController(requireView()).navigate(R.id.action_login_to_home);
                break;

            case NO_ERROR:
                Navigation.findNavController(requireView()).navigate(R.id.action_login_to_home);
                break;
            case LOGIN_FAILED:
                loginViewModel.setText("Something wrong happened, please try again");
            default:
                break;
        }
    }

    private String[] hasLoggedIn() throws IOException {
        FileInputStream fis = requireContext().openFileInput("last_user.txt");
        try (InputStreamReader isr = new InputStreamReader(fis)) {
            String username = "", password = "";
            int ch;
            while ((ch = isr.read()) != '#' && ch != -1) {
                username += (char) ch;
            }

            while ((ch = isr.read()) != '#' && ch != -1) {
                password += (char) ch;
            }

            if ((!username.isEmpty()) && (!password.isEmpty())) {
                String[] pair = new String[2];
                pair[0] = username;
                pair[1] = password;
                return pair;
            } else {
                return null;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }
}
