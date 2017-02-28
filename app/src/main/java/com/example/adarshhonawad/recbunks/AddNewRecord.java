package com.example.adarshhonawad.recbunks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AddNewRecord extends AppCompatActivity {

    private Toolbar toolbar;

    EditText newSubjectInput;
    EditText newMaxBunksInput;
    EditText newCurrBunksInput;
    TextView errorText;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_record);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        newSubjectInput = (EditText)findViewById(R.id.newSubjectInput);
        newMaxBunksInput  = (EditText)findViewById(R.id.newMaxBunksInput);
        newCurrBunksInput = (EditText)findViewById(R.id.newCurrBunksInput);
        errorText = (TextView)findViewById(R.id.errorText);
        dbHandler = new MyDBHandler(this,null,null,1);
    }

    //Add Button is Pressed
    public void addButtonPressed(View view){

        Records records;
        String subjectName = newSubjectInput.getText().toString();
        int flag = 1;
            if (subjectName.equals("")) {
                errorText.setText(R.string.errorSubjectName);
                flag = 0;
            } else if (newMaxBunksInput.getText().toString().trim().length()==0) {
                errorText.setText(R.string.errorMaxBunks);
                flag = 0;
            } else if (newCurrBunksInput.getText().toString().trim().length()==0) {
                errorText.setText(R.string.errorCurrBunks);
                flag = 0;
            } else {

                int maxBunks = Integer.parseInt(newMaxBunksInput.getText().toString());
                int currBunks = Integer.parseInt(newCurrBunksInput.getText().toString());

                if(currBunks>maxBunks){
                    errorText.setText("Already Bunked More Than Maximum!");
                    return;
                }else if(currBunks==maxBunks){
                    errorText.setText("Current Bunks is same as Maximum Bunks");
                    return;
                }
                records = new Records(subjectName, currBunks, maxBunks);
                dbHandler.addRecord(records);

                newSubjectInput.getText().clear();
                newMaxBunksInput.getText().clear();
                newCurrBunksInput.getText().clear();
                newSubjectInput.requestFocus();
                errorText.setText("");
            }
        if(flag==0)
            return;

    }

    //Finish Activity
    public void finishButtonPressed(View view){
        dbHandler.close();
        finish();
    }


}
