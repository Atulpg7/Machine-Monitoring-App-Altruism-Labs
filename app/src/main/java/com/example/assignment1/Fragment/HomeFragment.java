package com.example.assignment1.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment1.SavedDetailsClass;
import com.example.assignment1.ServerDataClass;
import com.example.assignment1.MyPrefs;
import com.example.assignment1.R;
import com.example.assignment1.SettingsActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONObject;

public class HomeFragment extends Fragment {


    // PieChartView chartView1,chartView2; // old chart
    PieChart pie_chart_oee, pie_chart_production;

    View main_view;
    TextView machine_name, status, shift, job_name, target, order_no, start_time, production,
            downtime, operator_name, availability_tv, productivity_tv, rejection_tv, cycle_time;

    ProgressBar availability_pb, productivity_pb, rejection_pb;

    ProgressDialog progressDialog;

    String sta = "34", sh = "2", jn = "22.4", ti = "10", orn = "A678", st = "60", pro = "22.4", dw = "10:89:4", ona = "Roshan", av = "66.8", prd = "56.7", rej = "44.9", ct = "12.06", oee_ch = "22.4", prod_ch;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //Getting main fragment view
        main_view = inflater.inflate(R.layout.fragment_home, container, false);


        getReferences();

        checkAdminLogin();
        setGeneralClassData();

        //setDataMainPage();

        new sendData().execute();


        setChart1();
        setChart2();


        //return view
        return main_view;
    }


    class sendData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Sending Data...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {


            Log.e("URL Main Page==> ", ServerDataClass.getUrlHome());


            JSONObject object = new JSONObject();

            try {

                object.put("client_id", SavedDetailsClass.cid);
                object.put("device_id", SavedDetailsClass.did);
                object.put("machine_id", SavedDetailsClass.mid);
                object.put("machine_name", SavedDetailsClass.mname);
                Log.e("Sending Server MP==> ", object.toString());

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ServerDataClass.getUrlHome(), object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Response Main Page ==> ", "" + response);
                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), "Something Went Wrong !", Toast.LENGTH_SHORT).show();
                    Log.e("Error Main Page ==> ", "" + error);
                    progressDialog.dismiss();

                }
            });
