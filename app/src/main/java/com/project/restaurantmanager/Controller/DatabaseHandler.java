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

    public static String admin_setEmployeeLink = "http://34.93.41.224/register_employee.php";
    public static String admin_getEmployeeLink = "http://34.93.41.224/admin_getEmployee.php";
    public static String admin_online_set_orderLink = "http://34.93.41.224/admin_set_order_online.php?ono=22";
    public static String admin_onlineordLink = "http://34.93.41.224/get_admin_online_orders.php";
    public static String admin_resLink = "http://34.93.41.224/admin_reservation.php";
    public static String send_invoiceLink = "http://34.93.41.224/send_invoice.php";
    public static String finishorder_link = "http://34.93.41.224/finishOrder.php";
    public static String welcome_link = "http://34.93.41.224/welcome_mailrequest.php";
    public static String employee_getorders_link = "http://34.93.41.224/employee_getOrders.php";
    public static String admin_getorders_link = "http://34.93.41.224/admin__getOrders.php";
    public static String sendEmpOrders_link = "http://34.93.41.224/employee_order.php";
    public static String gettables_link = "http://34.93.41.224/tables.php";
    public static String yourorders_link = "http://34.93.41.224/yourorders.php";
    public static String yourreservation_link = "http://34.93.41.224/your_reservation.php";
    public static String getitems_link = "http://34.93.41.224/getItems.php";
    public static String login_link = "http://34.93.41.224/index.php";
    public static String singup_link = "http://34.93.41.224/signup.php";
    public static String afterpayment_link = "http://34.93.41.224/after_payment.php";
    public static String otp_link = "http://34.93.41.224/otp.php";
    public static String reservation_link = "http://34.93.41.224/reservation.php";
    public static String manageaccount_link = "http://34.93.41.224/manageaccount.php";
    public static String contactus_link = "http://34.93.41.224/contactus.php";
    public static String updateaddress_link = "http://34.93.41.224/updateaddress.php";
    public static String updatepassword_link = "http://34.93.41.224/updatepassword.php";
    public static String placeorder_link = "http://34.93.41.224/placeorder_customer.php";
    public static String mailrequest_link = "http://34.93.41.224/mailrequest.php";
    public static String admin_allorders = "http://34.93.41.224/admin_allorders.php";

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
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }
    public abstract void writeCode(String response) throws JSONException, InterruptedException, Exception;
    public abstract Map<String, String> params();
}
