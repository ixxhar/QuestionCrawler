package izharhussain.questioncrawler;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostQuestionActivity extends AppCompatActivity {

    private static final String TAG = "PostQuestionActivity";
    private EditText etQuestionTitle;
    private EditText etQuestionDescription;
    private TextView tvTimeLimit;
    private QuestionModelClass questionModelClass;
    private DatabaseReference databaseReference;
    private Spinner spinnerCategory;
    private ArrayAdapter<CharSequence> adapter;

    private String id = "1";//generated randomly
    private String postedBy = "ao";//object value from intent
    private String categorySelected;
    private String questionPostDate;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postquestion);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() != null) {

            etQuestionTitle = (EditText) findViewById(R.id.editTextQuestionTitle);
            etQuestionDescription = (EditText) findViewById(R.id.editTextQuestionDescription);
            tvTimeLimit = (TextView) findViewById(R.id.textViewTimeLimit);

            findViewById(R.id.imageButtonSetTime).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(PostQuestionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            tvTimeLimit.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

            SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date myDate = new Date();
            questionPostDate = timeStampFormat.format(myDate);

            id = databaseReference.getDatabase().getReference("Questions").push().getKey();
            postedBy = firebaseAuth.getCurrentUser().getDisplayName();


            //for spinner
            spinnerCategory = (Spinner) findViewById(R.id.spinnerQuestionCategory);
            adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    categorySelected = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            questionModelClass = new QuestionModelClass();

            findViewById(R.id.buttonQuestionPost).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etQuestionTitle.getText().length() == 0 || etQuestionDescription.getText().length() == 0) {
                        Log.d(TAG, "onClick: Empty Fields!");
                        Toast.makeText(getApplicationContext(), "one of the field is empty", Toast.LENGTH_SHORT).show();
                    } else {

                        questionModelClass.setTitle(String.valueOf(etQuestionTitle.getText()));
                        questionModelClass.setDescription(String.valueOf(etQuestionDescription.getText()));
                        questionModelClass.setId(id);
                        questionModelClass.setCategory(categorySelected);
                        questionModelClass.setDate(questionPostDate);
                        questionModelClass.setPostedBy(postedBy);
                        questionModelClass.setTimeLimit(String.valueOf(tvTimeLimit.getText()));

                        databaseReference.child("Questions").push().setValue(questionModelClass);

                        Log.d(TAG, "onClick: User Entered");
                        Toast.makeText(getApplicationContext(), "question posted successfully", Toast.LENGTH_SHORT).show();
                        goToMenuActivity();

                    }
                }
            });

        } else {
            Intent intent = new Intent(PostQuestionActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(PostQuestionActivity.this, NavBottomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
