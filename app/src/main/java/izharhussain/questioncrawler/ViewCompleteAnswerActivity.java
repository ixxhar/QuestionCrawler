package izharhussain.questioncrawler;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ViewCompleteAnswerActivity extends AppCompatActivity {

    private static final String TAG = "ViewCompleteAnswerActiv";

    private AnswerModelClass answerModelClass;

    private ImageView imSelectedAnswerBy;
    private TextView tvSelectedAnswerByName, tvSelectedAnswerDate, tvSelectedAnswerDescription;
    private Transformation transformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcompleteanswer);

        answerModelClass = (AnswerModelClass) getIntent().getSerializableExtra("SelectedAnswer");
        imSelectedAnswerBy = (ImageView) findViewById(R.id.imageViewAnswerPostedUserSelected);
        tvSelectedAnswerByName = (TextView) findViewById(R.id.textViewAnswerBySelected);
        tvSelectedAnswerDate = (TextView) findViewById(R.id.textViewAnswerPostedBySelected);
        tvSelectedAnswerDescription = (TextView) findViewById(R.id.textViewAnswerDescriptionSelected);

        transformationForPicasso();

        Log.d(TAG, "onCreate: " + answerModelClass.toString());

        FirebaseStorage.getInstance().getReference().child(answerModelClass.getAnswerByEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Picasso.get().load(task.getResult()).transform(transformation).into(imSelectedAnswerBy);
                } catch (Exception e) {
                    Picasso.get().load(R.drawable.image).transform(transformation).into(imSelectedAnswerBy);
                }
            }
        });

        tvSelectedAnswerByName.setText(answerModelClass.getAnswerBy());
        tvSelectedAnswerDate.setText(answerModelClass.getDate());
        tvSelectedAnswerDescription.setText(answerModelClass.getDescription());


    }

    private void transformationForPicasso() {
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
    }
}
