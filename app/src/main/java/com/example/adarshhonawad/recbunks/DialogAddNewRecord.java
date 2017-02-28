package com.example.adarshhonawad.recbunks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Adarsh Honawad on 03-07-2016.
 */
public class DialogAddNewRecord extends DialogFragment {

    EditText newSubjectInput;
    EditText newMaxBunksInput;
    EditText newCurrBunksInput;
    TextView idText;
    TextView errorText;
    MyDBHandler dbHandler;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_record_dialog,null);

        builder.setView(dialogView);

        newSubjectInput = (EditText)dialogView.findViewById(R.id.dialogSubjectNameInput);
        newMaxBunksInput = (EditText)dialogView.findViewById(R.id.dialogMaxBunksInput);
        newCurrBunksInput = (EditText)dialogView.findViewById(R.id.dialogCurrBunksInput);
        errorText = (TextView)dialogView.findViewById(R.id.errorText);
        dbHandler = new MyDBHandler(getActivity(),null,null,1);
        Button multipleButton = (Button)dialogView.findViewById(R.id.addMultipleButton);

        final Button addButton = (Button)dialogView.findViewById(R.id.addButton);
        Button discardButton = (Button)dialogView.findViewById(R.id.discardButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        multipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddNewRecord.class);
                getDialog().dismiss();
                startActivity(intent);
            }
        });

        return builder.create();
    }

    public void addRecord() {
        Records records;
        String subjectName = newSubjectInput.getText().toString();
        int flag = 1;
        if (subjectName.equals("")) {
            errorText.setText(R.string.errorSubjectName);
            flag = 0;
        } else if (newMaxBunksInput.getText().toString().trim().length() == 0) {
            errorText.setText(R.string.errorMaxBunks);
            flag = 0;
        } else if (newCurrBunksInput.getText().toString().trim().length() == 0) {
            errorText.setText(R.string.errorCurrBunks);
            flag = 0;
        } else {

            int maxBunks = Integer.parseInt(newMaxBunksInput.getText().toString());
            int currBunks = Integer.parseInt(newCurrBunksInput.getText().toString());
            if(currBunks>=maxBunks){
                errorText.setText("Bunked more than Maximum Already?");
                flag = 0;
            }else {
                records = new Records(subjectName, currBunks, maxBunks);
                dbHandler.addRecord(records);
            }
        }

        if(flag==0)
            return;
        else dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.updateList();
    }
}
