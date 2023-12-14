package com.example.p2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.p2.databinding.ActivityMainBinding;
import com.example.p2.db.AppDatabase;
import com.example.p2.db.InventoryLogDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.p2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.p2.PREFERENCES_KEY";

    ActivityMainBinding binding;

    private TextView mMainDisplay;
    private TextView mDebug;
    private EditText mTitle;
    private EditText mPrice;
    private EditText mQuantity;
    private TextView mAdmin;
    private TextView mAdminSecretMessage;
    private Button mAdminButton;

    private Button mSubmitButton;

    private InventoryLogDAO mInventoryLogDAO;

    private List<InventoryLog> mInventoryLogs;

    private int mUserId = -1;       // -1 if no user yet defined
    private SharedPreferences mPreferences = null;
    private User mUser;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        getDatabase();
        checkForUser();
        loginUser(mUserId);

        //debug();

        mMainDisplay = findViewById(R.id.mainInventoryLogDisplay);
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        mTitle = findViewById(R.id.mainTitleEditText);
        mPrice = findViewById(R.id.mainPriceEditText);
        mQuantity = findViewById(R.id.mainQuantityEditText);
        mAdmin = findViewById(R.id.textView_admin);
        mAdminSecretMessage = findViewById(R.id.textView_admin_secret);

        mAdminButton = findViewById(R.id.mainAdminButton);
        mSubmitButton = findViewById(R.id.mainSubmitButton);

        refreshDisplay();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryLog log = getValuesFromDisplay();
                //log.setUserId(mUser.getUserId());

                mInventoryLogDAO.insert(log);

                refreshDisplay();
            }
        });

        if(mUser != null && mUser.isAdmin()){
            mAdmin.setVisibility(View.VISIBLE);
            mAdminButton.setVisibility(View.VISIBLE);
        } else{
            mAdmin.setVisibility(View.GONE);
            mAdminButton.setVisibility(View.GONE);
        }

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminSecretMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    /*private void debug(){
        mDebug = findViewById(R.id.DEBUG);
        mDebug.setMovementMethod(new ScrollingMovementMethod());
        List<User> users = mInventoryLogDAO.getAllUsers();

        StringBuilder sb = new StringBuilder();

        sb.append("All users:\n");
        for(User u: users){}
    }*/

    private void loginUser(int userId) {
        //check if userID is valid
        mUser = mInventoryLogDAO.getUserByUserId(userId);
        //check if mUser is not null
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());

            mOptionsMenu = menu;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void getDatabase() {
        mInventoryLogDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getInventoryLogDAO();
    }

    private void checkForUser() {
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        //do we have a user in the preferences?
        if(mUserId != -1){
            return;
        }
        if(mPreferences == null){
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        List<User> users = mInventoryLogDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1", "testuser1");
            User altUser = new User("admin2", "admin2");
            mInventoryLogDAO.insert(defaultUser, altUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);
        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Don't need anything here.
                    }
                });
        alertBuilder.create().show();
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }
    private void clearUserFromPref(){
        addUserToPreference(-1);
        //Toast.makeText(this, "clear users not yet implemented", Toast.LENGTH_SHORT).show();
    }
    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }
    private InventoryLog getValuesFromDisplay(){
        String title = "No record found";
        double price = 0.0;
        int quantity = 0;

        title = mTitle.getText().toString();
        try{
            price = Double.parseDouble(mPrice.getText().toString());
        } catch(NumberFormatException e){
            Log.d("GYMLOG", "Couldn't convert price");
        }

        try{
            quantity = Integer.parseInt(mQuantity.getText().toString());
        } catch(NumberFormatException e){
            Log.d("GYMLOG", "Couldn't convert quantity");
        }

        return new InventoryLog(title, price, quantity, mUserId);

    }

    private void refreshDisplay(){
        mInventoryLogs = mInventoryLogDAO.getInventoryLogsByUserId(mUserId);


        if(mInventoryLogs.size() <= 0){
            mMainDisplay.setText(R.string.noLogsMessage);
        }

        StringBuilder sb = new StringBuilder();
        for(InventoryLog log : mInventoryLogs){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainDisplay.setText(sb.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.userMenuLogout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}