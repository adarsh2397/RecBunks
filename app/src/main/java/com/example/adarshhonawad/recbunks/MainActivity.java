package com.example.adarshhonawad.recbunks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import  android.support.v7.widget.ShareActionProvider;

import java.util.ArrayList;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView titleText;
    static CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        titleText = (TextView)findViewById(R.id.titleText);

        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        Cursor todoCursor = dbHandler.getCursor();

        ListView listView = (ListView)findViewById(R.id.recordsListView);

        adapter = new CustomListAdapter(this,todoCursor,0);

        listView.setAdapter(adapter);
        titleText.setText(R.string.titleNoSubjects);
        listView.setEmptyView(titleText);

    }

    @Override
    protected void onResume() {

        super.onResume();
        updateList();
        Log.v("WHICH","I RESUMED");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_addNewRecord:
                DialogAddNewRecord dialogAddNewRecord = new DialogAddNewRecord();
                dialogAddNewRecord.show(this.getFragmentManager(),"DialogAddNewRecord");
                break;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,getDatabaseToString());
                startActivity(Intent.createChooser(shareIntent,"Send Details To"));
                break;
            case R.id.action_delete:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void editRecord(int recordId,String subjectName,String currBunks,String maxBunks){
        Bundle args = new Bundle();
        args.putInt("recordId",recordId);
        args.putString("subjectName",subjectName);
        args.putString("currBunks",currBunks);
        args.putString("maxBunks",maxBunks);
        DialogSelectAction dialogSelectAction = new DialogSelectAction();
        dialogSelectAction.setArguments(args);
        dialogSelectAction.show(getFragmentManager(),"Modify or Delete");
    }

    public void updateList(){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        Cursor todoCursor = dbHandler.getCursor();

        ListView listView = (ListView)findViewById(R.id.recordsListView);

        adapter.changeCursor(todoCursor);

        listView.setAdapter(adapter);
        listView.setEmptyView(titleText);

        dbHandler.close();
    }

    public void changeRecord(int recordId,String subjectName,String currBunks,String maxBunks){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        int currBunksInt = Integer.parseInt(currBunks);
        int maxBunksInt = Integer.parseInt(maxBunks);
        dbHandler.updateRecord(recordId,subjectName,currBunksInt,maxBunksInt);
        dbHandler.close();
    }

    public void changeRecordDialog(int recordId,String subjectName,String currBunks,String maxBunks){
        Bundle args = new Bundle();
        args.putInt("recordId",recordId);
        args.putString("subjectName",subjectName);
        args.putString("currBunks",currBunks);
        args.putString("maxBunks",maxBunks);
        DialogEditRecord dialogEditRecord = new DialogEditRecord();
        dialogEditRecord.setArguments(args);
        dialogEditRecord.show(getFragmentManager(),"Edit");
    }

    public String getDatabaseToString() {
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        Cursor cursor = dbHandler.getCursor();
        cursor.moveToFirst();

        String shareText = "";
        int i = 0;

        while(!cursor.isAfterLast()){
            String subject = cursor.getString(cursor.getColumnIndex("subject"));
            int bunks = cursor.getInt(cursor.getColumnIndex("bunks"));
            int maxBunks = cursor.getInt(cursor.getColumnIndex("maxbunks"));
            int percentage = (bunks*100)/maxBunks;
            i++;

            shareText += "" + i + ")" + " Subject Name: " + subject
                    + "\nBunks: " + bunks + "/" + maxBunks + " (" + percentage + "%)\n\n";
            cursor.moveToNext();
        }

        dbHandler.close();

        return shareText;
    }

    public void cursorChange(){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        Cursor todoCursor = dbHandler.getCursor();

        adapter.changeCursor(todoCursor);
    }

    public void deleteAll(){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        dbHandler.deleteEverything();

        dbHandler.close();

        updateList();
    }

}
