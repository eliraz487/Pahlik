package com.example.pahlik.helper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionBarPolicy;

import com.example.pahlik.R;

import java.util.ArrayList;
import java.util.List;public class MalfunctionTypeAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> StringList;

    public MalfunctionTypeAdapter(@NonNull Context context, int resource, ArrayList<String> StringList) {
        super(context, resource, StringList); // Pass the list to the ArrayAdapter's constructor
        this.context = context;
        this.StringList = StringList;
    }

    @Override
    public int getCount() {
        return StringList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.malfunction_type_list, parent, false);
        TextView txtName = view.findViewById(R.id.malfunction_type_text_view);
        txtName.setText(StringList.get(position));
        return view;
    }
}
