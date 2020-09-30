package com.example.todo;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText addItem;
    RecyclerView itemList;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // italicize called from activity_main.xml
        btnAdd = findViewById(R.id.btnADD);
        addItem = findViewById(R.id.addItem);
        itemList = findViewById(R.id.itemList);

        // Mock Input
        /*items = new ArrayList<>();
        items.add("DO HOMEWORK");
        items.add("COOK FOOD");
        items.add("AOISDAOSID");
         */

        // Calls the function to load the data into the system
        loadItems();

        // Reference of each view from main view
        // Handle logic for each / each component
        // Add member variable for each

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            /* REMOVE ITEM*/
            @Override
            public void onItemLongClicked(int position) {
            // HAve exact position
                // Delete item from model and notify adapter
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was Removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        /* UPDATE FUNCTION: ON CLICK */
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // Open up activity
                Log.d("MainActivity", "Single click at position" + position);
                // Create the new activity
                /* MainActivity.this: instance of MainActivity
                   EditActivity.class: class to go to */
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass relevant data being edited
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        // Construct Adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener,onClickListener);
        itemList.setAdapter(itemsAdapter);
        // Runs this vertically
        itemList.setLayoutManager(new LinearLayoutManager(this));

        /* ADDING ITEM */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ADD*/
                //Takes the input item data
                String todoItem = addItem.getText().toString();
                //Add item to model
                items.add(todoItem);
                //Notify adapter added an item to the list
                itemsAdapter.notifyItemInserted(items.size()-1);
                addItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was Added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // Handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // First check: if request code matches passed in
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Update the model at teh right position with new item text
            items.set(position, itemText);
            // Notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Updated Successfully!", Toast.LENGTH_SHORT).show();
        }else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Get data and store in data file
    private File getDataFile(){
        // getFilersDir: directory of file and name
        return new File(getFilesDir(), "data.txt");
    }

    /*  CALLED ONCE WHEN APP STARTS UP */
    // Load items by reading every line of the data file
    private void loadItems(){
        // Parameters: read all lines and populate into arraylist (list of model)
        try {
            // Tries this first then catch the bug if anything
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // What's really happening in the program
            Log.e("MainActivity", "Error reading items", e);
            // If we do end up with an exception,
            items = new ArrayList<>();
        }
    }

    /* CALLED WHEN CHANGE IS MADE: ADD OR REMOVE */
    // Save items by writing them into data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}