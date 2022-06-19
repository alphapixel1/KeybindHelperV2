package com.example.keybindhelperv2.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class Project {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    @TypeConverters({DateConverter.class})
    public Date lastAccessed;

    public void updateLastAccessed(){
        lastAccessed=new Date();
    }
}
