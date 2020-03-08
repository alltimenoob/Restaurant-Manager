package com.project.restaurantmanager.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.FoodItems;
import com.project.restaurantmanager.R;
import com.project.restaurantmanager.UI.Customer.RestaurantListFragment;
import com.project.restaurantmanager.UI.Customer.FoodItemListFragment;
import com.project.restaurantmanager.UI.Customer.FoodReservationFragment;
import com.project.restaurantmanager.UI.Customer.TableFragmentModules.Recyclerviewadapter_Table;
import com.project.restaurantmanager.UI.Customer.AccountFragment;
import com.project.restaurantmanager.UI.Customer.CartFragment;
import com.project.restaurantmanager.UI.Customer.ReservationFragment;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.project.restaurantmanager.UI.Customer.CartFragment.map;
import static com.project.restaurantmanager.UI.Customer.CartFragment.mapDB;
import static com.project.restaurantmanager.UI.Customer.ReservationFragment.GuestsDB;
import static com.project.restaurantmanager.UI.Customer.ReservationFragment.dateDB;


public class CustomerActivity extends AppCompatActivity  implements PaymentResultListener {
    Fragment fragment;
    public static BottomNavigationView bottomNavigationView;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        /*For Showing Internet Error*/
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        }

        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.customer_container,new RestaurantListFragment(),null).commit();

        bottomNavigationView = findViewById(R.id.baseactivity_bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
                switch (menuItem.getItemId()){
                    case R.id.Delivery:
                        fragment = new RestaurantListFragment();
                        break;
                    case R.id.Cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.Account:
                        fragment = new AccountFragment();
                        break;
                    default:
                }
                fragmentManager.beginTransaction().replace(R.id.customer_container,fragment,null).commit();
                return true;
            }
        });
    }
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        switch (bottomNavigationView.getSelectedItemId()) {
            case R.id.Delivery:
                if (count == 0) {
                    finishAffinity();
                } else {
                    bottomNavigationView.setSelectedItemId(R.id.Delivery);
                    getSupportFragmentManager().popBackStack("foodreservation",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                break;
            case R.id.Cart:
                bottomNavigationView.setSelectedItemId(R.id.Delivery);
                break;
            case R.id.Account:
                if (count == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.Cart);
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                break;
            default:
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        if(MainActivity.paymentMethod!=null&&MainActivity.paymentMethod.equals("res")) {
            DatabaseHandler databaseHandlerForRes = new DatabaseHandler(DatabaseHandler.afterpayment_link, getApplicationContext()) {
                @Override
                public void writeCode(String response) throws JSONException {
                    Log.d("ffff", "writeCode: "+response);
                    Toast.makeText(getApplicationContext(), response.trim(), Toast.LENGTH_SHORT).show();
                    GuestsDB = null;
                    dateDB = null;
                    Recyclerviewadapter_Table.endTimeDB=null;
                    Recyclerviewadapter_Table.startTimeDB=null;
                    ReservationFragment.tableNoDB = null;
                }

                @Override
                public Map<String, String> params() {
                    Map<String, String> map = new HashMap<>();
                    map.put("StartTime", Recyclerviewadapter_Table.startTimeDB);
                    map.put("EndTime", Recyclerviewadapter_Table.endTimeDB);
                    map.put("Date", dateDB);
                    map.put("Guest", GuestsDB);
                    map.put("Email", MainActivity.sharedPreferences.getEmail());
                    map.put("Tno", ReservationFragment.tableNoDB);
                    map.put("Rid", FoodReservationFragment.mSelectedRid+"");
                    return map;
                }
            };
            databaseHandlerForRes.execute();

        }else if(MainActivity.paymentMethod!=null&&MainActivity.paymentMethod.equals("cart")) {
            FoodItemListFragment.i=0;
            final DatabaseHandler databaseHandlerForCart = new DatabaseHandler(DatabaseHandler.placeorder_link, getApplicationContext()) {
                @Override
                public void writeCode(String response) throws JSONException {
                    Toast.makeText(getApplicationContext(), response.trim(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public Map<String, String> params() {
                    return mapDB;
                }
            };

            databaseHandlerForCart.execute();

            sendNotification();
            CartFragment.quantity.clear();
            FoodItemListFragment.nameForCart.clear();
            FoodItemListFragment.priceForCart.clear();
            map.clear();
            FoodItemListFragment.buttonflag.clear();

        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"" + s,Toast.LENGTH_SHORT).show();
        dateDB=null;
        FoodItemListFragment.nameForCart.clear();
        FoodItemListFragment.priceForCart.clear();
        FoodItemListFragment.buttonflag.clear();
        CartFragment.quantity.clear();
        map.clear();
        FoodItemListFragment.i=0;
        ReservationFragment.tableNoDB=null;
    }

    private void sendNotification()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("admin");
        reference.child(mapDB.get("Rid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                final String token = dataSnapshot.getValue()+"";

                Log.d("ffff", "onDataChange: "+token);

                DatabaseHandler handler = new DatabaseHandler("http://34.93.41.224/sendNoti.php",getApplicationContext()) {
                    @Override
                    public void writeCode(String response) throws JSONException, InterruptedException, Exception { }

                    @Override
                    public Map<String, String> params() {
                        Map<String,String> map = new HashMap<>();
                        map.put("message","You got new order for Home delievery...");
                        map.put("token",token);
                        return map;
                    }
                };
                handler.execute();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
