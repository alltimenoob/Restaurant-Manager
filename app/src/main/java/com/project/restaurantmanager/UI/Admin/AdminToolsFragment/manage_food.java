package com.project.restaurantmanager.UI.Admin.AdminToolsFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.FoodItems;
import com.project.restaurantmanager.Model.AdminActivity;
import com.project.restaurantmanager.Model.EmployeeActivity;
import com.project.restaurantmanager.Model.MainActivity;
import com.project.restaurantmanager.R;
import com.project.restaurantmanager.UI.Customer.FoodReservationFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.restaurantmanager.UI.Employee.dashboard_fragment.tno;

public class manage_food extends Fragment {
    private View view;
    private TextView price,name;
    private ImageView image,addImage;
    private List<FoodItems> items;
    private LinearLayout layoutOuter;
    private RelativeLayout layoutAdd;
    private Button delete;

    public static Bitmap bitmapDB;
    public static String nameDB,priceDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_manage_food,container,false);

        layoutOuter = view.findViewById(R.id.admin_food_verticalLinearLayout);
        layoutAdd = view.findViewById(R.id.admin_food_add_food_layout);

        addImage = view.findViewById(R.id.admin_food_imageV);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.admin_container,new add_food_fragment(),null).commit();
            }
        });

        return view;
    }
    public void onResume() {
        super.onResume();

        layoutOuter.removeAllViews();
        items=new ArrayList<>();
        layoutAdd.setVisibility(View.INVISIBLE);

         DatabaseHandler handler = new DatabaseHandler(DatabaseHandler.getitems_link,getContext()) {
            @Override
            public void writeCode(String response) throws JSONException {
                layoutAdd.setVisibility(View.VISIBLE);
                JSONArray jsonArray = new JSONArray(response);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    items.add(new FoodItems(object.getString("name"),object.getDouble("price"),object.getString("image")));
                }
                for(int i=0;i<items.size();i++)
                {
                    dynamic_views(i);
                }

            }
            @Override
            public Map<String, String> params() {
                Map<String,String > map = new HashMap<>();
                map.put("rid", AdminActivity.sharedPreferencesHandler.getId());
                return map;
            }
        };
        handler.execute();

    }
    private void dynamic_views(int i){
        Resources r = getResources();
        final RelativeLayout layoutInner = new RelativeLayout(getContext());
        layoutInner.setId(i);
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());
        final RelativeLayout.LayoutParams layoutForInner = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, px);
        layoutForInner.setMargins(10,10,10,10);
        layoutInner.setBackgroundResource(R.drawable.border_layout);
        layoutInner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id = v.getId();
                bitmapDB = ((BitmapDrawable)((ImageView)view.findViewById(id+100)).getDrawable()).getBitmap();
                nameDB = ((TextView)view.findViewById(id+200)).getText().toString();
                priceDB = ((TextView)view.findViewById(id+300)).getText().toString().substring(1);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.admin_container,new modify_item_fragment(),null).commit();
            return true;
            }
        });

        image = new ImageView(getContext());
        int px1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
        RelativeLayout.LayoutParams layoutForImage= new RelativeLayout.LayoutParams
                (px1, px1);
        layoutForImage.leftMargin = 20;
        layoutForImage.topMargin = 20;
        layoutForImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        image.setId(i + 100);
        String imagebase64 =  items.get(i).getImage();
        byte[] imagedecoded = Base64.decode(imagebase64,Base64.DEFAULT);
        Bitmap decodedimage = BitmapFactory.decodeByteArray(imagedecoded,0,imagedecoded.length);
        image.setImageBitmap(decodedimage);

        name = new TextView(getContext());
        RelativeLayout.LayoutParams layoutForName = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutForName.leftMargin = 20;
        layoutForName.topMargin = 20;
        layoutForName.addRule(RelativeLayout.RIGHT_OF,image.getId());
        name.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        name.setTextSize(1, 18);
        name.setId(i + 200);
        name.setText(items.get(i).getName());

        price = new TextView(getContext());
        RelativeLayout.LayoutParams layoutForPrice = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutForPrice.topMargin = 10;
        layoutForPrice.leftMargin = 20;
        layoutForPrice.addRule(RelativeLayout.BELOW,name.getId());
        layoutForPrice.addRule(RelativeLayout.RIGHT_OF,image.getId());
        price.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        price.setTextSize(1, 12);
        price.setId(i + 300);
        price.setText("₹"+items.get(i).getPrice().toString());


        delete = new Button(getContext());
        delete.setId(i + 400);
        delete.setBackgroundResource(R.drawable.ic_delete);
        RelativeLayout.LayoutParams layoutFordelete = new RelativeLayout.LayoutParams
                (35, 35);
        layoutFordelete.rightMargin = 20;
        layoutFordelete.topMargin = 20;
        layoutFordelete.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("ResourceType") int id = v.getId()-400;
                final String namefordelete = ((TextView)view.findViewById(id+200)).getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You really want to remove '"+namefordelete+"'?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler databaseHandler = new DatabaseHandler("http://34.93.41.224/delete_item.php",getContext()) {
                            @Override
                            public void writeCode(String response) throws Exception {
                                Toast.makeText(getContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                onResume();
                            }
                            @Override
                            public Map<String, String> params() {
                                Map<String, String> map = new HashMap<>();
                                map.put("name", namefordelete);
                                return map;
                            }
                        };
                        databaseHandler.execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        layoutInner.addView(delete,layoutFordelete);
        layoutInner.addView(name, layoutForName);
        layoutInner.addView(price, layoutForPrice);
        layoutInner.addView(image, layoutForImage);
        layoutOuter.addView(layoutInner, layoutForInner);
    }
}
