package izharhussain.questioncrawler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.ImageButton;

public class ItemOne extends Fragment implements View.OnClickListener {

    private ImageButton one, two, thee, four, five, six, seven, eight;
    private Intent intent;

    public static ItemOne newInstance() {
        ItemOne fragment = new ItemOne();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.item_one, container, false);

        one = (ImageButton) v.findViewById(R.id.one);
        two = (ImageButton) v.findViewById(R.id.two);
        thee = (ImageButton) v.findViewById(R.id.three);
        four = (ImageButton) v.findViewById(R.id.four);
        five = (ImageButton) v.findViewById(R.id.five);
        six = (ImageButton) v.findViewById(R.id.six);
        seven = (ImageButton) v.findViewById(R.id.seven);
        eight = (ImageButton) v.findViewById(R.id.eight);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        thee.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);

        return v;
        //return inflater.inflate(R.layout.item_one, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "program");
                startActivity(intent);
                break;

            case R.id.two:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "softwaredesign");
                startActivity(intent);
                break;
            case R.id.three:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "calculus");
                startActivity(intent);
                break;

            case R.id.four:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "softwareissue");
                startActivity(intent);
                break;
            case R.id.five:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "project");
                startActivity(intent);
                break;

            case R.id.six:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "softwarematerial");
                startActivity(intent);
                break;
            case R.id.seven:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "admission");
                startActivity(intent);
                break;

            case R.id.eight:
                intent = new Intent(getContext(), QuestionsActivity.class);
                intent.putExtra("category", "other");
                startActivity(intent);
                break;

            default:
                //nothing here
        }
    }
}
