package com.project.restaurantmanager.UI.Base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.Data.Constants;
import com.project.restaurantmanager.Model.MainActivity;
import com.project.restaurantmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.project.restaurantmanager.Controller.DatabaseHandler.login_link;
import static com.project.restaurantmanager.Controller.DatabaseHandler.welcome_link;

public class register_restaurant_fragment extends Fragment {

    String[] locations = {"Baroda", "Anand", "VVNagar", "Ahemdabad","Nadiad","Surat","Bharuch","Bhuj","Rajkot","Bhavnagar"};
    int FILE_SELECT_CODE = 023432;
    ImageButton imageView;
    String encoded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_restaurant_fragment, container, false);

        final Spinner location = view.findViewById(R.id.reg_res_location);

        imageView = view.findViewById(R.id.reg_res_image);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, locations);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(arrayAdapter);



        final TextInputEditText username = view.findViewById(R.id.reg_res_username);
        final TextInputEditText name = view.findViewById(R.id.reg_res_name);
        final TextInputEditText email = view.findViewById(R.id.reg_res_email);
        final TextInputEditText password = view.findViewById(R.id.reg_res_password);
        final TextInputEditText mobile = view.findViewById(R.id.reg_res_contact);
        final TextInputEditText address = view.findViewById(R.id.reg_res_address);
        final TextInputEditText gstin = view.findViewById(R.id.reg_res_gstin);

        Button signup = view.findViewById(R.id.reg_res_RegisterBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler handler = new DatabaseHandler("http://34.93.41.224/add_restaurant.php", getContext()) {
                    @Override
                    public void writeCode(String response) throws JSONException, InterruptedException, Exception {
                        Log.d("ffff", "writeCode: " + response);
                        if (new JSONObject(response).getInt("error") == 0) {
                            Toast.makeText(getContext(), Constants.getError0, Toast.LENGTH_SHORT).show();
                        } else if (new JSONObject(response).getInt("error") == 1) {
                            Toast.makeText(getContext(), Constants.getError1, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else if (new JSONObject(response).getInt("error") == 2) {
                            Toast.makeText(getContext(), Constants.getError2, Toast.LENGTH_SHORT).show();
                        } else if (new JSONObject(response).getInt("error") == 3) {
                            Toast.makeText(getContext(), Constants.getError3, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public Map<String, String> params() {

                        Map<String, String> map = new HashMap<>();
                        map.put("username", username.getText().toString());
                        map.put("password", password.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("contact", mobile.getText().toString());
                        map.put("address", address.getText().toString());
                        map.put("location", location.getSelectedItem().toString());
                        map.put("GSTIN", gstin.getText().toString());
                        map.put("image",encoded);

                        return map;
                    }
                };

                handler.execute();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser, FILE_SELECT_CODE);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == -1) {
                Uri uri = data.getData();

                imageView.setImageURI(uri);

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

                Bitmap image = drawable.getBitmap();
                image = Bitmap.createScaledBitmap(image, 512, 512, false);

                imageView.setImageBitmap(image);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 65, outputStream);
                byte[] arry = outputStream.toByteArray();

                encoded = Base64.encodeToString(arry, 0, arry.length, Base64.DEFAULT);

            }
        }

    }
}
