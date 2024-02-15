package androidsamples.java.dicegames;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActionBar actionBar = getSupportActionBar();
        String titleText = "Dice Games";
        SpannableStringBuilder s = new SpannableStringBuilder(titleText);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(s);
        }
        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(view -> finish());
    }
}