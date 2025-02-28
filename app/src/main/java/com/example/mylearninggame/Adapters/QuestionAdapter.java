package com.example.mylearninggame.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Screens.Level;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    public QuestionAdapter(Level level, List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvWord.setText("שאלה: " + question.getWord());
        holder.tvRightAnswer.setText("✅ תשובה נכונה: " + question.getRightAnswer());
        holder.tvWrongAnswer1.setText("❌ " + question.getWrongAnswer1());
        holder.tvWrongAnswer2.setText("❌ " + question.getWrongAnswer2());
        holder.tvWrongAnswer3.setText("❌ " + question.getWrongAnswer3());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvRightAnswer, tvWrongAnswer1, tvWrongAnswer2, tvWrongAnswer3;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvRightAnswer = itemView.findViewById(R.id.tvRightAnswer);
            tvWrongAnswer1 = itemView.findViewById(R.id.tvWrongAnswer1);
            tvWrongAnswer2 = itemView.findViewById(R.id.tvWrongAnswer2);
            tvWrongAnswer3 = itemView.findViewById(R.id.tvWrongAnswer3);
        }
    }
    public void updateQuestionsList(ArrayList<Question> newQuestionsList) {
        this.questionList.clear();
        this.questionList.addAll(newQuestionsList);
        notifyDataSetChanged(); // מעדכן את ה-RecyclerView
    }
}
