package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectedQuestionActivity extends AppCompatActivity {
    private static final String TAG = "SelectedQuestionActivit";
    private QuestionModelClass questionModelClass;
    private TextView tvSelectedQuestionTitle, tvSelectedQuestionDescription, tvSelectedQuestionAskedBy;
    private AnswerAdapterClass answerAdapterClass;
    private DatabaseReference databaseReference;
    private List<AnswerModelClass> answerModelClassList;

    private TextView tvTimeLimitGiven;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedquestion);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        answerModelClassList = new ArrayList<>();

        questionModelClass = (QuestionModelClass) getIntent().getSerializableExtra("SelectedQuestion");

        tvSelectedQuestionDescription = (TextView) findViewById(R.id.textViewSelectedQuestionDescription);
        tvSelectedQuestionTitle = (TextView) findViewById(R.id.textViewSelectedQuestionTitle);
        tvSelectedQuestionAskedBy = (TextView) findViewById(R.id.textViewSelectedQuestionAskedBy);
        tvTimeLimitGiven = (TextView) findViewById(R.id.textViewTimeLimitGiven);

        tvSelectedQuestionTitle.setText(questionModelClass.getTitle());
        tvSelectedQuestionAskedBy.setText(questionModelClass.getPostedBy());
        tvSelectedQuestionDescription.setText(questionModelClass.getDescription());
        tvTimeLimitGiven.setText(questionModelClass.getTimeLimit());

        databaseReference.child("Answers").orderByChild("questionID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List list = new ArrayList<AnswerModelClass>();
                for (DataSnapshot dSShot : dataSnapshot.getChildren()) {
                    list.add(dSShot.getValue(AnswerModelClass.class));
                }
                Log.d(TAG, "Records: " + list.size());

                for (int i = 0; i < list.size(); i++) {

                    AnswerModelClass answerModelClass = (AnswerModelClass) list.get(i);
                    answerModelClassList.add(answerModelClass);
                }

                forRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.buttonSelectedQuestionReply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedQuestionActivity.this, PostAnswerActivity.class);
                intent.putExtra("SelectedQuestionForAnswer", questionModelClass);
                startActivity(intent);
            }
        });
    }

    private void forRecyclerView() {
        answerAdapterClass = new AnswerAdapterClass(answerModelClassList, SelectedQuestionActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListOfAnswers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(answerAdapterClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedQuestionActivity.this));
    }

}
