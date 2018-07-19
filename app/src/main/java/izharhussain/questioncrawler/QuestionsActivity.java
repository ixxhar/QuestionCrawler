package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinnerCategory;
    private String sortCategorySelected;

    private RecyclerView recyclerView;


    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        questionModelClassList = new ArrayList<>();

        intent = getIntent();
        String category = intent.getStringExtra("category");
        Log.d(TAG, "onCreate: "+category);

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


        //for spinner
        spinnerCategory = (Spinner) findViewById(R.id.spinnerTypeSort);
        adapter = ArrayAdapter.createFromResource(this, R.array.sortitem_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortCategorySelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),sortCategorySelected,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void forRecyclerView() {
        questionAdapterClass = new QuestionAdapterClass(questionModelClassList, QuestionsActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListOfQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(questionAdapterClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
    }
}
