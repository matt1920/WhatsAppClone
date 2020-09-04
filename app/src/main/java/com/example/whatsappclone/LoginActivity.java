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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmailLogin, edtPasswordLogin;
    Button btnLoginLogin;
    TextView txtSignUpPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        txtSignUpPage = findViewById(R.id.txtSignUpPage);


        btnLoginLogin.setOnClickListener(this);
        txtSignUpPage.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){

            ParseUser.getCurrentUser().logOut();
        }
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnLoginLogin :
                if (edtEmailLogin.getText().toString().equals("")  ||  edtPasswordLogin.getText().toString().equals("")) {

                    Toast.makeText(LoginActivity.this,
                            "Email and password is required!", Toast.LENGTH_LONG).show();

                }
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging in " + edtEmailLogin.getText().toString());
                    progressDialog.show();

                    ParseUser.logInInBackground(edtEmailLogin.getText().toString(), edtPasswordLogin.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                Toast.makeText(LoginActivity.this, user.getUsername() + " is logged in successfully", Toast.LENGTH_LONG).show();
                                transitionToWhatsAppUsers();

                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.txtSignUpPage :
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    public void rootLayoutTappedLogin(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void transitionToWhatsAppUsers() {

        Intent intent = new Intent(LoginActivity.this, WhatsAppUsers.class);
        startActivity(intent);
        finish();

    }
}

