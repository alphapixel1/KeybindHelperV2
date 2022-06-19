package com.example.keybindhelperv2;

import android.content.Context;

import androidx.room.Room;

import com.example.keybindhelperv2.Room.AppDatabase;
import com.example.keybindhelperv2.Room.ProjectDao;

public class DatabaseManager {
    public static ProjectDao db;
    public static void init(Context c){
        AppDatabase db = Room.databaseBuilder(c, AppDatabase.class, "ProjectDB").allowMainThreadQueries().build();
        DatabaseManager.db =db.projectDao();

    }
}
