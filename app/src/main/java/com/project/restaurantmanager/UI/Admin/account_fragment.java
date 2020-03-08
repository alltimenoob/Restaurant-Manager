package com.project.restaurantmanager.UI.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.restaurantmanager.Controller.FirebaseMessageService;
import com.project.restaurantmanager.Controller.SharedPreferencesHandler;
import com.project.restaurantmanager.Model.AdminActivity;
import com.project.restaurantmanager.Model.MainActivity;
import com.project.restaurantmanager.R;
import com.project.restaurantmanager.UI.Admin.AccountFragmentModules.manage_account_fragment;
import com.project.restaurantmanager.UI.Customer.AccountFragmentModules.change_password;
import com.project.restaurantmanager.UI.Customer.AccountFragmentModules.help_fragment;

public class account_fragment extends Fragment {
    private View view;
    private String[] name = new String[]{"Change Password","Account Setting", "Help", "Log Out"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_account_fragment,container,false);

        ListView listView = view.findViewById(R.id.admin_acc_list);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        TextView name = view.findViewById(R.id.admin_acc_name_text);
        name.setText(MainActivity.sharedPreferences.getUsername());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        AdminActivity.fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.admin_container,new change_password(),null).commit();
                        break;
                    case 1:
                        AdminActivity.fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.admin_container, new manage_account_fragment(), null).commit();
                        break;
                    case 2:
                        AdminActivity.fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.admin_container, new help_fragment(),null).commit();
                        break;
                    case 3:
                        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getContext());
                        sharedPreferencesHandler.setFlag(false);
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                        FirebaseMessageService.removeToken("admin",sharedPreferencesHandler.getId());
                        /*To Prevent Unauthorized Login*/
                        getActivity().finish();
                        break;
                }
            }
        });
        return view;
    }
    /* List Needs Adapter For Gathering Data,
     Here CustomAdapter Takes Data From name String
     And Bind It To ListView
     */
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return name.length;
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
            convertView = getLayoutInflater().inflate(R.layout.listview_account_layout,null);

            TextView settingName = convertView.findViewById(R.id.list_TextView);
            ImageView settingNextImage = convertView.findViewById(R.id.list_imageView);

            settingNextImage.setImageResource(R.drawable.ic_next);
            settingName.setText(name[position]);

            return convertView;
        }
    }
}
