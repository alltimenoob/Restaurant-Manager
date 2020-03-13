package com.project.restaurantmanager.Controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;


import java.util.Map;


public abstract class DatabaseHandler {

    String link;
    Context context;
    RequestQueue queue;


    //Links

    public static final String REGISTER_RESTAURANT_CHECK = "http://34.93.41.224/RegisterRestaurantCheck.php";
    public static final String REGISTER_RESTAURANT = "http://34.93.41.224/RegisterRestaurant.php";
    public static final String REGISTER_EMPLOYEE = "http://34.93.41.224/RegisterEmployee.php";
    public static final String EMPLOYEE_LIST_ADMIN = "http://34.93.41.224/EmployeeListAdmin.php";
    public static final String ACCEPT_ONLINE_ORDER_ADMIN = "http://34.93.41.224/AcceptOnlineOrderAdmin.php";
    public static final String ADMIN_ONLINE_ORDER_LINK = "http://34.93.41.224/OnlineOrderListAdmin.php";
    public static final String RESERVATION_LIST_ADMIN = "http://34.93.41.224/ReservationListAdmin.php";
    public static final String INVOICE_TO_CUSTOMER_ADMIN = "http://34.93.41.224/InvoiceToCustomerAdmin.php";
    public static final String UPDATE_FINISH_ORDER_EMPLOYEE = "http://34.93.41.224/UpdateFinishOrderEmployee.php";
    public static final String WELCOME_MAIL_CUSTOMER = "http://34.93.41.224/WelcomeMailCustomer.php";
    public static final String ORDER_ITEMLIST_EMPLOYEE_DASHBOARD = "http://34.93.41.224/OrderItemListEmployeeDashboard.php";
    public static final String OFFLINE_ORDER_ITEMLIST_ADMIN = "http://34.93.41.224/OfflineOrderItemListAdmin.php";
    public static final String INSERT_NEW_ODER_EMPLOYEE = "http://34.93.41.224/InsertNewOrderEmployee.php";
    public static final String TABLE_LIST_EMPLOYEE_DASHBOARD = "http://34.93.41.224/TableListEmployeeDashboard.php";
    public static final String ORDER_LIST_CUSTOMER = "http://34.93.41.224/OrderListCustomer.php";
    public static final String RESERVATION_LIST_CUSTOMER = "http://34.93.41.224/ReservationListCustomer.php";
    public static final String FOOD_ITEMLIST_CUSTOMER = "http://34.93.41.224/FoodItemListCustomer.php";
    public static final String LOGIN = "http://34.93.41.224/Login.php";
    public static final String REGISTER_CUSTOMER = "http://34.93.41.224/RegisterCustomer.php";
    public static final String INSERT_NEW_RESERVATION_CUSTOMER = "http://34.93.41.224/InsertNewReservationCustomer.php";
    public static final String RESET_PASSWORD_CUSTOMER = "http://34.93.41.224/ResetPasswordCustomer.php";
    public static final String INSERT_RESERVATION_CHECK_CUSTOMER = "http://34.93.41.224/InsertReservationCheckCustomer.php";
    public static final String UPDATE_ACCOUNT_SETTING_EMPLOYEE_CUSTOMER = "http://34.93.41.224/UpdateAccountSettingCustomerEmployee.php";
    public static final String CONTACT_US = "http://34.93.41.224/ContactUs.php";
    public static final String UPDATE_ADDRESS_CUSTOMER_EMPLOYEE = "http://34.93.41.224/UpdateAddressCustomerEmployee.php";
    public static final String UPDATE_PASSWORD_CUSTOMER_EMPLOYEE = "http://34.93.41.224/UpdatePasswordCustomerEmployee.php";
    public static final String INSERT_NEW_ORDER_CUSTOMER = "http://34.93.41.224/InsertNewOrderCustomer.php";
    public static final String RESET_PASSWORD_MAIL_CUSTOMER = "http://34.93.41.224/ResetPasswordMailCustomer.php";
    public static final String ORDER_LIST_ADMIN = "http://34.93.41.224/OrderListAdmin.php";
	public static final String OFFLINE_ORDER_LIST_ADMIN = "http://34.93.41.224/OfflineOrderListAdmin.php";
	public static final String DELETE_FOOD_ITEM_ADMIN = "http://34.93.41.224/DeleteFoodItemAdmin.php";
	public static final String TABLE_LIST_ADMIN_EMPLOYEE = "http://34.93.41.224/TableListAdminEmployee.php";
	public static final String INSERT_TABLE_ADMIN = "http://34.93.41.224/InsertTableAdmin.php";
	public static final String DELETE_TABLE_ADMIN = "http://34.93.41.224/DeleteTableAdmin.php";
	public static final String UPDATE_TABLE_ADMIN = "http://34.93.41.224/UpdateTableAdmin.php";
	public static final String DELETE_EMPLOYEE_ADMIN = "http://34.93.41.224/DeleteEmployeeAdmin.php";
	public static final String INSERT_FOODITEM_ADMIN = "http://34.93.41.224/InsertFoodItemAdmin.php";
	public static final String UPDATE_FOODITEM_ADMIN = "http://34.93.41.224/UpdateFoodItemAdmin.php";
	public static final String SEND_NOTIFICATION = "http://34.93.41.224/SendNotification.php";
	public static final String UPDATE_FOODITEM_SERVED_EMPLOYEE = "http://34.93.41.224/UpdateFoodItemServedEmployee.php";
	public static final String RESTAURANT_LIST_CUSTOMER = "http://34.93.41.224/RestaurantListCustomer.php";
	public static final String RESTAURANT_OPEN_CLOSE_STATUS = "http://34.93.41.224/RestaurantOpenCloseStatus.php";
    public static final String RESTAURANT_OPEN_CLOSE = "http://34.93.41.224/RestaurantOpenCloseAdmin.php";

    public DatabaseHandler(String link, Context context) {
        this.link = link;
        this.context = context;
    }

    public void execute(){
        queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    writeCode(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("LUND", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("LUND", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params();
            }
        };
        /* To Prevent Double Post Request */
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }
    public abstract void writeCode(String response) throws JSONException, InterruptedException, Exception;
    public abstract Map<String, String> params();
}
