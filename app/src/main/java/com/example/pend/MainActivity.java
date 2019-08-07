package com.example.pend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText edtuserid, edtpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.editText);
        edtpass = (EditText) findViewById(R.id.editText2);
        btnlogin = (Button) findViewById(R.id.btn_login);
        pbbar = (ProgressBar) findViewById(R.id.progressBar);
        pbbar.setVisibility(View.GONE);


        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivity2();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
    }//onCreate

    public void openActivity2() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);



    }//onCreate

    public class DoLogin extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isSuccess = false;

        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (userid.trim().equals("")||password.trim().equals("")) {
                message = "Please enter User ID and Password";
            } else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        message = " Error in connection with SQL Server";
                    } else {
                        String query = "select * from User1 where userid ='" + userid + "' and userpassword='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            String username = rs.getString("UserFname")+" "+rs.getString("UserLname");
                            message = "Login Sucessfull...Hello Khun: " + userid + ":"+username;
                            isSuccess = true;
                            openActivity2();
                        } else {
                            message = "Invalid Credentials " ;
                            isSuccess = false;
                        }
                    }

                } catch (Exception ex) {
                    isSuccess = false;
                    message = ex + " Exceptions";
                }
            }
            return message;
        }//doInBackground

    }//DoLogin

}//main
