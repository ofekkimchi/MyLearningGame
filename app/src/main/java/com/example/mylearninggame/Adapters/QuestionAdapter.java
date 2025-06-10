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

/**
 * אדפטר להצגת שאלות ברשימה
 * מטפל בהצגה ובאינטראקציה עם שאלות בממשק המנהל
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    /**
     * ממשק לטיפול באירועי לחיצה על שאלות
     * מספק קריאות חזרה ללחיצות רגילות וארוכות על שאלות
     */
    public interface QuestionClickListener {
        void onQuestionClick(Question question);
        void onLongQuestionClick(Question question);
    }

    private List<Question> questionList;
    private QuestionClickListener questionClickListener;

    /**
     * בנאי לאדפטר השאלות
     * @param questionClickListener מאזין לטיפול באירועי לחיצה על שאלות
     */
    public QuestionAdapter(@Nullable QuestionClickListener questionClickListener) {
        this.questionList = new ArrayList<>();
        this.questionClickListener = questionClickListener;
    }

    /**
     * יוצר מחזיק תצוגה חדש עבור פריט שאלה
     * @param parent קבוצת התצוגה האב
     * @param viewType סוג התצוגה ליצירה
     * @return מופע חדש של QuestionViewHolder
     */
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    /**
     * מקשר נתוני שאלה למחזיק התצוגה
     * מגדיר את הצגת השאלה ומאזיני לחיצה
     * @param holder מחזיק התצוגה לקישור נתונים
     * @param position מיקום השאלה ברשימה
     */
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

    /**
     * מחזיר את המספר הכולל של שאלות ברשימה
     * @return מספר השאלות
     */
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    /**
     * מעדכן את רשימת השאלות כולה
     * @param questions רשימת השאלות החדשה
     */
    public void setQuestionList(List<Question> questions) {
        this.questionList.clear();
        this.questionList.addAll(questions);
        notifyDataSetChanged();
    }

    /**
     * מוסיף שאלה בודדת לרשימה
     * @param question השאלה להוספה
     */
    public void addQuestion(Question question) {
        this.questionList.add(question);
        notifyItemInserted(this.questionList.size() - 1);
    }

    /**
     * מעדכן שאלה קיימת ברשימה
     * @param question השאלה המעודכנת
     */
    public void updateQuestion(Question question) {
        int index = questionList.indexOf(question);
        if (index == -1) return;
        questionList.set(index, question);
        notifyItemChanged(index);
    }

    /**
     * מסיר שאלה מהרשימה
     * @param user השאלה להסרה
     */
    public void removeQuestion(Question user) {
        int index = questionList.indexOf(user);
        if (index == -1) return;
        questionList.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * מקבל שאלה במיקום ספציפי
     * @param pos מיקום השאלה
     * @return השאלה במיקום המבוקש, או null אם המיקום לא תקין
     */
    public Question getByPosition(int pos) {
        if (pos < 0 || getItemCount() <= pos) return null;
        return questionList.get(pos);
    }

    /**
     * מחלקת מחזיק תצוגה עבור פריטי שאלה
     * מחזיקה הפניות לתצוגות בפריט השאלה
     */
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

    /**
     * מסיר שאלה במיקום ספציפי
     * @param position מיקום השאלה להסרה
     */
    public void removeItem(int position) {
        if (position < 0 || position >= questionList.size()) {
            return;
        }
        questionList.remove(position);
        notifyItemRemoved(position);
    }
}
