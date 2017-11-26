package com.kaidos85.simplepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kaidos85.simplepad.dal.RepositoryNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidos on 07.06.2016.
 */
public class SortDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG="SimplePad";
    View view = null;
    List<String> items;
    ListView lv;

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanse){

        view = getActivity().getLayoutInflater().inflate(R.layout.sort_dialog, null);
        lv = (ListView)view.findViewById(R.id.sortListView);
        items = new ArrayList<String>();
        items.add("By Date");
        items.add("By Title");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(itemClick);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Sort")
                .setView(view)
                .create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which){

    }

    @Override
    public void onDismiss(DialogInterface unused)
    {
        super.onDismiss(unused);
    }

    ListView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selected = (String)parent.getItemAtPosition(position);
            Log.d(LOG_TAG, selected);
            if(selected.equals("By Date"))
                ((MainActivity)getActivity()).SetSort(RepositoryNote.orderByDate);
            if(selected.equals("By Title"))
                ((MainActivity)getActivity()).SetSort(RepositoryNote.orderByTitle);
            dismiss();
        }
    };

}
