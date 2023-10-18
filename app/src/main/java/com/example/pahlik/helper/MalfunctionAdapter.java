package com.example.pahlik.helper;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.example.pahlik.R;
import com.example.pahlik.bean.Malfunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MalfunctionAdapter extends ArrayAdapter<Malfunction> implements Filterable {

    private ArrayList<Malfunction> originalData;
    private ArrayList<Malfunction> filteredData;
    private MalfunctionFilter filter;

    public MalfunctionAdapter(@NonNull Context context, ArrayList<Malfunction> dataArrayList) {
        super(context, R.layout.malfunction_item, dataArrayList);
        this.originalData = new ArrayList<>(dataArrayList);
        this.filteredData = new ArrayList<>(dataArrayList);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Nullable
    @Override
    public Malfunction getItem(int position) {
        return filteredData.get(position);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Malfunction Malfunction = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.malfunction_item, parent, false);
        }
        ImageView check = view.findViewById(R.id.check_image);
        TextView malfunction_type = view.findViewById(R.id.malfunction_type);
        TextView listTime = view.findViewById(R.id.listTime);
        TextView username = view.findViewById(R.id.username_item);
        TextView malfunction_system = view.findViewById(R.id.malfunction_system);
        TextView location = view.findViewById(R.id.location_item);
        malfunction_system.setText(Malfunction.getMalfunctionType().getSystem());
        malfunction_type.setText(Malfunction.getName_malfunction());
        location.setText(Malfunction.getLocation());
        if(Malfunction.isIs_solved()){
            check.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.ok));
        }
        else {
            check.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.not_ok));
        }
        username.setText(Malfunction.getUser().getFirstName() +" "+Malfunction.getUser().getLastName());
        listTime.setText(convertDateFormat(Malfunction.getLocalDateTime()));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDateFormat(String originalDateTimeString) {
        DateTimeFormatter originalFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            originalFormat = DateTimeFormatter.ofPattern(" mm:HH dd/MM/yy");

            DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

            try {
                LocalDateTime dateTime = LocalDateTime.parse(originalDateTimeString, originalFormat);
                return dateTime.format(targetFormat);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return originalDateTimeString;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MalfunctionFilter();
        }
        return filter;
    }

    private class MalfunctionFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Malfunction> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalData);
            } else {
                for (Malfunction malfunction : originalData) {
                    String fullName = malfunction.getUser().getFirstName() + " " + malfunction.getUser().getLastName();
                    if (fullName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(malfunction);
                    }
                }
            }
            results.count = filteredList.size();
            results.values = filteredList;
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Malfunction>) results.values;
            notifyDataSetChanged();
        }
    }
}