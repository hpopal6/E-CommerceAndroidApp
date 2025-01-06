package com.example.p2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.p2.db.AppDatabase;
import com.example.p2.db.InventoryLogDAO;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;

    private Button mCreateButton;
    private Button mCancelButton;
    private InventoryLogDAO mInventoryLogDAO;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;
    private User mUser;

    private int count = 0;
    private int count2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        wireupDisplay();
        getDatabase();
    }

    private void wireupDisplay() {
        mUsernameField = findViewById(R.id.editTextCreateUserName);
        mPasswordField = findViewById(R.id.editTextCreatePassword);
        mConfirmPasswordField = findViewById(R.id.editTextConfirmPassword);

        mCreateButton = findViewById(R.id.buttonCreateAccount);
        mCancelButton = findViewById(R.id.buttonCancelCreate);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getValuesFromDisplay();

                if(checkForUserAlreadyInDatabase()){
                    if(count < 3) {
                        Toast.makeText(CreateAccountActivity.this,
                                "Username already taken, try again", Toast.LENGTH_SHORT).show();
                        count++;
                    }
                    else{
                        count = 0;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateAccountActivity.this);
                        alertBuilder.setMessage("Login?");
                        alertBuilder.setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = LoginActivity.intentFactory(getApplicationContext());
                                        startActivity(intent);
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

                        //Toast.makeText(LoginActivity.this,
                         //       "Timeout buddy!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (!userNameMeetsMinimumSize()) {
                    Toast.makeText(CreateAccountActivity.this,
                            "Username needs to be at least 3 characters, try again", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordMeetsMinimumSize()){
                    Toast.makeText(CreateAccountActivity.this,
                            "Password needs to be at least 6 characters, try again", Toast.LENGTH_SHORT).show();
                }
                else if (!validatePasswords()) {
                        Toast.makeText(CreateAccountActivity.this,
                                "Passwords do not match, try again", Toast.LENGTH_SHORT).show();
                }
                else {
                        addUserToDatabase(mUsername, mPassword);
                    Toast.makeText(CreateAccountActivity.this,
                            mUsername + " user is created", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }
    private void addUserToDatabase(String mNewUsername, String mNewPassword){
        User mNewUser = new User(mNewUsername, mNewPassword);
        mInventoryLogDAO.insert(mNewUser);
    }
    private boolean validatePasswords(){
        return mPassword.equals(mConfirmPassword);
    }

    private boolean passwordMeetsMinimumSize() {
        if(mPassword.length() >= 6)
            return true;
        return false;
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
        mConfirmPassword = mConfirmPasswordField.getText().toString();
    }

    private boolean checkForUserAlreadyInDatabase(){
        mUser = mInventoryLogDAO.getUserByUsername(mUsername);
        if(mUser != null){
            Toast.makeText(this, "Username " + mUsername +
                    " already exists, try a different one", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private boolean userNameMeetsMinimumSize() {
        if(mUsername.length() >= 3)
            return true;
        return false;
    }

    private void getDatabase(){
        mInventoryLogDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getInventoryLogDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, CreateAccountActivity.class);

        return intent;
    }
}