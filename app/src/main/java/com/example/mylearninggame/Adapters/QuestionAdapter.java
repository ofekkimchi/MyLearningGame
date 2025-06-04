package com.example.mylearninggame.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {


    public interface QuestionClickListener {
        void onQuestionClick(Question question);
        void onLongQuestionClick(Question question);
    }

    private List<Question> questionList;
    private QuestionClickListener questionClickListener;

    public QuestionAdapter(@Nullable QuestionClickListener questionClickListener) {
        this.questionList = new ArrayList<>();
        this.questionClickListener = questionClickListener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionClickListener != null) {
                    questionClickListener.onQuestionClick(question);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (questionClickListener != null) {
                    questionClickListener.onLongQuestionClick(question);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void setQuestionList(List<Question> questions) {
        this.questionList.clear();
        this.questionList.addAll(questions);
        notifyDataSetChanged();
    }

    public void addQuestion(Question question) {
        this.questionList.add(question);
        notifyItemInserted(this.questionList.size() - 1);
    }
    public void updateQuestion(Question question) {
        int index = questionList.indexOf(question);
        if (index == -1) return;
        questionList.set(index, question);
        notifyItemChanged(index);
    }

    public void removeQuestion(Question user) {
        int index = questionList.indexOf(user);
        if (index == -1) return;
        questionList.remove(index);
        notifyItemRemoved(index);
    }

    public Question getByPosition(int pos) {
        if (pos < 0 || getItemCount() <= pos) return null;
        return questionList.get(pos);
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

    public void removeItem(int position) {
        if (position < 0 || position >= questionList.size()) {
            return;
        }
        questionList.remove(position);
        notifyItemRemoved(position);

    }
}
