package com.example.emailpasswordauthentivation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextInputEditText emailInput, passwordInput,confirmPassword;
    Button registerButton;
    FirebaseAuth mAuth;
    Dialog dialog;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPassword = findViewById(R.id.confirmpassword_input);
        registerButton = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.logintextview);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = emailInput.getText().toString();
                String Password = passwordInput.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {

                    if (Password.matches(ConfirmPassword) && Password.length() >= 8)
                    {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                        dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_wait);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        mAuth.createUserWithEmailAndPassword(Email,Password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(MainActivity.this,"Successfuly created",Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null)
                                            {

                                                dialog.dismiss();
                                                Intent i = new Intent(MainActivity.this,MainPage.class);
                                                startActivity(i);
                                                finish();

                                            }

                                        }else {


                                            dialog.dismiss();
                                            Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });

                    }else if (Password.length() < 8){

                        passwordInput.setError("Password must be atleast 8 character long");
                        passwordInput.requestFocus();


                    }else {

                        confirmPassword.setError("Password doesn't match");
                        confirmPassword.requestFocus();
                    }


                }else {

                    emailInput.setError("Please enter a valid Email Address");
                    emailInput.requestFocus();
                }

            }
        });
    }
}