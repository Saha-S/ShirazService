package com.alizare.server.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alizare.server.App;
import com.alizare.server.R;

import pt.joaocruz04.lib.SOAPManager;
import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

public class LoginActivity extends AppCompatActivity {

    EditText etusername,etpassword;
    Button   btnlogin;
    ProgressBar pblogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etusername  =(EditText) findViewById(R.id.username_et);
        etpassword  =(EditText) findViewById(R.id.password_et);
        btnlogin    =(Button)   findViewById(R.id.login_btn);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new LoginTask(etusername.getText().toString(),etpassword.getText().toString());

            }
        });
    }



    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String user;
        private final String pass;

        LoginTask(String username, String password) {
            user = username;
            pass = password;
        }


        @Override
        protected void onPreExecute() {
            pblogin.setVisibility(View.VISIBLE);
        }



        @Override
        protected Boolean doInBackground(Void... params) {



            try {

            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }


            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                App.CustomToast("خوش آمدید");
                App.newactivity(LoginActivity.this,MainActivity.class);
                finish();

            } else {
                pblogin.setVisibility(View.INVISIBLE);
                etpassword.setError("error");
                etpassword.requestFocus();

            }
        }

        @Override
        protected void onCancelled() {
            pblogin.setVisibility(View.INVISIBLE);
        }
    }


}
