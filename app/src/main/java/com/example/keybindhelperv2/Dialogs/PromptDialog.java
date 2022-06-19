package com.example.keybindhelperv2.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.keybindhelperv2.R;


public class PromptDialog {
    private Dialog dialog;
    public Validator validation;
    public ConfirmedEvent confirmedEvent;
    public Boolean cannotBeEmpty=true;
    private Button cancelBtn,doneBtn;
    private EditText inputBox;
    private TextView errorView;
    public PromptDialog(Context context, String title, String content, String currentValue){
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.promptdialog);
        cancelBtn=dialog.findViewById(R.id.promptCancelButton);
        doneBtn=dialog.findViewById(R.id.PromptOkButton);
        TextView titleView=dialog.findViewById(R.id.promptText);
        TextView contentView=dialog.findViewById(R.id.PromptContent);
        inputBox=dialog.findViewById(R.id.PromptInputBox);

        errorView=dialog.findViewById(R.id.PromptErrorText);
        titleView.setText(title);
        contentView.setText(content);
        inputBox.setText(currentValue);
        inputBox.setSelectAllOnFocus(true);
        inputBox.requestFocus();




        if(title.length()==0)
            titleView.setVisibility(View.GONE);
        if(content.length()==0)
            contentView.setVisibility(View.GONE);
    }
    public void ShowDialog(){
        dialog.show();

        inputBox.requestFocus();
        InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
       /* dialog.setOnCancelListener(v-> imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0));
        dialog.setOnDismissListener(v-> imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0));
        dialog.setOnDismissListener(v->{
            System.err.println("FUCK ME IN THE ASS");
        });*/

        doneBtn.setOnClickListener(v -> {
            String text=inputBox.getText().toString();
            if(validation!=null){
                ValidatorResponse vr=validation.Validate(text);
                if(cannotBeEmpty && text.length()==0){
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText("Input Cannot Be Empty.");
                    return;
                }
                if(vr.isValid){
                    imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0);
                    dialog.cancel();
                    if(confirmedEvent!=null)
                        confirmedEvent.onConfirmed(text);
                }else{
                    errorView.setText(vr.invalidMessage);
                }
            }
        });
        cancelBtn.setOnClickListener(v->{
            imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0);
            dialog.cancel();
        });
    }
    public interface Validator{
      ValidatorResponse Validate(String text);
    }
    public interface ConfirmedEvent{
        void onConfirmed(String text);
    }


}
