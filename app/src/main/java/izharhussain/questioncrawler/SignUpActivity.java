package izharhussain.questioncrawler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static int RESULT_LOAD_IMAGE = 1;

    private EditText etName, etPassword, etEmail, etCity, etConfirmPassword;
    private ImageView imUserImage;
    private UserModelClass userModelClass;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    private Uri selectedImageURI;

    private Transformation transformation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        bindViews();
        forFirebaseInstances();
        signupYo();
        loginYO();
        forImageYO();

    }

    private void forImageYO() {

        imUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void loginYO() {
        findViewById(R.id.buttonLoginNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });
    }

    boolean forSignupFields() {
        if (etEmail.getText().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(String.valueOf(etEmail.getText())).matches()) {
            Toast.makeText(getApplicationContext(), "Incorrect Email Address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etName.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter Your Name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPassword.getText().length() <= 6) {
            Toast.makeText(getApplicationContext(), "Password should be more than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void signupYo() {
        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (forSignupFields()&&String.valueOf(etPassword.getText()).equals(String.valueOf(etConfirmPassword.getText()))) {
                    Log.d(TAG, "onClick: Shit is working");
                    firebaseAuth.createUserWithEmailAndPassword(String.valueOf(etEmail.getText()), String.valueOf(etPassword.getText())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(etName.getText())).build();

                                user.updateProfile(profileUpdates);

                                addUserRecordToDatabase(task.getResult().getUser());
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "confirm Password!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: shit is not working...");
                }
            }
        });
    }

    private void addUserRecordToDatabase(final FirebaseUser user) {
        userModelClass.setName(String.valueOf(etName.getText()));
        userModelClass.setEmail(String.valueOf(etEmail.getText()));
        userModelClass.setPassword(String.valueOf(etPassword.getText()));
        userModelClass.setCity(String.valueOf(etCity.getText()));
        userModelClass.setCreatedAt(new Date().getTime());
        userModelClass.setConnection(UsersChatAdapter.ONLINE);

        try {
            StorageReference filePath = storageReference.child(String.valueOf(etEmail.getText())).child("profilephoto");

            filePath.putFile(selectedImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseReference.child("RegisteredUsers").child(user.getUid()).setValue(userModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    goToMenuActivity();
                }
            }
        });

        Log.d(TAG, "onClick: User Entered");
        Toast.makeText(getApplicationContext(), "user entered successfully", Toast.LENGTH_SHORT).show();
    }

    private void forFirebaseInstances() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userModelClass = new UserModelClass();
    }

    private void bindViews() {
        etName = (EditText) findViewById(R.id.editTextNameSignUp);
        etEmail = (EditText) findViewById(R.id.editTextEmailSignUp);
        etPassword = (EditText) findViewById(R.id.editTextPasswordSignUp);
        etCity = (EditText) findViewById(R.id.editTextCitySignup);
        imUserImage = (ImageView) findViewById(R.id.imageViewUserImage);
        etConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPasswordSignUp);
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(SignUpActivity.this, NavBottomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectedImageURI = data.getData();
        Picasso.get().load(selectedImageURI).transform(transformation).into(imUserImage);
    }

    private void transformationForPicasso(){
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
    }
}
