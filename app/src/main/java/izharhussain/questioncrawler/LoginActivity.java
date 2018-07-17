package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0) {
                    Log.d(TAG, "onClick: empty fields");
                    Toast.makeText(getApplicationContext(), "one of the field is empty", Toast.LENGTH_SHORT).show();
                } else {

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(String.valueOf(etEmail.getText())).matches()) {
                        Log.d(TAG, "onClick: incorrect email address");
                        Toast.makeText(getApplicationContext(), "incorrect email address", Toast.LENGTH_SHORT).show();

                    } else {
                        firebaseAuth.signInWithEmailAndPassword(String.valueOf(etEmail.getText()), String.valueOf(etPassword.getText())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();
                                    setUserOnline();
                                    goToMenuActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "no such user exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });

    }

    private void setUserOnline() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance()
                    .getReference().
                    child("RegisteredUsers").
                    child(userId).
                    child("connection").
                    setValue(UsersChatAdapter.ONLINE);
        }
    }

    private void bindViews() {
        etEmail = (EditText) findViewById(R.id.editTextEmailLogin);
        etPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, NavBottomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
