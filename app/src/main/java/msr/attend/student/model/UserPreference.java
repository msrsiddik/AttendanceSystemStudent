package msr.attend.student.model;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setStudentIdPref(String id){
        editor.putString("id",id);
        editor.commit();
    }

    public String getStudentIdPref(){
        return sharedPreferences.getString("id","");
    }

    public void setBatchPref(String batch){
        editor.putString("batch",batch);
        editor.commit();
    }

    public String getBatchPref(){
        return sharedPreferences.getString("batch","");
    }

    public void setLoginStatus(boolean loginStatus){
        editor.putBoolean("status", loginStatus);
        editor.commit();
    }

    public boolean getLoginStatus(){
        return sharedPreferences.getBoolean("status", false);
    }

    public void setUserName(String name) {
        editor.putString("userName",name);
        editor.commit();
    }

    public String getUserName(){
        return sharedPreferences.getString("userName",null);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
