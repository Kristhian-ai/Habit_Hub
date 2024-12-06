package com.example.HabitTracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import com.example.HabitTracker.ui.HabitTrackingActivity;


public class MainActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView registerButton, forgetPasswordButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views for login functionality
        initializeLoginViews();

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validate input fields and attempt login
            if (validateInputs(email, password)) {
                loginUser(email, password);
            }
        });

        // Set up register button click listener
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistrationForm.class));
        });

        // Set up forget password button click listener
        forgetPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void initializeLoginViews() {
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.pwd);
        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.register_btn);
        forgetPasswordButton = findViewById(R.id.forget_password_btn);
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please enter a valid email");
            emailField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordField.setError("Password should be at least 6 characters");
            passwordField.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            startActivity(new Intent(MainActivity.this, HabitTrackingActivity.class));

                            finish(); // Close the login activity
                        } else {
                            showToast("Login failed. Please try again later.");
                        }
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void handleLoginError(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            showToast("No account found with this email");
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            showToast("Incorrect email or password");
        } else {
            showToast("Login failed. Please try again later");
        }
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
