package com.alpha.babyneeds;

import android.os.Bundle;

import com.alpha.babyneeds.Data.DatabaseHandler;
import com.alpha.babyneeds.Model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText ItemName;
    private EditText ItemQuantity;
    private EditText ItemColor;
    private EditText ItemSize;
    private Button EnterButton;
    private DatabaseHandler db;


    protected void onCreate(Bundle savedInstanceState) {
       db = new DatabaseHandler(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("itemName", "onCreate: "+db.getItem(0).getItemName().toString());

//        List<Item> itemList = db.getAllItems();
//        for(Item item: itemList){
//            Log.d("itemName", "onCreate: "+item.getItemName().toString());
//        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopupDialog();
            }


        });
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up, null);
        ItemName  = view.findViewById(R.id.itemName);
        ItemQuantity = view.findViewById(R.id.itemQuantity);
        ItemColor = view.findViewById(R.id.itemColor);
        ItemSize = view.findViewById(R.id.itemSize);
        EnterButton = view.findViewById(R.id.enter);


        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(     !(ItemName.getText().toString().isEmpty()&&
                        ItemQuantity.getText().toString().isEmpty()&&
                        ItemColor.getText().toString().isEmpty()&&
                        ItemSize.getText().toString().isEmpty())     )
                { saveItem(v);}
                else {
                    Snackbar.make(v,"Empty Field Not Allow", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);

        dialog = builder.create();
        dialog.show();




    }

    public void saveItem(View v) {
        Item item = new Item();
        item.setItemName(ItemName.getText().toString().trim());
        item.setItemColor(ItemColor.getText().toString().trim());
        item.setItemSize(Integer.parseInt(ItemSize.getText().toString().trim()));
        item.setItemSize(Integer.parseInt(ItemSize.getText().toString().trim()));
        db.addItem(item);
        Snackbar.make(v, "Item Saved",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
