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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p2.db.AppDatabase;
import com.example.p2.db.InventoryLogDAO;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.p2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.p2.PREFERENCES_KEY";

    private TextView mSearchDisplay;
    private EditText mTitle;
    private EditText mPrice;
    private EditText mQuantity;

    private Button mExitSearchButton;

    private Button mBuyItemButton;

    private InventoryLogDAO mInventoryLogDAO;
    private List<InventoryLog> mInventoryLogs;
    private List<Item> mItems;

    private int mUserId = -1;       // -1 if no user yet defined
    private SharedPreferences mPreferences = null;
    private User mUser;
    private List<User> userList;
    private Menu mOptionsMenu;

    private String requestedTitle;
    private int requestedQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getDatabase();
        checkForUser();
        loginUser(mUserId);

        mSearchDisplay = findViewById(R.id.searchUserDisplay);
        mSearchDisplay.setMovementMethod(new ScrollingMovementMethod());

        mTitle = findViewById(R.id.editTextBuyTitle);
        mQuantity = findViewById(R.id.editTextBuyQuantity);

        mBuyItemButton = findViewById(R.id.buttonBuyItemButton);
        mExitSearchButton = findViewById(R.id.buttonExitSearch);

        refreshDisplay();

        mBuyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                //mItems = mInventoryLogDAO.getItemByTitle(requestedTitle);
                int remainingRequestedQuantity = requestedQuantity;
                Item requestedItem = (mInventoryLogDAO.getItemByTitle(requestedTitle));
                int totalAvailableQuantity = requestedItem.getQuantity();
                //get total number of requested item (across multiple users)
                /*for(Item item: mItems){
                    availableQuantity += item.getQuantity();
                }*/

                // if buyer requested too many of an item
                if(requestedQuantity > totalAvailableQuantity){
                    Toast.makeText(SearchActivity.this,
                            "There is not enough " + requestedTitle
                                    + " for your request, try again", Toast.LENGTH_LONG).show();
                }
                // if buyer requested less than (or equal to) what's available
                else if (requestedQuantity <= totalAvailableQuantity){
                    List<ItemHolder> itemHolders = mInventoryLogDAO.getItemHolderByTitle(requestedTitle);
                    for(ItemHolder itemHolder : itemHolders){
                        //if requested items to buy is less seller's inventory
                        if(remainingRequestedQuantity < itemHolder.getQuantity() ){
                            int newQuantity = itemHolder.getQuantity() - remainingRequestedQuantity;
                            remainingRequestedQuantity = 0;
                            itemHolder.setQuantity(newQuantity);
                            mInventoryLogDAO.update(itemHolder);
                            break;
                        }
                        //if requested items to buy is more than seller's inventory
                        // reduced requested quantity, remove this itemHolder
                        // and continue to the next itemHolder
                        else if(remainingRequestedQuantity > itemHolder.getQuantity()){
                            remainingRequestedQuantity -= itemHolder.getQuantity();
                            mInventoryLogDAO.delete(itemHolder);
                        }
                        //else if requested items exactly matches seller's inventory
                        else{
                            remainingRequestedQuantity = 0;
                            mInventoryLogDAO.delete(itemHolder);
                            break;
                        }
                    }
                    // Update the requested item's quantity
                    requestedItem.setQuantity(totalAvailableQuantity - requestedQuantity);
                    mInventoryLogDAO.update(requestedItem);
                }
                // if requested items exactly matches all Inventorys'
                // remove the item and its associated itemHolders
                else{
                    List<ItemHolder> itemHolders = mInventoryLogDAO.getItemHolderByTitle(requestedTitle);
                    for(ItemHolder itemHolder : itemHolders) {
                        mInventoryLogDAO.delete(itemHolder);
                    }
                    mInventoryLogDAO.delete(requestedItem);
                }
                refreshDisplay();
            }
        });

        mExitSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WelcomeActivity.intentFactory(getApplicationContext(), mUser.getUserId());
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
    private void getValuesFromDisplay(){
        requestedTitle = mTitle.getText().toString();
        requestedQuantity = Integer.parseInt(mQuantity.getText().toString());

    }

    private void refreshDisplay(){
        mItems = mInventoryLogDAO.getAllItems();

        if(mItems.size() <= 0){
            mSearchDisplay.setText(R.string.noLogsMessage);
        }

        StringBuilder sb = new StringBuilder();
        for(Item item : mItems){
            sb.append(item);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mSearchDisplay.setText(sb.toString());
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
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}