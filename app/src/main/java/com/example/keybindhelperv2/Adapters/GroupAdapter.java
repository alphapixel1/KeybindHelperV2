package com.example.keybindhelperv2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keybindhelperv2.CurrentProject;
import com.example.keybindhelperv2.DatabaseManager;
import com.example.keybindhelperv2.Dialogs.ArrowProvider;
import com.example.keybindhelperv2.Dialogs.GroupListProvider;
import com.example.keybindhelperv2.Dialogs.PromptDialog;
import com.example.keybindhelperv2.Dialogs.ValidatorResponse;
import com.example.keybindhelperv2.R;
import com.example.keybindhelperv2.Room.Group;
import com.example.keybindhelperv2.Room.Keybind;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groupList;

    public GroupAdapter(List<Group> groupList){
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_layout,parent,false);

        return new GroupViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group=groupList.get(position);
        group.currentAdapter=this;
        Context context=holder.itemView.getContext();

        RecyclerView rv= holder.itemView.findViewById(R.id.keybind_zone);
        rv.setAdapter(new KeybindAdapter(group.keybinds));
        rv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //LinearLayoutManager llm=new LinearLayoutManager(holder.itemView.getContext());

        Button nameBtn=holder.itemView.findViewById(R.id.group_name_button);
        nameBtn.setText(group.name);
        nameBtn.setOnClickListener(v->{
            PromptDialog pd= new PromptDialog(
                    holder.itemView.getContext(),
                    "Rename Group",
                    "",
                    group.name
            );
            pd.validation= text -> new ValidatorResponse(
                    text.equals(group.name) || CurrentProject.isGroupNameAvailable(text),
                    "Name Has Already Been Taken");
            pd.confirmedEvent= n->{
                group.name=n;
                nameBtn.setText(n);
                DatabaseManager.db.update(group);
            };
            pd.ShowDialog();
        });

        holder.itemView.findViewById(R.id.group_add_button).setOnClickListener(v->{
            group.AddKeybind();

            rv.getAdapter().notifyItemChanged(group.keybinds.size()-1);
            if(rv.getVisibility()!=View.VISIBLE)
                rv.setVisibility(View.VISIBLE);
        });

        holder.itemView.findViewById(R.id.group_more_button).setOnClickListener(z->{
            Dialog d=new Dialog(context);
            d.setContentView(R.layout.group_menu);
            ((TextView)d.findViewById(R.id.group_name)).setText(group.name);
            d.findViewById(R.id.group_clear_keybinds).setOnClickListener(v->{
                DatabaseManager.db.deleteGroupKeybinds(group.id);
                /*for (Keybind k:group.keybinds) {
                    DatabaseManager.Project.delete(k);
                }*/
                group.keybinds.clear();
                try {
                    rv.getAdapter().notifyDataSetChanged();
                }catch (Exception e){

                }
                d.cancel();
            });
            d.findViewById(R.id.group_delete).setOnClickListener(v->{
                notifyItemRemoved(CurrentProject.Groups.indexOf(group));
                CurrentProject.Groups.remove(group);
                DatabaseManager.db.deleteGroup(group.id);

                d.cancel();
            });
            d.findViewById(R.id.group_showhide).setOnClickListener(v->{
                rv.setVisibility(rv.getVisibility()==View.VISIBLE? View.GONE:View.VISIBLE);
                d.cancel();
            });
            d.findViewById(R.id.group_copy).setOnClickListener(v->{
                Group g=group.Clone();
                notifyItemInserted(g.index);
                d.cancel();
            });
            d.findViewById(R.id.group_dissolve).setOnClickListener(v->{
                List<Group> groups= new ArrayList<>();
                for (Group g: CurrentProject.Groups) {
                    if(g!=group)
                        groups.add(g);
                }
                GroupListProvider glp=new GroupListProvider(context,"Dissolve Group Into",groups);
                d.cancel();
                glp.groupClick=newGroup->{
                    for (Keybind kb:group.keybinds.toArray(new Keybind[0])) {
                        newGroup.AddKeybind(kb,false);
                    }
                    newGroup.currentAdapter.notifyDataSetChanged();
                    CurrentProject.Groups.remove(group);
                    CurrentProject.updateGroupIndexes();
                };
                glp.Show();
            });
            d.findViewById(R.id.group_move).setOnClickListener(v->{
                d.cancel();
                ArrowProvider ap=new ArrowProvider(context);
                ap.directionClicked=isUp -> {
                    int indx= CurrentProject.Groups.indexOf(group);
                    System.out.println(indx);
                    if(isUp){
                        if(indx>0) {
                            CurrentProject.Groups.remove(group);
                            CurrentProject.Groups.add(indx - 1, group);
                            CurrentProject.updateGroupIndexes();
                            notifyItemMoved(indx,indx-1);
                        }
                    }else{
                        if(indx< CurrentProject.Groups.size()-1){
                            CurrentProject.Groups.remove(group);
                            CurrentProject.Groups.add(indx + 1, group);
                            CurrentProject.updateGroupIndexes();
                            notifyItemMoved(indx,indx+1);
                        }
                    }
                };
                ap.Show();
            });
            d.show();

        });



    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public GroupViewHolder(View itemView){
            super(itemView);

        }
    }
}
