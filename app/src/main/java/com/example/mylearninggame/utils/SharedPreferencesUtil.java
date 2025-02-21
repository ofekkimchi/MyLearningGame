package com.example.mylearninggame.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPreferencesUtil {
    private static final String PREF_NAME = "com.example.testapp.PREFERENCE_FILE_KEY";
    private static final String QUESTIONS_KEY = "questions_list";

    /// שמירת מחרוזת ל-SharedPreferences
    private static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /// טעינת מחרוזת מ-SharedPreferences
    private static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /// שמירת מספר שלם ל-SharedPreferences
    private static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /// טעינת מספר שלם מ-SharedPreferences
    private static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /// מחיקת נתונים מ-SharedPreferences
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /// הסרת מפתח ספציפי מ-SharedPreferences
    private static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /// בדיקה אם מפתח מסוים קיים ב-SharedPreferences
    private static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    /// שמירת משתמש ל-SharedPreferences
    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("fName", user.getFname());
        editor.putString("lName", user.getLname());
        editor.putString("phone", user.getPhone());
        editor.putBoolean("isAdmin", user.getIsAdmin());
        editor.apply();
    }

    /// טעינת משתמש מ-SharedPreferences
    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!isUserLoggedIn(context)) {
            return null;
        }
        String uid = sharedPreferences.getString("uid", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        String fName = sharedPreferences.getString("fName", "");
        String lName = sharedPreferences.getString("lName", "");
        String phone = sharedPreferences.getString("phone", "");
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
        return new User(uid, email, password, fName, lName, phone, isAdmin);
    }

    /// התנתקות משתמש על ידי הסרת הנתונים שלו
    public static void signOutUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("uid");
        editor.remove("email");
        editor.remove("password");
        editor.remove("fName");
        editor.remove("lName");
        editor.remove("phone");
        editor.remove("isAdmin");
        editor.apply();
    }

    /// בדיקה אם משתמש מחובר
    public static boolean isUserLoggedIn(Context context) {
        return contains(context, "uid");
    }

    // 🆕 שמירת רשימת השאלות
    public static void saveQuestions(Context context, List<Question> questions) {
        Gson gson = new Gson();
        String json = gson.toJson(questions);
        saveString(context, QUESTIONS_KEY, json);
    }

    //  טעינת רשימת השאלות
    public static List<Question> loadQuestions(Context context) {
        String json = getString(context, QUESTIONS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Question>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static void saveQuestions(Context context, ArrayList<Question> questions) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(questions);  // המרת הרשימה ל-JSON
        editor.putString("questions_list", json);  // שימור ה-JSON ב-SharedPreferences
        editor.apply();  // שמירה א-סינכרונית
    }
}
