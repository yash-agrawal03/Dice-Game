package androidsamples.java.dicegames;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.apps.common.testing.accessibility.framework.replacements.SpannableString;

import java.util.Locale;

public class WalletActivity extends AppCompatActivity {
  Button btnDie;
  Button nextBtn;
  TextView txtBalance;
  WalletViewModel vm;
  public static final String KEY_BALANCE = "KEY_BALANCE";
  public static final int RETURN_CODE = 0312;
  public static final String RETURN_KEY = "balance";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wallet);

    ActionBar actionBar = getSupportActionBar();
    String titleText = "Dice Games";
    SpannableStringBuilder s = new SpannableStringBuilder(titleText);
    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    if (actionBar != null) {
      actionBar.setTitle(s);
    }

    vm = new ViewModelProvider(this).get(WalletViewModel.class);
    Die dice = new Die6();
    vm.setDie(dice);

    btnDie = findViewById(R.id.btn_die);
    nextBtn =findViewById(R.id.btn_launch_twoormore);
    txtBalance = findViewById(R.id.txt_balance);


    btnDie.setOnClickListener(v -> {
      vm.setWinValue(6);
      vm.setIncrement(5);
      vm.rollDie();
      updateUI();
      if (vm.dieValue() == vm.victoryVal) Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
    });

    ActivityResultLauncher<Intent> twoMoreAct = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == RETURN_CODE) {
                Intent intent = result.getData();
                if (intent != null) {
                  String retStr = intent.getStringExtra(RETURN_KEY);
                  vm.setBalance(Integer.parseInt(retStr));
                  txtBalance.setText(String.format(Locale.ENGLISH, "%d", vm.balance()));
                }
              }
            });


    nextBtn.setOnClickListener(v -> {
      Intent intent = new Intent(this, TwoOrMoreActivity.class);
      Bundle bundle = new Bundle();
      bundle.putString(KEY_BALANCE, vm.balance()+"");
      intent.putExtras(bundle);
      twoMoreAct.launch(intent);
    });

    updateUI();
  }

  void updateUI()
  {
    btnDie.setText(String.format(Locale.ENGLISH, "%d", vm.dieValue()));
    txtBalance.setText(String.format(Locale.ENGLISH, "%d", vm.balance()));
  }
}