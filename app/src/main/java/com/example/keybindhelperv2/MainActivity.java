package com.example.keybindhelperv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.keybindhelperv2.Adapters.GroupAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager.init(MainActivity.this);
        CurrentProject.loadFirstProject();
        RecyclerView rv= findViewById(R.id.recyclerView);
        rv.setAdapter(new GroupAdapter(CurrentProject.Groups));
        rv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.floatingActionButton).setOnClickListener(z->{
            CurrentProject.AddGroup();
            System.out.println("MainActivity.floatingactionbutton.click: Groups Size:"+CurrentProject.Groups.size());
            rv.getAdapter().notifyItemChanged(CurrentProject.Groups.size()-1);
        });
        findViewById(R.id.deleteallgroups).setOnClickListener(v->{
            //int s=CurrentProject.Groups.size()-1;
            CurrentProject.deleteAllGroups();
            rv.setAdapter(new GroupAdapter(CurrentProject.Groups));

        });

        //DatabaseManager.Project.deleteAllProjectsGroups(CurrentProject.CurrentProject.id);
        //rv.getAdapter().notifyAll();
    }
}
