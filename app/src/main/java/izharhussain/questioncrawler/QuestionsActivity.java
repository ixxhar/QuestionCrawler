package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsActivity";
    private QuestionAdapterClass questionAdapterClass;
    private DatabaseReference databaseReference;
    private List<QuestionModelClass> questionModelClassList;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        questionModelClassList = new ArrayList<>();

        intent = getIntent();
        String category = intent.getStringExtra("category");

        databaseReference.child("Questions").orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List list = new ArrayList<QuestionModelClass>();
                for (DataSnapshot dSShot : dataSnapshot.getChildren()) {
                    list.add(dSShot.getValue(QuestionModelClass.class));
                }
                Log.d(TAG, "Records: " + list.size());

                for (int i = 0; i < list.size(); i++) {

                    QuestionModelClass questionModelClass = (QuestionModelClass) list.get(i);
                    questionModelClassList.add(questionModelClass);
                }

                forRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void forRecyclerView() {
        questionAdapterClass = new QuestionAdapterClass(questionModelClassList, QuestionsActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListOfQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(questionAdapterClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
    }
}
