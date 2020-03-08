package com.project.restaurantmanager.UI.Admin.CheckOutModules;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.restaurantmanager.Controller.DatabaseHandler;
import com.project.restaurantmanager.R;

import java.util.HashMap;
import java.util.Map;

import static com.project.restaurantmanager.Controller.DatabaseHandler.send_invoiceLink;
import static com.project.restaurantmanager.UI.Admin.checkout_fragment.ono;

public class invoice_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_invoice_fragment,container,false);

        WebView webView = view.findViewById(R.id.admin_invoice_webview);

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://34.93.41.224/invoice_templet_mobile.php?ono="+ono+"+&email="+"");


        Button send = view.findViewById(R.id.admin_invoice_sendmail);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = new LinearLayout(getContext());
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(50, 10, 50, 10);

                final EditText email = new EditText(getContext());
                layout.addView(email);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Email");
                builder.setView(layout);
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(send_invoiceLink,getContext()) {
                            @Override
                            public void writeCode(String response) throws Exception {
                                Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public Map<String, String> params() {
                                Map<String,String> map = new HashMap<>();
                                map.put("ono",""+ono);
                                map.put("email",email.getText().toString());
                                return map;
                            }
                        };
                        databaseHandler.execute();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        return view;
    }
}
