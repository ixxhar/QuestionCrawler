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

public class AnswerAdapterClass extends RecyclerView.Adapter<AnswerAdapterClass.AdapterViewHolder> {
    private List<AnswerModelClass> answerModelClassList;
    private Context context;
    private static final String TAG = "AnswerAdapterClass";

    private Transformation transformation;

    public AnswerAdapterClass(List<AnswerModelClass> answerModelClassList, Context context) {
        this.answerModelClassList = answerModelClassList;
        this.context = context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowforanswerslist, parent, false);
        AnswerAdapterClass.AdapterViewHolder adapterViewHolder = new AnswerAdapterClass.AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {
        transformationForPicasso();

        final AnswerModelClass answerModelClass = answerModelClassList.get(position);
        holder.tvAnswerTitles.setText(answerModelClass.getDescription());
        holder.tvAnswersPostedBy.setText(answerModelClass.getAnswerBy());

        FirebaseStorage.getInstance().getReference().child(answerModelClass.getAnswerByEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Picasso.get().load(task.getResult()).transform(transformation).into(holder.imAnswerPostedImage);
                } catch (Exception e) {
                    Picasso.get().load(R.drawable.image).transform(transformation).into(holder.imAnswerPostedImage);
                }
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,ViewCompleteAnswerActivity.class);
                intent.putExtra("SelectedAnswer",answerModelClassList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return answerModelClassList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAnswerTitles, tvAnswersPostedBy;
        private ImageView imAnswerPostedImage;
        private RelativeLayout parentLayout;


        public AdapterViewHolder(View itemView) {
            super(itemView);

            tvAnswerTitles = (TextView) itemView.findViewById(R.id.textViewAnswerTitle);
            tvAnswersPostedBy = (TextView) itemView.findViewById(R.id.textViewAnswerPostedBy);
            imAnswerPostedImage=(ImageView)itemView.findViewById(R.id.imageViewAnswerPostedUser);
            parentLayout=(RelativeLayout)itemView.findViewById(R.id.parentLayoutAnswer);

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
