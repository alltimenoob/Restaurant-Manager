package com.project.restaurantmanager.UI.Customer.AccountFragmentModules;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.Orders;
import com.project.restaurantmanager.Data.Reservation;
import com.project.restaurantmanager.Model.MainActivity;
import com.project.restaurantmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class your_reservation extends Fragment {
    List<Reservation> reservations = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_accountyourreservation_fragment,container,false);

        final ListView listView = view.findViewById(R.id.cust_acc_yourreservation_listView);

        DatabaseHandler handler = new DatabaseHandler(DatabaseHandler.yourreservation_link,getContext()) {
            @Override
            public void writeCode(String response) throws JSONException {
                JSONArray array = new JSONArray(response);
                for(int i=0;i<array.length();i++) {
                    JSONObject object = array.getJSONObject(i);
                    reservations.add(new Reservation(object.getString("rdate"),object.getString("starttime"),
                            object.getString("endtime"),object.getString("guest")
                            ,object.getString("deposit"),object.getString("tno")));
                }
                CustomAdapter arrayAdapter = new CustomAdapter();
                listView.setAdapter(arrayAdapter);
            }
            @Override
            public Map<String, String> params() {
                Map<String,String> mapDB= new HashMap<>();
                mapDB.put("email", MainActivity.sharedPreferences.getEmail());
                return mapDB;
            }
        };
        handler.execute();



        return view;
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return reservations.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = getLayoutInflater().inflate(R.layout.listview_yourreservation_layout,parent,false);
            TextView rdate = view.findViewById(R.id.listview_yourreservation_date);
            TextView deposit = view.findViewById(R.id.listview_yourreservation_amount);
            TextView starttime = view.findViewById(R.id.listview_yourreservation_starttime);
            TextView endtime = view.findViewById(R.id.listview_yourreservation_endtime);
            TextView guest = view.findViewById(R.id.listview_yourreservation_guests);
            TextView tno = view.findViewById(R.id.listview_yourreservation_tno);

            rdate.setText("Date : "+reservations.get(position).getRdate());
            deposit.setText("Deposit : "+"₹ "+(int)Double.parseDouble(reservations.get(position).getDeposit()));
            starttime.setText("Start Time : "+reservations.get(position).getStarttime());
            endtime.setText("End Time : "+reservations.get(position).getEndtime());
            guest.setText("Guests : "+reservations.get(position).getGuests());
            tno.setText("Table No : "+reservations.get(position).getTno());

            return view;
        }
    }
}

