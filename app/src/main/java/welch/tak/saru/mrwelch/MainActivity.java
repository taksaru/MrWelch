package welch.tak.saru.mrwelch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        //SharedPreferences Setup
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //try read textSize
        try{
            textSize = preferences.getInt("text_size", -1);
        }catch(NumberFormatException ex){
            textSize = 1;
            editor.putInt("text_size", textSize);
        }

        setContentView(R.layout.activity_main);
        //Initialization for App Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Mr. Welch's List
        array = getResources().getStringArray(R.array.welch_list);

        //default starting position
        position = 0;
        //initialise variables
        rng = new Random();
        read = (EditText) findViewById(R.id.textField);
        Button prev = (Button) findViewById(R.id.prev_button);
        Button next = (Button) findViewById(R.id.next_button);
        Button rand = (Button) findViewById(R.id.rand_button);
        display = (TextView) findViewById(R.id.display);

        //set Text Size
        setSize();

        read.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO) {
                    int temp;

                    try {
                        temp = Integer.valueOf(read.getText().toString());
                        temp--;
                    } catch (NumberFormatException ex) {
                        temp = 0;
                        holder = false;
                    }
                    if(holder != false) {
                        if (temp < 0) {
                            Context context = getApplicationContext();
                            CharSequence text = "Value too small";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } else if (temp > 2374) {
                            Context context = getApplicationContext();
                            CharSequence text = "Value too large";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } else {
                            position = temp;
                        }
                        updateText();
                    }
                    holder = true;
                    return true;
                }
                return false;
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0){
                    position--;
                }
                updateText();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 2374){
                    position++;
                }
                updateText();
            }
        });

        rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = rng.nextInt(2375);
                updateText();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
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
        editor.putInt("text_size", textSize);
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
            default:
                break;
        }

    }

}
