package com.example.HabitTracker.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.HabitTracker.ForgotPasswordActivity;
import com.example.HabitTracker.R;
import com.example.HabitTracker.ui.HabitHistoryActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchBluetooth;
    private Button btnChangeProfile, btnChangePassword, btnHabitHistory, btnBackHome;
    private ImageView profileIcon;
    private ListView deviceListView;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> discoveredDevices;
    private ArrayAdapter<String> deviceListAdapter;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_CODE = 102;

    private int steps;
    private int calories;
    private double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI components
        switchBluetooth = findViewById(R.id.switchBluetooth);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnHabitHistory = findViewById(R.id.btnHabitHistory);
        btnBackHome = findViewById(R.id.btnBackHome);
        profileIcon = findViewById(R.id.profileIcon);
        deviceListView = findViewById(R.id.deviceListView);

        deviceListView.setVisibility(View.GONE);

        // Load profile icon using Glide
        Glide.with(this)
                .load(R.drawable.ic_default)
                .into(profileIcon);

        // Initialize Bluetooth adapter and device list
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        discoveredDevices = new ArrayList<>();
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceListAdapter);

        // Device list click listener
        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = discoveredDevices.get(position);
            connectToDevice(selectedDevice);
        });

        // Handle Bluetooth switch toggle
        switchBluetooth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableBluetooth();
            } else {
                disableBluetooth();
            }
        });

        // Change profile picture
        btnChangeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        });

        // Navigate to password change activity
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Navigate to habit history activity
        btnHabitHistory.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HabitHistoryActivity.class);
            startActivity(intent);
        });

        // Navigate back to home activity
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HabitTrackingActivity.class); // Replace with your home activity class
            startActivity(intent);
            finish(); // Close the current activity to avoid stack overflow
        });
    }

    private void enableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        PERMISSION_REQUEST_CODE);
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            checkPermissionsAndDiscoverDevices();
        }
    }

    private void disableBluetooth() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.disable();
            Toast.makeText(this, "Bluetooth disabled.", Toast.LENGTH_SHORT).show();
            deviceListView.setVisibility(View.GONE);  // Hide device list
        }
    }

    private void checkPermissionsAndDiscoverDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                        PERMISSION_REQUEST_CODE);
                return;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
                return;
            }
        }

        discoverDevices();
    }

    private void discoverDevices() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            deviceListView.setVisibility(View.VISIBLE);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            bluetoothAdapter.startDiscovery();  // Start device discovery
            registerReceiver(bluetoothReceiver, new android.content.IntentFilter(BluetoothDevice.ACTION_FOUND));  // Register receiver
        }
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !discoveredDevices.contains(device)) {
                    discoveredDevices.add(device);
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        deviceListAdapter.add(device.getName() + "\n" + device.getAddress());
                        deviceListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    private void connectToDevice(BluetoothDevice device) {
        new Thread(() -> {
            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(() -> Toast.makeText(this, "Permission denied to connect.", Toast.LENGTH_SHORT).show());
                    return;
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();

                runOnUiThread(() -> Toast.makeText(this, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show());
                readDataFromDevice(socket);
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void readDataFromDevice(BluetoothSocket socket) {
        try (InputStream inputStream = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String data = new String(buffer, 0, bytesRead);
                parseSmartwatchData(data);
                runOnUiThread(this::updateUIWithSmartwatchData);
            }
        } catch (IOException e) {
            runOnUiThread(() -> Toast.makeText(this, "Error reading data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void parseSmartwatchData(String data) {
        try {
            String[] dataParts = data.split(",");
            for (String part : dataParts) {
                String[] keyValue = part.split(":");
                switch (keyValue[0]) {
                    case "steps":
                        steps = Integer.parseInt(keyValue[1]);
                        break;
                    case "calories":
                        calories = Integer.parseInt(keyValue[1]);
                        break;
                    case "distance":
                        distance = Double.parseDouble(keyValue[1]);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUIWithSmartwatchData() {
        // Display data on the screen (could be steps, calories, etc.)
        Toast.makeText(this, "Smartwatch data synchronized: " +
                "Steps: " + steps + ", Calories: " + calories + ", Distance: " + distance, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                discoverDevices();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister Bluetooth receiver to avoid memory leaks
        unregisterReceiver(bluetoothReceiver);
    }
}
