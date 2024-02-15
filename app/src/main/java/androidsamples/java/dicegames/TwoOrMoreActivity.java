package androidsamples.java.dicegames;

import static java.lang.Integer.parseInt;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class TwoOrMoreActivity extends AppCompatActivity {
  RadioButton selectedRadioButton;
  TextView balanceTextView;
  TwoOrMoreViewModel viewModel;
  Button backButton;
  Button goButton;
  Button infoButton;
  EditText wagerEditText;
  TextView dieButton1;
  TextView dieButton2;
  TextView dieButton3;
  TextView dieButton4;
  RadioGroup radioGroup;
  Bundle extrasBundle;
  String previousBalance = "";
  int balance=0;
  List<Integer> itemList;
  List<Integer> itemListII;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_two_or_more);

    ActionBar actionBar = getSupportActionBar();
    String titleText = "Dice Games";
    SpannableStringBuilder st = new SpannableStringBuilder(titleText);
    st.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    if (actionBar != null) {
      actionBar.setTitle(st);
    }
    viewModel = new ViewModelProvider(this).get(TwoOrMoreViewModel.class);
    backButton = findViewById(R.id.btn_back);
    goButton = findViewById(R.id.btn_go);
    infoButton = findViewById(R.id.btn_info);
    wagerEditText = findViewById(R.id.edit_wager);
    dieButton1 = findViewById(R.id.txt_die1);
    dieButton2 = findViewById(R.id.txt_die2);
    dieButton3 = findViewById(R.id.txt_die3);
    dieButton4 = findViewById(R.id.txt_die4);
    radioGroup = findViewById(R.id.radioGroup);
    balanceTextView = findViewById(R.id.txt_balance_twoormore);
    extrasBundle = getIntent().getExtras();
    if (extrasBundle != null) {
      previousBalance = extrasBundle.getString(WalletActivity.KEY_BALANCE);
      balance=previousBalance==null?0: parseInt(previousBalance);
    }
    if (!viewModel.flagReturn()) {
      balanceTextView.setText(String.format(Locale.ENGLISH, "%d", balance));
      viewModel.setBalance(balance);
      Die d1 = new Die6();
      Die d2 = new Die6();
      Die d3 = new Die6();
      Die d4 = new Die6();
      viewModel.addDie(d1);
      viewModel.addDie(d2);
      viewModel.addDie(d3);
      viewModel.addDie(d4);
      viewModel.flagSet(true);
    }
    else {
      balanceTextView.setText(String.format(Locale.ENGLISH, "%d", viewModel.balance()));
      itemListII = viewModel.diceValues();
      dieButton1.setText(String.valueOf(itemListII.get(0)));
      dieButton2.setText(String.valueOf(itemListII.get(1)));
      dieButton3.setText(String.valueOf(itemListII.get(2)));
      dieButton4.setText(String.valueOf(itemListII.get(3)));
    }

    backButton.setOnClickListener(view -> {
      viewModel.flagSet(false);
      Intent intent = new Intent(TwoOrMoreActivity.this,WalletActivity.class);
      Bundle bundle = new Bundle();
      bundle.putString(WalletActivity.RETURN_KEY, balanceTextView.getText().toString());
      intent.putExtras(bundle);
      setResult(WalletActivity.RETURN_CODE,intent);
      //WalletActivity.vm.setBalance(parseInt(txtBalance.getText().toString().split(" ")[1]));
      finish();
    });

    infoButton.setOnClickListener(view -> {
      Intent intent = new Intent(this,InformationActivity.class);
      startActivity(intent);
    });

    goButton.setOnClickListener(view -> {
      GameResult game_result;
      int x;
      try {
        x = parseInt(wagerEditText.getText().toString());
      } catch (NumberFormatException e) {
        Toast.makeText(getApplicationContext(), "Invalid input!", Toast.LENGTH_SHORT).show();
        return;
      }
      viewModel.setWager(x);
      int y = radioGroup.getCheckedRadioButtonId();
      if (y != -1) {
        selectedRadioButton = findViewById(y);
        String s = selectedRadioButton.getText().toString();
        if ("2 Alike".equals(s)) {
          viewModel.setGameType(GameType.TWO_ALIKE);
        } else if ("3 Alike".equals(s)) {
          viewModel.setGameType(GameType.THREE_ALIKE);
        } else if ("4 Alike".equals(s)) {
          viewModel.setGameType(GameType.FOUR_ALIKE);
        }

      }
      try {
        game_result = viewModel.play();
      } catch (IllegalStateException e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        return;
      }

      itemList = viewModel.diceValues();
      dieButton1.setText(String.valueOf(itemList.get(0)));
      dieButton2.setText(String.valueOf(itemList.get(1)));
      dieButton3.setText(String.valueOf(itemList.get(2)));
      dieButton4.setText(String.valueOf(itemList.get(3)));
      balance = viewModel.balance();
      balanceTextView.setText(String.format(Locale.ENGLISH, "%d", viewModel.balance()));
      if (game_result == GameResult.WIN) {
        Toast.makeText(getApplicationContext(), "Congratulations!", Toast.LENGTH_SHORT).show();
        //WalletActivity.vm.setBalance(parseInt(balanceTextView.getText().toString().split(" ")[1]));
      } else if (game_result == GameResult.LOSS) {
        Toast.makeText(getApplicationContext(), "Oh no! Coins lost!", Toast.LENGTH_SHORT).show();
        //WalletActivity.vm.setBalance(parseInt(balanceTextView.getText().toString().split(" ")[1]));
      }

      OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
          viewModel.flagSet(false);
          Intent intent = new Intent(TwoOrMoreActivity.this, WalletActivity.class);
          Bundle bundle = new Bundle();
          bundle.putString(WalletActivity.RETURN_KEY, balanceTextView.getText().toString());
          intent.putExtras(bundle);
          setResult(WalletActivity.RETURN_CODE, intent);
          //WalletActivity.vm.setBalance(parseInt(balanceTextView.getText().toString().split(" ")[1]));
          finish();
        }
      };
      this.getOnBackPressedDispatcher().addCallback(this, callback);
    });
    }
}