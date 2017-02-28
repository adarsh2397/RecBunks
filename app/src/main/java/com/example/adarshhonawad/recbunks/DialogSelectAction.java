package com.example.adarshhonawad.recbunks;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Adarsh Honawad on 04-07-2016.
 */
public class DialogSelectAction extends DialogFragment {


    MyDBHandler dbHandler;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] items = {"Modify","Delete"};

        final int recordId = getArguments().getInt("recordId");
        final String subjectName = getArguments().getString("subjectName");
        final String currBunks = getArguments().getString("currBunks");
        final String maxBunks = getArguments().getString("maxBunks");
        dbHandler = new MyDBHandler(getActivity(),null,null,1);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select An Action")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1:
                                    dbHandler.deleteRecord(recordId);
                                dismiss();
                                break;
                            case 0:
                                MainActivity mainActivity = (MainActivity)getActivity();
                                mainActivity.changeRecordDialog(recordId,subjectName,currBunks,maxBunks);
                                dismiss();
                                break;
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.updateList();
    }

    public void editRecord(){
        /*MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.editRecord();*/
    }
}
