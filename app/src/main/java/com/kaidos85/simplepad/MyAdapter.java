package com.kaidos85.simplepad;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.SequenceInputStream;
import java.util.ArrayList;

/**
 * Created by Aidos on 02.06.2016.
 */
public class MyAdapter extends ArrayAdapter<Item>
{
    private final Context context;
    private final ArrayList<Item> items;
    private final LayoutInflater inf;

    public MyAdapter(Context _context, ArrayList<Item> _items)
    {
        super(_context, 0, _items);
        context = _context;
        items = _items;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        Item item = items.get(position);
        if(item != null){
            if(item.isSection()) {
                SectionItem si = (SectionItem)item;
                v = inf.inflate(R.layout.sectionitem, null);
                TextView titleView = (TextView) v.findViewById(R.id.titletext);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                titleView.setText(si.Title);
            }
            else{
                NoteItem ni = (NoteItem)item;
                v = inf.inflate(R.layout.listitem_1, null);
                TextView title = (TextView) v.findViewById(R.id.lvItemTitle);
                TextView date = (TextView) v.findViewById(R.id.lvItemDate);

                title.setText(ni.Title);
                date.setText(ni.NoteDate);
            }
        }
        return v;
    }
}
