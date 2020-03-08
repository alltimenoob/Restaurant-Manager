package com.project.restaurantmanager.UI.Customer.AccountFragmentModules;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.Orders;
import com.project.restaurantmanager.Model.MainActivity;
import com.project.restaurantmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devs.mulham.horizontalcalendar.utils.HorizontalCalendarPredicate;

public class your_orders extends Fragment {
    List<Orders> orders =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_accountyourorders_fragment,container,false);

        final ListView listView = view.findViewById(R.id.cust_acc_yourorder_listView);
        listView.setDividerHeight(20);
        DatabaseHandler handler = new DatabaseHandler(DatabaseHandler.yourorders_link,getContext()) {
            @Override
            public void writeCode(String response) throws JSONException {
                JSONArray array = new JSONArray(response);
                for(int i=0;i<array.length();i++) {

                    JSONObject object = array.getJSONObject(i);
                    JSONArray array1 = object.getJSONArray("items");

                    String[] item = new String[array1.length()];
                    String[] qty = new String[array1.length()];

                    for(int k=0;k<array1.length();k++) {
                        JSONObject items = array1.getJSONObject(k);
                        item[k] = items.getString("item");
                        qty[k] = items.getString("qty");
                    }
                    orders.add(new Orders(object.getString("date"),object.getString("totalamount"),item,qty));
               }
                CustomAdapter arrayAdapter = new CustomAdapter();
                listView.setAdapter(arrayAdapter);
            }
            @Override
            public Map<String, String> params() {
                Map<String,String> mapDB= new HashMap<>();
                mapDB.put("username", MainActivity.sharedPreferences.getUsername());
                return mapDB;
            }
        };
        handler.execute();
        return view;
    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orders.size();
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
            @SuppressLint("ViewHolder") View view = getLayoutInflater().inflate(R.layout.listview_yourorders_layout,parent,false);
            TextView amount = view.findViewById(R.id.listview_yourorders_amount);
            TextView date = view.findViewById(R.id.listview_yourorders_date);
            TextView items = view.findViewById(R.id.listview_yourorders_items);
            items.setText("Items : ");

            amount.setText("₹ "+(int)Double.parseDouble(orders.get(position).getAmount()));
            date.setText("Date : "+orders.get(position).getDate());

            String[] strings = orders.get(position).getItems();
            String[] strings1 = orders.get(position).getQuantity();

              for(int i=0;i<strings.length;i++)
              {
                  if(i==strings.length-1)
                  {
                      items.append(strings1[i]+"x "+strings[i]+" . ");
                  }
                  else {
                      items.append(strings1[i]+"x "+strings[i]+" , ");
                  }
              }

            return view;
        }
    }
}
