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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.p2.LoginActivity;
import com.example.p2.R;
import com.example.p2.User;
import com.example.p2.db.AppDatabase;
import com.example.p2.db.InventoryLogDAO;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.p2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.p2.PREFERENCES_KEY";
    
    private Button mSearchButton;
    private Button mOrdersButton;
    private Button mPostButton;
    private Button mInventoryButton;
    private Button mAdminButton;

    private InventoryLogDAO mInventoryLogDAO;
    private int mUserId = -1;       // -1 if no user yet defined
    private SharedPreferences mPreferences = null;
    private User mUser;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getDatabase();
        checkForUser();
        loginUser(mUserId);
        wireupDisplay();

        //loginUser(mUserId);
    }

    private void wireupDisplay() {
        mSearchButton = findViewById(R.id.buttonSearch);
        mOrdersButton = findViewById(R.id.buttonOrders);
        mPostButton = findViewById(R.id.buttonPost);
        mInventoryButton = findViewById(R.id.buttonInventory);
        mAdminButton = findViewById(R.id.buttonAdmin);
        
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });

        mOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });

        mInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = InventoryActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });

        if(mUser != null && mUser.isAdmin()){
            mAdminButton.setVisibility(View.VISIBLE);
        } else{
            mAdminButton.setVisibility(View.GONE);
        }

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);

            }
        });
        
    }
    private void loginUser(int userId) {
        //check if userID is valid
        mUser = mInventoryLogDAO.getUserByUserId(userId);
        //check if mUser is not null
        addUserToPreference(userId);
        invalidateOptionsMenu();
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());

            mOptionsMenu = menu;
        }
        return super.onPrepareOptionsMenu(menu);
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
    private void getDatabase() {
        mInventoryLogDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getInventoryLogDAO();
    }
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}