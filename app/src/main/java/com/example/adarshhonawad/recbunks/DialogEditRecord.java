package com.example.adarshhonawad.recbunks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Adarsh Honawad on 05-07-2016.
 */
public class DialogEditRecord extends DialogFragment {

    EditText subjectNameEdit;
    EditText currBunksEdit;
    EditText maxBunksEdit;
    TextView errorTextEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.v("WHICH","DIALOG CREATED");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_record_dialog,null);
        builder.setView(dialogView);

        final int recordId = getArguments().getInt("recordId");
        final String subjectName = getArguments().getString("subjectName");
        final String currBunks = getArguments().getString("currBunks");
        final String maxBunks = getArguments().getString("maxBunks");

        subjectNameEdit = (EditText)dialogView.findViewById(R.id.editSubjectName);
        currBunksEdit = (EditText)dialogView.findViewById(R.id.editCurrBunks);
        maxBunksEdit = (EditText)dialogView.findViewById(R.id.editMaxBunks);
        errorTextEdit = (TextView)dialogView.findViewById(R.id.errorTextEdit);

        subjectNameEdit.setText(subjectName);
        currBunksEdit.setText(currBunks);
        maxBunksEdit.setText(maxBunks);
        subjectNameEdit.requestFocus();

        Button saveButton = (Button)dialogView.findViewById(R.id.saveButton);
        Button discardButton = (Button)dialogView.findViewById(R.id.discardButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSubjectName = subjectNameEdit.getText().toString();


                //Check if current bunks > max bunks
                String newCurrBunks = "";
                String newMaxBunks = "";
                int currBunks = 0;
                int maxBunks = 1;

                int flag = 1;
                if(newSubjectName.equals("")){
                    errorTextEdit.setText("Don't Leave Subject Name Empty");
                    flag = 0;
                }else if(currBunksEdit.getText().toString().trim().length() == 0 ){
                    errorTextEdit.setText("You forgot to enter your current bunks");
                    flag = 0;
                }else if(maxBunksEdit.getText().toString().trim().length() == 0){
                    errorTextEdit.setText("You forgot to enter your maximum bunks");
                    flag = 0;
                }else{
                    newCurrBunks = currBunksEdit.getText().toString();
                    newMaxBunks = maxBunksEdit.getText().toString();
                    currBunks = Integer.parseInt(newCurrBunks);
                    maxBunks = Integer.parseInt(newMaxBunks);
                }

                if(currBunks>maxBunks){
                    errorTextEdit.setText("Already Bunked More Than Maximum");
                    flag = 0;
                }else if(currBunks==maxBunks){
                    errorTextEdit.setText("Current Bunks Equal to Maximum Bunks");
                }

                if(flag == 0)
                    return;

                ;

                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.changeRecord(recordId,newSubjectName,newCurrBunks,newMaxBunks);
                withChangesDismiss();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void withChangesDismiss(){
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.updateList();
        dismiss();
    }

}
