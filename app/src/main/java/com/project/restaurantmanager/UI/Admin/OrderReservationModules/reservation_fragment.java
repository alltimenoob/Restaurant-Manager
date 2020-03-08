package com.project.restaurantmanager.UI.Admin.OrderReservationModules;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.Reservation;
import com.project.restaurantmanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.project.restaurantmanager.Controller.DatabaseHandler.admin_resLink;

public class reservation_fragment extends Fragment {
    View view;
    List<Reservation> reservations = new ArrayList<>();
    String[] array = {"Older Reservations", "Today's Reservations", "Newer Reservation"};
    LinearLayout layoutOuter;
    Spinner select_reservation;

    protected List<String> tno = new ArrayList<>();
    protected List<String> rdate = new ArrayList<>();
    protected List<String> starttime = new ArrayList<>();
    protected List<String> endtime = new ArrayList<>();
    protected List<String> guests = new ArrayList<>();
    protected List<String> deposit = new ArrayList<>();
    protected List<Integer> index = new ArrayList<Integer>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_allreservtion_fragment, container, false);

        select_reservation = view.findViewById(R.id.admin_reservation_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_reservation.setAdapter(adapter);

        final ListView listView = view.findViewById(R.id.admin_reservation_linearlayout);

        DatabaseHandler handler = new DatabaseHandler(admin_resLink, getContext()) {
            @Override
            public void writeCode(String response) throws Exception {

                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    tno.add(object.getString("tno"));
                    rdate.add(object.getString("rdate"));
                    starttime.add(object.getString("starttime"));
                    endtime.add(object.getString("endtime"));
                    guests.add(object.getString("guest"));
                    deposit.add(object.getString("deposit"));
                }
                select_reservation.setSelection(1);
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                select_reservation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        index.clear();
                        for (int i = 0; i < rdate.size(); i++) {
                            try {
                                Date strDate = sdf.parse(rdate.get(i));
                                if(new Date().after(strDate)) {
                                    if(position==0&&new Date().getDate()!=strDate.getDate()){
                                        index.add(i);
                                        CustomAdapter customAdapter = new CustomAdapter();
                                        listView.setAdapter(customAdapter);
                                    }
                                    else if(new Date().getDate()==strDate.getDate()&&position==1) {
                                        index.add(i);
                                        CustomAdapter customAdapter = new CustomAdapter();
                                        listView.setAdapter(customAdapter);
                                    }
                                }
                                else if(new Date().before(strDate)&&position==2)
                                {
                                    index.add(i);
                                    CustomAdapter customAdapter = new CustomAdapter();
                                    listView.setAdapter(customAdapter);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public Map<String, String> params() {
                return null;
            }
        };
        handler.execute();


        return view;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return index.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_yourreservation_layout, parent, false);

            TextView resdateV = convertView.findViewById(R.id.listview_yourreservation_date);
            TextView amountV = convertView.findViewById(R.id.listview_yourreservation_amount);
            TextView starttimeV = convertView.findViewById(R.id.listview_yourreservation_starttime);
            TextView endtimeV = convertView.findViewById(R.id.listview_yourreservation_endtime);
            TextView tabnoV = convertView.findViewById(R.id.listview_yourreservation_tno);
            TextView guestV = convertView.findViewById(R.id.listview_yourreservation_guests);

            resdateV.setText("Reservation Date: " + rdate.get(index.get(position)));
            amountV.setText("Amount: ₹ " + (int) Double.parseDouble(deposit.get(index.get(position))));
            starttimeV.setText("Start Time: " + starttime.get(index.get(position)));
            endtimeV.setText("End Time: " + endtime.get(index.get(position)));
            tabnoV.setText("Table No: " + tno.get(index.get(position)));
            guestV.setText("Guests: " + guests.get(index.get(position)));

            return convertView;
        }
    }
}