//            StringRequest request = new StringRequest(Request.Method.GET, GlobalData.getUrlHome(), new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//
//                    //progressDialog.setMessage("Fetching Data");
//
//                    Log.e("Response Main Page ==> ",""+response);
//                    progressDialog.dismiss();
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Toast.makeText(getActivity(), "Something Went Wrong !", Toast.LENGTH_SHORT).show();
//                    Log.e("Error Main Page ==> ",""+error);
//                    progressDialog.dismiss();
//
//                }
//            }){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//
//                    HashMap<String,String> params = new HashMap<>();
//
//                    params.put("client_id",GeneralClass.cid);
//                    params.put("device_id",GeneralClass.did);
//                    params.put("machine_id",GeneralClass.mid);
//                    params.put("machine_name",GeneralClass.mname);
//
//
//                    Log.e("Sending Server MP==> ",params.toString());
//
//                    return params;
//                }
//            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            //setDataMainPage();
        }
    }


    //Function to set data of main page
    private void setDataMainPage() {

        machine_name.setText(SavedDetailsClass.mname);
        status.setText(sta);
        shift.setText(sh);
        job_name.setText(jn);
        target.setText(ti);
        order_no.setText(orn);
        start_time.setText(st);
        production.setText(pro);
        downtime.setText(dw);
        operator_name.setText(ona);
        availability_tv.setText(av);
        productivity_tv.setText(prd);
        rejection_tv.setText(rej);
        cycle_time.setText(ct);

        availability_pb.setProgress((int) Double.parseDouble(av));
        productivity_pb.setProgress((int) Double.parseDouble(prd));
        rejection_pb.setProgress((int) Double.parseDouble(rej));

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    //Function for getting reference
    private void getReferences() {

        machine_name = main_view.findViewById(R.id.machine_name);
        status = main_view.findViewById(R.id.status);
        shift = main_view.findViewById(R.id.shift);
        job_name = main_view.findViewById(R.id.job_name);
        target = main_view.findViewById(R.id.target);
        order_no = main_view.findViewById(R.id.orderno);
        start_time = main_view.findViewById(R.id.starttime);
        production = main_view.findViewById(R.id.production);
        downtime = main_view.findViewById(R.id.downtime);
        operator_name = main_view.findViewById(R.id.operator_name);
        availability_tv = main_view.findViewById(R.id.availability_tv);
        productivity_tv = main_view.findViewById(R.id.productivity_tv);
        rejection_tv = main_view.findViewById(R.id.rejection_tv);
        availability_pb = main_view.findViewById(R.id.availability_pb);
        productivity_pb = main_view.findViewById(R.id.productivity_pb);
        rejection_pb = main_view.findViewById(R.id.rejection_pb);
        pie_chart_oee = main_view.findViewById(R.id.pie_chart_oee);
        pie_chart_production = main_view.findViewById(R.id.pie_chart_production);
        cycle_time = main_view.findViewById(R.id.cycletime);
        progressDialog = new ProgressDialog(getActivity());

    }


    //Function to set General class data for all activities to send
    // to the server
    private void setGeneralClassData() {

        MyPrefs.prefs = getActivity().getSharedPreferences(MyPrefs.MY_PREF_NAME, getActivity().MODE_PRIVATE);
        String cid = MyPrefs.prefs.getString("c_id", "");
        String did = MyPrefs.prefs.getString("d_id", "");
        String mid = MyPrefs.prefs.getString("m_id", "");
        String mname = MyPrefs.prefs.getString("m_name", "");
        String sip = MyPrefs.prefs.getString("s_ip", "");

        ServerDataClass.setIP_Address(sip);//Setting IP Address


        if (!checkLen(cid)) {
            SavedDetailsClass.cid = cid;
        }

        if (!checkLen(did)) {
            SavedDetailsClass.did = did;
        }

        if (!checkLen(mid)) {
            SavedDetailsClass.mid = mid;
        }

        if (!checkLen(mname)) {
            SavedDetailsClass.mname = mname;
        }

        if (!checkLen(sip)) {
            SavedDetailsClass.sip = sip;
            ServerDataClass.setIP_Address(sip);
        }


    }


    //Function to validating strings length
    private boolean checkLen(String s) {
        return s.equals("");
    }


    //Function to check Logged in user is admin or normal user
    private void checkAdminLogin() {

        MyPrefs.prefs = getActivity().getSharedPreferences(MyPrefs.MY_PREF_NAME, getActivity().MODE_PRIVATE);
        String isAdmin = MyPrefs.prefs.getString("isAdmin", "false");

        if (isAdmin.equals("true")) {
            main_view.findViewById(R.id.configure_settings_layout).setVisibility(View.VISIBLE);
            main_view.findViewById(R.id.settings_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

        }
    }


    //Function to set OEE percentage chart
    private void setChart1() {

        float data = Float.parseFloat(oee_ch);
        pie_chart_oee.addPieSlice(new PieModel("OEE " + data + "%", data, getActivity().getResources().getColor(R.color.not_active)));
        pie_chart_oee.setInnerValueString(data + "%");

        if (100 - data > 0) {
            pie_chart_oee.addPieSlice(new PieModel("OEE " + data + "%", 100.0f - data, getActivity().getResources().getColor(R.color.grey)));
        }

        pie_chart_oee.startAnimation();
    }

    //Function to set Production percentage chart
    private void setChart2() {

        float data = (Float.parseFloat(pro) / Float.parseFloat(ti)) * 100;

        pie_chart_production.addPieSlice(new PieModel("Production " + data + "%", data, getActivity().getResources().getColor(R.color.active)));
        pie_chart_production.setInnerValueString(data + "%");

        if (100 - data > 0) {
            pie_chart_production.addPieSlice(new PieModel("Production " + data + "%", 100.0f - data, getActivity().getResources().getColor(R.color.grey)));
        }

        pie_chart_production.startAnimation();
    }


}