package saru.tak.welchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private int position;
  private String[] array;
  private TextView display;
  private Random rng;
  private EditText read;
  private boolean holder;
  private int textSize;
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Shared Preferences Setup
    preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    editor = preferences.edit();

    //try read textSize
    try{
      textSize = preferences.getInt("text_size",-1);
    }catch(NumberFormatException e){
      textSize = 1;
      editor.putInt("text_size",textSize);
    }

    setContentView(R.layout.activity_main);
    // Initialization for App Bar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Load List
    array = getResources().getStringArray(R.array.welch_list);

    // Default starting position
    position = 0;
    // Initialize Starting Position
    rng = new Random();
    read = findViewById(R.id.textField);

    read.setOnEditorActionListener(new TextView.OnEditorActionListener(){
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if(actionId == EditorInfo.IME_ACTION_GO){
          int temp;

          try{
            temp = Integer.valueOf(read.getText().toString());
            temp--;
          }catch(NumberFormatException e){
            temp = 0;
            holder =false;
          }
          if(holder){
            if(temp < 0)
              Toast.makeText(getApplicationContext(),"Value too small",Toast.LENGTH_SHORT).show();
            else if(temp > 2499)
              Toast.makeText(getApplicationContext(),"Value too large",Toast.LENGTH_SHORT).show();
            else
              position = temp;
            updateText();
          }
          holder = true;
          return true;
        }
        return false;
      }
    });

    Button prev = findViewById(R.id.prev_button);

    prev.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(position != 0)
          position--;
        updateText();
      }
    });

    Button next = findViewById(R.id.next_button);

    next.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(position != 2499)
          position++;
        updateText();
      }
    });

    Button rand = findViewById(R.id.rand_button);

    rand.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        position = rng.nextInt(2500);
        updateText();
      }
    });

    display = findViewById(R.id.display);

    // Set Text Size
    setSize();

    AdView adView = new AdView(this);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId("ca-app-pub-9077780131623484/7715468952");
    MobileAds.initialize(this,"ca-app-pub-9077780131623484~6492202417");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()){
      case R.id.size_small:
        textSize = 0;
        break;
      case R.id.size_med:
        textSize = 1;
        break;
      case R.id.size_large:
        textSize = 2;
        break;
    }
    editor.putInt("text_size",textSize);
    editor.commit();
    setSize();
    return super.onOptionsItemSelected(item);
  }

  public void updateText(){
    display.setText(array[position]);
  }

  public void setSize(){
    switch(textSize){
      case 0:
        display.setTextSize(12);
        break;
      case 1:
        display.setTextSize(24);
        break;
      case 2:
        display.setTextSize(36);
        break;
    }
  }
}
