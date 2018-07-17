package izharhussain.questioncrawler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class QuestionAdapterClass extends RecyclerView.Adapter<QuestionAdapterClass.AdapterViewHolder> {

    private List<QuestionModelClass> questionModelClassList;
    private Context context;
    private static final String TAG = "QuestionAdapterClass";

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
    public void onBindViewHolder(AdapterViewHolder holder, final int position) {
        QuestionModelClass questionModelClass=questionModelClassList.get(position);
        holder.questionTitle.setText(questionModelClass.getTitle());
        holder.questionDate.setText(questionModelClass.getDate());

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

        public AdapterViewHolder(View itemView) {
            super(itemView);

            questionTitle=(TextView)itemView.findViewById(R.id.textViewQuestionTitle);
            questionDate=(TextView)itemView.findViewById(R.id.textViewQuestionPostedDate);
            parentLayout=(RelativeLayout)itemView.findViewById(R.id.parentLayoutQuestions);
        }
    }
}
