package com.example.HabitTracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button resetPasswordButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize fields and Firebase Auth
        emailField = findViewById(R.id.email);
        resetPasswordButton = findViewById(R.id.reset_password_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        // Set up reset password button click event
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }

            // Show progress dialog while sending reset email
            progressDialog.setMessage("Sending reset email...");
            progressDialog.show();

            // Send password reset email
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss(); // Hide progress dialog

                        if (task.isSuccessful()) {
                            // Display success message and navigate back to login screen
                            Toast.makeText(ForgotPasswordActivity.this, "Reset email sent. Please check your inbox.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            // Display error message if email sending failed
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send reset email.";
                            Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
