package izharhussain.questioncrawler;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class SelectedQuestionActivity extends AppCompatActivity {
    private static final String TAG = "SelectedQuestionActivit";
    private QuestionModelClass questionModelClass;
    private TextView tvSelectedQuestionTitle, tvSelectedQuestionDescription, tvSelectedQuestionAskedBy, tvSelectedQuestionNumberOfLikes;
    private ImageView imUserImage;
    private AnswerAdapterClass answerAdapterClass;
    private DatabaseReference databaseReference;
    private List<AnswerModelClass> answerModelClassList;

    private TextView tvTimeLimitGiven;
    private Transformation transformation;
    private FirebaseAuth firebaseAuth;

    private boolean alreadyLiked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedquestion);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        answerModelClassList = new ArrayList<>();

        questionModelClass = (QuestionModelClass) getIntent().getSerializableExtra("SelectedQuestion");

        Log.d(TAG, "onCreate: " + questionModelClass.toString());

        tvSelectedQuestionDescription = (TextView) findViewById(R.id.textViewSelectedQuestionDescription);
        tvSelectedQuestionTitle = (TextView) findViewById(R.id.textViewSelectedQuestionTitle);
        tvSelectedQuestionAskedBy = (TextView) findViewById(R.id.textViewSelectedQuestionAskedBy);
        tvTimeLimitGiven = (TextView) findViewById(R.id.textViewTimeLimitGiven);
        imUserImage = (ImageView) findViewById(R.id.imageViewSelectedQuestionUserImage);
        tvSelectedQuestionNumberOfLikes=(TextView)findViewById(R.id.textViewQuestionLikes);


        tvSelectedQuestionTitle.setText(questionModelClass.getTitle());
        tvSelectedQuestionAskedBy.setText(questionModelClass.getPostedBy());
        tvSelectedQuestionDescription.setText(questionModelClass.getDescription());
        tvTimeLimitGiven.setText(questionModelClass.getTimeLimit());

        transformationForPicasso();
        forTheGloryOfLikes();

        FirebaseStorage.getInstance().getReference().child(questionModelClass.getPostedByEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Picasso.get().load(task.getResult()).transform(transformation).into(imUserImage);
                } catch (Exception e) {
                    Picasso.get().load(R.drawable.image).transform(transformation).into(imUserImage);
                }
            }
        });

        databaseReference.child("Answers").orderByChild("questionID").equalTo(questionModelClass.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                intent.putExtra("SelectedQuestion",questionModelClass);
                intent.putExtra("SelectedQuestionForAnswer", questionModelClass);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonSelectedQuestionVote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("LikedQuestions").orderByChild("questionID").equalTo(questionModelClass.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List list = new ArrayList<Like>();
                        for (DataSnapshot dSShot : dataSnapshot.getChildren()) {
                            list.add(dSShot.getValue(Like.class));
                        }
                        Log.d(TAG, "Records: " + list.size());

                        for (int i = 0; i < list.size(); i++) {

                            Like like = (Like) list.get(i);

                            if (new String(like.getUserID()).equals(firebaseAuth.getUid())) {
                                alreadyLiked = true;
                            }
                        }

                        if (!alreadyLiked) {
                            Toast.makeText(getApplicationContext(),"Liked",Toast.LENGTH_SHORT).show();
                            databaseReference.child("LikedQuestions").push().setValue(new Like(questionModelClass.getId(), firebaseAuth.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        forTheGloryOfLikes();
                                        Log.d(TAG, "onComplete: duck ueaj");
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),"Already Liked",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: " + "already liked!");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

    private void transformationForPicasso() {
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
    }

    private void forTheGloryOfLikes() {
        databaseReference.child("LikedQuestions").orderByChild("questionID").equalTo(questionModelClass.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List list = new ArrayList<Like>();
                for (DataSnapshot dSShot : dataSnapshot.getChildren()) {
                    list.add(dSShot.getValue(Like.class));
                }
                Log.d(TAG, "Records: " + list.size());
                tvSelectedQuestionNumberOfLikes.setText(String.valueOf(list.size()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
