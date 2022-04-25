package com.example.handiwalk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText registerUsernameField;
    EditText registerPasswordField;
    EditText registerRepeatPasswordField;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        setListenersToButtons();
    }

    private void findViews() {
        registerUsernameField = findViewById(R.id.registerUsernameField);
        registerPasswordField = findViewById(R.id.registerPasswordField);
        registerRepeatPasswordField = findViewById(R.id.registerRepeatPaswordField);
        registerButton = findViewById(R.id.RegisterButton);
    }

    private void setListenersToButtons() {
        registerButton.setOnClickListener(
                view -> {
                    {
                        register(registerRepeatPasswordField.getText().toString(), registerUsernameField.getText().toString(), registerPasswordField.getText().toString());
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                }
        );
    }

    private void register(String email, String username, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
