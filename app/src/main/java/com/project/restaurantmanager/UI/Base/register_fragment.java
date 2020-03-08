package com.project.restaurantmanager.UI.Base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.project.restaurantmanager.Controller.DatabaseHandler.login_link;
import static com.project.restaurantmanager.Controller.DatabaseHandler.welcome_link;

public class register_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment,container,false);

        final TextInputEditText username = view.findViewById(R.id.reg_username);
        final TextInputEditText name = view.findViewById(R.id.reg_name);
        final TextInputEditText email = view.findViewById(R.id.reg_email);
        final TextInputEditText password = view.findViewById(R.id.reg_password);
        final TextInputEditText mobile = view.findViewById(R.id.reg_contact);
        final TextInputEditText address = view.findViewById(R.id.reg_address);
        final ProgressBar progressBar = view.findViewById(R.id.signup_progress_circular);
        final Button signupBtn = view.findViewById(R.id.reg_RegisterBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);

                DatabaseHandler databaseHandler = new DatabaseHandler("http://34.93.41.224/signup.php",getContext()) {
                    @Override
                    public void writeCode(String response) throws Exception {
                        progressBar.setVisibility(View.GONE);
                        progressBar.setIndeterminate(false);
                        Thread.sleep(1000);

                        if(new JSONObject(response).getInt("error")==0)
                        {
                            Toast.makeText(getContext(), Constants.getError0, Toast.LENGTH_SHORT).show();
                        }
                        else if(new JSONObject(response).getInt("error")==1)
                        {
                            Toast.makeText(getContext(), Constants.getError1, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else if(new JSONObject(response).getInt("error")==2)
                        {
                            Toast.makeText(getContext(), Constants.getError2, Toast.LENGTH_SHORT).show();
                        }
                        else if(new JSONObject(response).getInt("error")==2)
                        {
                            Toast.makeText(getContext(), Constants.getError3, Toast.LENGTH_SHORT).show();
                        }

                        username.setText("");name.setText("");email.setText("");
                        password.setText("");mobile.setText("");address.setText("");
                    }
                    @Override
                    public Map<String, String> params() {
                        Map<String,String> map = new HashMap<>();
                        map.put("username", Objects.requireNonNull(username.getText()).toString());
                        map.put("email", Objects.requireNonNull(email.getText()).toString());
                        map.put("password", Objects.requireNonNull(password.getText()).toString());
                        map.put("name", Objects.requireNonNull(name.getText()).toString());
                        map.put("contact", Objects.requireNonNull(mobile.getText()).toString());
                        map.put("address", Objects.requireNonNull(address.getText()).toString());
                        return map;
                    }
                };
                databaseHandler.execute();
            }
        });


        return view;
    }

}
