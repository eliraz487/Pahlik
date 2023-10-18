package com.example.pahlik.helper;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.pahlik.R;
import com.example.pahlik.bean.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends ArrayAdapter<User> implements Filterable {

    private ArrayList<User> originalData;
    private ArrayList<User> filteredData;
    private UserFilter filter;

    public UserAdapter(@NonNull Context context, ArrayList<User> dataArrayList) {
        super(context, R.layout.list_item_user, dataArrayList);
        this.originalData = new ArrayList<>(dataArrayList);
        this.filteredData = new ArrayList<>(dataArrayList);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return filteredData.get(position);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_user, parent, false);
        }
        TextView rank_and_name = view.findViewById(R.id.rank_and_name);
        TextView job_item = view.findViewById(R.id.job_item);
        TextView phone_number_item = view.findViewById(R.id.phone_number_item);
        rank_and_name.setText(user.getRank()+" "+ user.getFirstName() +" "+ user.getLastName());
        phone_number_item.setText(user.getPhoneNumber());
        job_item.setText(user.getJobTitle());
        
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDateFormat(String originalDateTimeString) {
        DateTimeFormatter originalFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
            filter = new UserFilter();
        }
        return filter;
    }

    private class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalData);
            } else {
                for (User User : originalData) {
                    String fullName = User.getFirstName() + " " + User.getLastName();
                    if (fullName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(User);
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
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }
}