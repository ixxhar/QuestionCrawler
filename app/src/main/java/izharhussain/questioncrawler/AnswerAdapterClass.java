package izharhussain.questioncrawler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AnswerAdapterClass extends RecyclerView.Adapter<AnswerAdapterClass.AdapterViewHolder> {
    private List<AnswerModelClass> answerModelClassList;
    private Context context;
    private static final String TAG = "AnswerAdapterClass";

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
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        AnswerModelClass answerModelClass = answerModelClassList.get(position);
        holder.tvAnswerTitles.setText(answerModelClass.getDescription());
        holder.tvAnswersPostedBy.setText(answerModelClass.getAnswerBy());

    }

    @Override
    public int getItemCount() {
        return answerModelClassList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAnswerTitles, tvAnswersPostedBy;

        public AdapterViewHolder(View itemView) {
            super(itemView);

            tvAnswerTitles = (TextView) itemView.findViewById(R.id.textViewAnswerTitle);
            tvAnswersPostedBy = (TextView) itemView.findViewById(R.id.textViewAnswerPostedBy);
        }
    }
}
