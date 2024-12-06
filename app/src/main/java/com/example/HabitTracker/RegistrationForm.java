package com.example.HabitTracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationForm extends AppCompatActivity {

    // Define UI elements
    EditText username, password, email, phone, interest, birthdate, birthtime;
    AutoCompleteTextView country;
    RadioGroup genderGroup;
    Button submit;
    Spinner provinceSpinner;
    ArrayAdapter<String> countryAdapter, provinceAdapter;

    // Data for AutoCompleteTextView and Spinner
    String[] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina",
            "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
            "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil",
            "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
            "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile",
            "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia",
            "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica",
            "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
            "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany",
            "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia",
            "Iran", "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait",
            "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi",
            "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania",
            "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru",
            "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria",
            "North Korea", "North Macedonia", "Norway", "Oman", "Pakistan",
            "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
            "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda",
            "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
            "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal",
            "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia",
            "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",
            "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden",
            "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
            "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
            "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
            "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City",
            "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};
    String[] provinces = {"Abra", "Agusan del Norte", "Agusan del Sur", "Aklan", "Albay", "Antique", "Apayao",
            "Aurora", "Basilan", "Bataan", "Batanes", "Batangas", "Benguet", "Biliran",
            "Bohol", "Bukidnon", "Bulacan", "Cagayan", "Camarines Norte", "Camarines Sur",
            "Camiguin", "Capiz", "Catanduanes", "Cavite", "Cebu", "Cotabato", "Davao de Oro",
            "Davao del Norte", "Davao del Sur", "Davao Occidental", "Davao Oriental", "Dinagat Islands",
            "Eastern Samar", "Guimaras", "Ifugao", "Ilocos Norte", "Ilocos Sur", "Iloilo",
            "Isabela", "Kalinga", "La Union", "Laguna", "Lanao del Norte", "Lanao del Sur",
            "Leyte", "Maguindanao del Norte", "Maguindanao del Sur", "Marinduque", "Masbate",
            "Metro Manila", "Misamis Occidental", "Misamis Oriental", "Mountain Province",
            "Negros Occidental", "Negros Oriental", "Northern Samar", "Nueva Ecija", "Nueva Vizcaya",
            "Occidental Mindoro", "Oriental Mindoro", "Palawan", "Pampanga", "Pangasinan",
            "Quezon", "Quirino", "Rizal", "Romblon", "Samar", "Sarangani", "Siquijor",
            "Sorsogon", "South Cotabato", "Southern Leyte", "Sultan Kudarat", "Sulu", "Surigao del Norte",
            "Surigao del Sur", "Tarlac", "Tawi-Tawi", "Zambales", "Zamboanga del Norte",
            "Zamboanga del Sur", "Zamboanga Sibugay"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Initialize UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        country = findViewById(R.id.country);
        genderGroup = findViewById(R.id.gender_group);
        interest = findViewById(R.id.interest);
        birthdate = findViewById(R.id.birthdate);
        birthtime = findViewById(R.id.birthtime);
        submit = findViewById(R.id.regi);
        provinceSpinner = findViewById(R.id.province);

        // Set up ArrayAdapter for AutoCompleteTextView
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        country.setAdapter(countryAdapter);

        // Set up ArrayAdapter for Spinner
        provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);

        // Set up DatePickerDialog
        birthdate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationForm.this,
                    (view, year1, month1, dayOfMonth) -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, month1, dayOfMonth);
                        birthdate.setText(sdf.format(selectedDate.getTime()));
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Set up TimePickerDialog
        birthtime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(RegistrationForm.this,
                    (view, hourOfDay, minute1) -> {
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        birthtime.setText(formattedTime);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        });



        // Set up submit button click listener
        submit.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String phoneNum = phone.getText().toString().trim();
            String selectedCountry = country.getText().toString().trim();
            String selectedProvince = provinceSpinner.getSelectedItem() != null ? provinceSpinner.getSelectedItem().toString() : "";
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : ""; // Extract text from RadioButton
            String userInterest = interest.getText().toString().trim();
            String dob = birthdate.getText().toString().trim();
            String time = birthtime.getText().toString().trim();

            // Input Validation
            if (user.isEmpty()) {
                username.setError("Username is required");
                return;
            }

            if (pass.isEmpty() || pass.length() < 6) {
                password.setError("Password must be at least 6 characters");
                return;
            }

            if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                email.setError("Valid email is required");
                return;
            }

            if (phoneNum.isEmpty() || !phoneNum.matches("\\d{10,}")) {
                phone.setError("Valid phone number is required");
                return;
            }

            if (selectedCountry.isEmpty()) {
                country.setError("Country is required");
                return;
            }

            if (selectedProvince.isEmpty()) {
                Toast.makeText(RegistrationForm.this, "Please select a province", Toast.LENGTH_SHORT).show();
                return;
            }

            if (gender.isEmpty()) {
                Toast.makeText(RegistrationForm.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userInterest.isEmpty()) {
                interest.setError("Interest is required");
                return;
            }

            if (dob.isEmpty()) {
                birthdate.setError("Birthdate is required");
                return;
            }

            if (time.isEmpty()) {
                birthtime.setError("Birthtime is required");
                return;
            }

            // Save the selected gender in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_gender", gender);  // Save the gender text as a string
            editor.apply();

            // Register user with Firebase Authentication
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(verificationTask -> {
                                            if (verificationTask.isSuccessful()) {
                                                Toast.makeText(RegistrationForm.this, "Registration successful. Please check your email for verification.", Toast.LENGTH_LONG).show();

                                                // Create a User object
                                                User newUser = new User(user, mail, phoneNum, selectedCountry, selectedProvince, gender, userInterest, dob, time);

                                                // Save user to Firestore
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                db.collection("users").document(firebaseUser.getUid())
                                                        .set(newUser)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(RegistrationForm.this, "User saved in Firestore", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(RegistrationForm.this, "Failed to save user in Firestore", Toast.LENGTH_SHORT).show();
                                                        });

                                                // Navigate to MainActivity after successful registration
                                                Intent intent = new Intent(RegistrationForm.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegistrationForm.this, "Failed to send verification email.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegistrationForm.this, "This email is already in use.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationForm.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }
}
