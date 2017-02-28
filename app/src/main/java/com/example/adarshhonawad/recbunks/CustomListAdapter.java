package com.example.adarshhonawad.recbunks;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.LayoutInflater.*;

/**
 * Created by Adarsh Honawad on 02-07-2016.
 */
public class CustomListAdapter extends CursorAdapter {

    Context context;
    LayoutInflater inflater;
    MyDBHandler dbHandler;
    Holder mainHolder;
    Resources resources;
    private Map<Integer,String> val;

    public CustomListAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
        this.context = context;
        resources = context.getResources();
        val = new HashMap<Integer, String>();
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return from(context).inflate(R.layout.custom_list_item,parent,false);
    }


    @Override
    public void bindView(View view, final Context context,Cursor cursor) {

        final TextView subjectName = (TextView) view.findViewById(R.id.subjectName);
        TextView percentDisplay = (TextView) view.findViewById(R.id.percentDisplay);
        TextView bunksDisplay = (TextView) view.findViewById(R.id.bunksDisplay);
        TextView id = (TextView) view.findViewById(R.id.recordId);
        ImageButton incButton = (ImageButton) view.findViewById(R.id.incrementButton);
        final Holder holder = new Holder();
        holder.bunks = bunksDisplay;
        holder.percent = percentDisplay;
        holder.listBackground = (RelativeLayout)view.findViewById(R.id.listBackground);
        holder.failNotify = (TextView)view.findViewById(R.id.failNotify);


        String subject = cursor.getString(cursor.getColumnIndex("subject"));
        holder.currBunks = cursor.getInt(cursor.getColumnIndex("bunks"));
        int maxBunks = cursor.getInt(cursor.getColumnIndex("maxbunks"));
        int idInt = cursor.getInt(cursor.getColumnIndex("_id"));

        Log.v("WHICH","BINDED" + subject);
        holder.id = idInt;
        holder.subject = subject;
        holder.maxBunks = maxBunks;

        if(maxBunks <= holder.currBunks ){
            holder.listBackground.setBackground(ContextCompat.getDrawable(context,R.drawable.ripple_max_background));
            holder.failNotify.setText(R.string.failNotify);
        }else {
            holder.listBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_background));
            holder.failNotify.setText("");
        }

        int percentage = ((holder.currBunks * 100) / maxBunks);
        String percentInput = "" + percentage + "% Bunked";
        String bunksInput = "";
        if (holder.currBunks < 10)
            bunksInput += "0" + holder.currBunks;
        else bunksInput += holder.currBunks;

        String recordId = "" + idInt;

        if (percentage >= 67) {
            bunksDisplay.setTextColor(ContextCompat.getColor(context, R.color.HighBunks));
            percentDisplay.setTextColor(ContextCompat.getColor(context, R.color.HighBunks));
        } else if (percentage >= 33) {
            bunksDisplay.setTextColor(ContextCompat.getColor(context, R.color.MediumBunks));
            percentDisplay.setTextColor(ContextCompat.getColor(context, R.color.MediumBunks));
        } else{
            bunksDisplay.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
            percentDisplay.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
        }

        String displaySubject = limitTitle(subject);

        id.setText(recordId);
        subjectName.setText(displaySubject);
        percentDisplay.setText(percentInput);
        bunksDisplay.setText(bunksInput);


        incButton.setTag(holder);

        //ON CLICK OF THE PLUS BUTTON
        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("WHICH","HERE AT LEAST");
                Holder value = (Holder)v.getTag();
                int bunks = Integer.parseInt(value.bunks.getText().toString());
                int maxBunks = value.maxBunks;

                if(bunks >= maxBunks) {
                    value.failNotify.setText(R.string.failNotify);
                    return;
                }

                bunks++;

                Log.v("WHICH","Reached here");
                dbHandler = new MyDBHandler(context,null,null,1);
                dbHandler.increaseBunks(value.id);
                String newBunks = "";
                value.currBunks++;
                if(bunks<10){
                    newBunks += "0" + bunks;
                }else newBunks += bunks;
                int percentage = (bunks*100)/value.maxBunks;
                String percent = "" + percentage + "% Bunked";


                if(percentage>=67){
                    value.bunks.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
                }else if(percentage>=33){
                    value.bunks.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
                }else {
                    value.bunks.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
                }

                Log.v("WHICH","I reached here");
                holder.percent.setText(percent);
                holder.bunks.setText(newBunks);
                MainActivity mainActivity = (MainActivity)context;
                mainActivity.cursorChange();


                if(maxBunks == bunks ){
                    value.listBackground.setBackground(ContextCompat.getDrawable(context,R.drawable.ripple_max_background));
                    value.failNotify.setText(R.string.failNotify);
                    return;
                }
            }
        });

        incButton.setFocusable(false);
        final android.app.FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
        final int recId = idInt;
        final String maximumBunks = "" + maxBunks;

        holder.recordId = recId;

        view.setTag(holder);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Holder value = (Holder)v.getTag();
                String subjectName = value.subject;
                String currBunks = value.bunks.getText().toString();
                MainActivity mainActivity = (MainActivity)context;
                mainActivity.editRecord(value.recordId,subjectName,currBunks,maximumBunks);
                Log.v("WHICH","Hello");
                return true;
            }
        });

        /*view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle args = new Bundle();
                args.putString("subjectName",sub);
                DialogSelectAction dialogSelectAction = new DialogSelectAction();
                dialogSelectAction.setArguments(args);
                dialogSelectAction.show(fragmentManager,"Modify or Delete");
                Log.v("WHICH","Hello");
                return true;
            }
        }); */
    }


    /*
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Holder holder;

        TextView id = (TextView) view.findViewById(R.id.recordId);

        if(view.getTag() == null){
            TextView percentDisplay = (TextView) view.findViewById(R.id.percentDisplay);
            TextView bunksDisplay = (TextView) view.findViewById(R.id.bunksDisplay);
            TextView failNotify = (TextView)view.findViewById(R.id.failNotify);
            holder = new Holder();
            holder.subject = cursor.getString(cursor.getColumnIndex("subject"));
            holder.currBunks = cursor.getInt(cursor.getColumnIndex("bunks"));
            holder.maxBunks = cursor.getInt(cursor.getColumnIndex("maxbunks"));
            holder.recordId = cursor.getInt(cursor.getColumnIndex("_id"));
            int percentage = (holder.currBunks*100)/holder.maxBunks;
            holder.subjectDisplay = (TextView) view.findViewById(R.id.subjectName);
            holder.percentage = percentage;
            holder.bunks = bunksDisplay;
            holder.percent = percentDisplay;
            holder.failNotify = failNotify;
            holder.listBackground = (RelativeLayout)view.findViewById(R.id.listBackground);
            view.setTag(holder);
        }else holder = (Holder)view.getTag();


        ImageButton incButton = (ImageButton) view.findViewById(R.id.incrementButton);

        String displaySubject = limitTitle(holder.subject);
        holder.subjectDisplay.setText(displaySubject);
        String currBunks = "";
        if(holder.currBunks<10){
            currBunks += "0" + holder.currBunks;
        }else currBunks += holder.currBunks;
        holder.bunks.setText(currBunks);
        String percentage = "" + holder.percentage;
        holder.percent.setText(percentage + "% Bunked");

        if(holder.percentage>=67){
            holder.bunks.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
            holder.percent.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
        }else if(holder.percentage>=33){
            holder.bunks.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
            holder.percent.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
        }else {
            holder.bunks.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
            holder.percent.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
        }

        if(holder.currBunks >= holder.maxBunks){
            holder.listBackground.setBackground(ContextCompat.getDrawable(context,R.drawable.ripple_max_background));
            holder.failNotify.setText(R.string.failNotify);
        }else{
            holder.listBackground.setBackground(ContextCompat.getDrawable(context,R.drawable.ripple_background));
            holder.failNotify.setText("");
        }

        incButton.setTag(holder);
        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Holder value = (Holder)v.getTag();

                if(value.currBunks == value.maxBunks)
                    return;

                value.currBunks++;
                value.percentage = (value.currBunks*100)/value.maxBunks;
                String newBunks = "";
                if(value.currBunks<10){
                    newBunks += "0" + value.currBunks;
                }else newBunks += value.currBunks;
                String percentage = "" + value.percentage + "% Bunked";
                MyDBHandler dbHandler = new MyDBHandler(context,null,null,1);
                dbHandler.increaseBunks(value.recordId);
                dbHandler.close();

                if(value.percentage>=67){
                    value.bunks.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context,R.color.HighBunks));
                }else if(value.percentage>=33){
                    value.bunks.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context,R.color.MediumBunks));
                }else {
                    value.bunks.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
                    value.percent.setTextColor(ContextCompat.getColor(context, R.color.LowBunks));
                }


                if(value.currBunks == value.maxBunks){
                    value.listBackground.setBackground(ContextCompat.getDrawable(context,R.drawable.ripple_max_background));
                    value.failNotify.setText(R.string.failNotify);
                }

                value.bunks.setText(newBunks);
                value.percent.setText(percentage);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Holder value = (Holder)v.getTag();
                String subjectName = value.subject;
                String currBunks = value.bunks.getText().toString();
                String maxBunks = "" + value.maxBunks;
                MainActivity mainActivity = (MainActivity)context;
                mainActivity.editRecord(value.recordId,subjectName,currBunks,maxBunks);
                Log.v("WHICH","Hello");
                return true;
            }
        });
        Log.v("WHICH","BINDVIEW");

    }
    */

    public class Holder{
        TextView subjectDisplay;
        TextView bunks;
        TextView percent;
        RelativeLayout listBackground;
        String subject;
        TextView failNotify;
        int maxBunks;
        int id;
        int currBunks;
        int recordId;
        int percentage;
    }

    private String limitTitle(String subjectName){
        String newSubjectName = "";
        int length = subjectName.length();
        int positionSpace = subjectName.indexOf(' ');
        if(length==(positionSpace+1)){
            positionSpace = 0;
        }

        if(length>12){
            if(positionSpace>0){
                newSubjectName += subjectName.substring(0,positionSpace) + "...";
            }else newSubjectName += subjectName.substring(0,12) + "...";
        }else newSubjectName += subjectName;

        return newSubjectName;
    }


}
