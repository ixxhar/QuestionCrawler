package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostAnswerActivity extends AppCompatActivity {
    private static final String TAG = "PostAnswerActivity";
    private EditText etAnswerDescription;
    private TextView tvQSD;
    private DatabaseReference databaseReference;
    private QuestionModelClass questionModelClass;
    private AnswerModelClass answerModelClass;
    private FirebaseAuth firebaseAuth;

    private String questionID, answerPostDate, answeredBy = "punkass";//values got from intents


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postanswer);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            answeredBy = firebaseAuth.getCurrentUser().getDisplayName();

            SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date myDate = new Date();
            answerPostDate = timeStampFormat.format(myDate);

            questionModelClass = (QuestionModelClass) getIntent().getSerializableExtra("SelectedQuestionForAnswer");
            tvQSD = (TextView) findViewById(R.id.textViewQuestionDescription);
            etAnswerDescription = (EditText) findViewById(R.id.editTextYourAnswer);
            tvQSD.setText(questionModelClass.getDescription());
            questionID = questionModelClass.getId();


            answerModelClass = new AnswerModelClass();

            findViewById(R.id.buttonPostAnswer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAnswerDescription.getText().length() == 0) {
                        Log.d(TAG, "onClick: Empty Fields!");
                        Toast.makeText(getApplicationContext(), "one of the field is empty", Toast.LENGTH_SHORT).show();
                    } else {

                        answerModelClass.setDescription(String.valueOf(etAnswerDescription.getText()));
                        answerModelClass.setAnswerBy(answeredBy);
                        answerModelClass.setDate(answerPostDate);
                        answerModelClass.setQuestionID(questionID);

                        databaseReference.child("Answers").push().setValue(answerModelClass);

                        Log.d(TAG, "onClick: User Entered");
                        Toast.makeText(getApplicationContext(), "user entered successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You're are not logged in, Redirecting you to...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PostAnswerActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
