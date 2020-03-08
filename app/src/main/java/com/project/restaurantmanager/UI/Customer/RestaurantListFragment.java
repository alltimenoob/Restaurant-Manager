package com.project.restaurantmanager.UI.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Controller.RestuarantSQLite;
import com.project.restaurantmanager.Data.FoodItems;
import com.project.restaurantmanager.Data.Restaurant;
import com.project.restaurantmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class RestaurantListFragment extends Fragment {

    List<Restaurant> restaurantList = new ArrayList<>();
    CustomAdapter customAdapter ;
    RestuarantSQLite db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        db = new RestuarantSQLite(getContext());

        final ListView mRestaurantListView = view.findViewById(R.id.restaurantListview);
        mRestaurantListView.setDividerHeight(20);

        customAdapter = new CustomAdapter(getContext(),getFragmentManager());

        restaurantList = db.getRestaurantData();

        if(restaurantList.size()>0)
        {
            customAdapter.filter("");
            mRestaurantListView.setAdapter(customAdapter);
        }

        SearchView searchView = view.findViewById(R.id.restaurantSearch);
        searchView.setQueryHint("Search Restaurant");
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.filter(newText);
                return true;
            }
        });


        DatabaseHandler databaseHandler = new DatabaseHandler("http://34.93.41.224/RestaurantListCustomer.php",getContext()) {
            @Override
            public void writeCode(String response) throws Exception {

                JSONArray jsonArray = new JSONArray(response);

                SQLiteDatabase database = db.getWritableDatabase();
                database.execSQL("DELETE FROM restaurants");

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    db.insertRestaurantData(jsonObject.getInt("rid"),
                            jsonObject.getString("name"),
                            jsonObject.getString("city"),
                            jsonObject.getString("address"),
                            jsonObject.getInt("contact"),
                            jsonObject.getString("image"));
                }

                restaurantList = db.getRestaurantData();
                customAdapter.filter("");
                mRestaurantListView.setAdapter(customAdapter);
            }
            @Override
            public Map<String, String> params() {
                return null;
            }
        };
        databaseHandler.execute();



        return view;
    }
    public class CustomAdapter extends BaseAdapter
    {
        Context mContext;
        FragmentManager mFragmentManager;
        List<Restaurant> list = new ArrayList<>();

        public CustomAdapter(Context mContext,FragmentManager mFragmentManager) {
            this.mContext = mContext;
            this.mFragmentManager = mFragmentManager;
        }

        public void filter(String charText){
            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length()==0){
                list.addAll(restaurantList);
            }
            else {
                for (Restaurant model : restaurantList){
                    if (model.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)){
                        list.add(model);
                    }
                }
            }
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_restaurants,parent,false);
            convertView.setId(Integer.parseInt(list.get(position).getRid()));

            TextView name = convertView.findViewById(R.id.restaurantListView_name);
            TextView city = convertView.findViewById(R.id.restaurantListview_city);
            ImageView image = convertView.findViewById(R.id.restaurantListview_image);

            name.setText(list.get(position).getName());
            city.setText(list.get(position).getCity());


            String imagebase64 =  list.get(position).getImage();
            byte[] imagedecoded = Base64.decode(imagebase64,Base64.DEFAULT);
            Bitmap decodedimage = BitmapFactory.decodeByteArray(imagedecoded,0,imagedecoded.length);

            image.setImageBitmap(decodedimage);

            convertView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(final View v) {
                    final int id = v.getId();
                    Boolean flag = false;

                    for(int i = 0; i< FoodItemListFragment.nameForCart.size(); i++)
                    {
                        if(FoodItemListFragment.nameForCart.get(i)!=null)
                        {
                            flag=true;
                        }
                    }

                    if(id == FoodReservationFragment.mSelectedRid )
                    {
                        mFragmentManager.beginTransaction().addToBackStack("foodreservation").
                                replace(R.id.customer_container,new FoodReservationFragment(),null).commit();
                    }
                    else if(FoodReservationFragment.mSelectedRid==0)
                    {
                        FoodReservationFragment.mSelectedRid=v.getId();
                        mFragmentManager.beginTransaction().addToBackStack("foodreservation").
                                replace(R.id.customer_container,new FoodReservationFragment(),null).commit();
                    }
                    else if(!flag)
                    {
                        FoodReservationFragment.mSelectedRid=v.getId();
                        mFragmentManager.beginTransaction().addToBackStack("foodreservation").
                                replace(R.id.customer_container,new FoodReservationFragment(),null).commit();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Warning");
                        builder.setMessage("You are trying to select diffrent restaurant,this will clear your cart");
                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FoodItemListFragment.nameForCart.clear();
                                FoodItemListFragment.priceForCart.clear();
                                FoodItemListFragment.buttonflag.clear();
                                FoodItemListFragment.i=0;

                                FoodReservationFragment.mSelectedRid=id;
                                mFragmentManager.beginTransaction().addToBackStack("foodreservation").
                                        replace(R.id.customer_container,new FoodReservationFragment(),null).commit();
                            }
                        });
                        builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });

            return convertView;
        }
    }
}
