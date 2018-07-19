package izharhussain.questioncrawler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class QuestionAdapterClass extends RecyclerView.Adapter<QuestionAdapterClass.AdapterViewHolder> {

    private List<QuestionModelClass> questionModelClassList;
    private Context context;
    private static final String TAG = "QuestionAdapterClass";
    private FirebaseStorage firebaseStorage;
    private Transformation transformation;


    public QuestionAdapterClass(List<QuestionModelClass> questionModelClassList, Context context) {
        this.questionModelClassList = questionModelClassList;
        this.context = context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rowforquestionslist,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {

        transformationForPicasso();

        QuestionModelClass questionModelClass=questionModelClassList.get(position);
        holder.questionTitle.setText(questionModelClass.getTitle());
        holder.questionDate.setText(questionModelClass.getDate());

        FirebaseStorage.getInstance().getReference().child(questionModelClass.getPostedByEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Picasso.get().load(task.getResult()).transform(transformation).into(holder.postedByImage);
                } catch (Exception e) {
                    Picasso.get().load(R.drawable.image).transform(transformation).into(holder.postedByImage);
                }
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,SelectedQuestionActivity.class);
                intent.putExtra("SelectedQuestion",questionModelClassList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionModelClassList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTitle, questionDate;
        private RelativeLayout parentLayout;

        private ImageView postedByImage;

        public AdapterViewHolder(View itemView) {
            super(itemView);

            questionTitle=(TextView)itemView.findViewById(R.id.textViewQuestionTitle);
            questionDate=(TextView)itemView.findViewById(R.id.textViewQuestionPostedDate);
            parentLayout=(RelativeLayout)itemView.findViewById(R.id.parentLayoutQuestions);

            postedByImage=(ImageView)itemView.findViewById(R.id.imageViewQuestionPostedUser);
        }
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
