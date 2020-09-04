package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp;
    private TextView txtLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtLoginPage = findViewById(R.id.txtLoginPage);

        btnSignUp.setOnClickListener(this);
        txtLoginPage.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            // ParseUser.getCurrentUser().logOut();
            transitionToWhatsAppUsersActivity();
        }

    }



    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.btnSignUp :

                if (edtEmail.getText().toString().equals("")
                        || edtUsername.getText().toString().equals("")
                        ||  edtPassword.getText().toString().equals("")) {

                    Toast.makeText(MainActivity.this,
                            "Email, Username, Password is required!", Toast.LENGTH_LONG).show();


                }else {


                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, appUser.getUsername() + " is signed up successfully", Toast.LENGTH_LONG).show();
                                transitionToWhatsAppUsersActivity();
                            } else {
                                Toast.makeText(MainActivity.this, "There was en error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.txtLoginPage :

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }

    }

    public void rootLayoutTapped(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transitionToWhatsAppUsersActivity() {

        Intent intent = new Intent(MainActivity.this, WhatsAppUsers.class);
        startActivity(intent);
        finish();


    }
}