package com.example.mosquesilentmode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_DND_DELAY = "dnd_delay_minutes";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Oddiy va xavfsiz interfeys elementlari
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        EditText dndDelayEditText = new EditText(this);
        dndDelayEditText.setHint("Kechikish vaqtini kiriting (daqiqa)");
        layout.addView(dndDelayEditText);
        setContentView(layout);

        prefs = getSharedPreferences("MosquePrefs", MODE_PRIVATE);
        int savedDelay = prefs.getInt(KEY_DND_DELAY, 0);
        dndDelayEditText.setText(String.valueOf(savedDelay));

        // Matn o'zgarganda xavfsiz saqlash (Crash oldini olish)
        dndDelayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String text = editable.toString().trim();
                    if (!text.isEmpty()) {
                        int minutes = Integer.parseInt(text);
                        if (minutes < 0) minutes = 0;
                        if (minutes > 60) minutes = 60;
                        prefs.edit().putInt(KEY_DND_DELAY, minutes).apply();
                    }
                } catch (NumberFormatException e) {
                    android.util.Log.e("MainActivity", "Xato raqam formati", e);
                }
            }
        });

        // Fon xizmatini ishga tushirish
        Intent serviceIntent = new Intent(this, LocationService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
}
