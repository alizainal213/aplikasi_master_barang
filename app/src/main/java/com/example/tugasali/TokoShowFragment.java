package com.example.tugasali;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 **/
public class TokoShowFragment extends Fragment {

    public TokoShowFragment(){


    }

    ArrayList<HashMap<String, String>>Baranglist;


    private String url;
    RecyclerView lvbarang;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_toko_show,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lvbarang = view.findViewById(R.id.lv_barang);
        lvbarang.setLayoutManager(new LinearLayoutManager(getActivity()));
        Baranglist = new ArrayList<HashMap<String,String>>();
        url = "http://192.168.23.108/SMP_IDN/webdatabase/apitampilbarang.php";
        showData();


    }

    private void showData() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading ");
        progressDialog.setMessage("Tunggu Coy");
        progressDialog.show();

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();


                try {
                    JSONArray jsonArray = response.getJSONArray("barang");

                    for (int a = 0; a < jsonArray.length(); a++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        HashMap<String, String> rowData = new HashMap<String, String>();
                        rowData.put("nama", jsonObject.getString("nama_barang"));
                        rowData.put("stock", jsonObject.getString("stock_barang"));
                        Baranglist.add(rowData);


                    }
                    TokoAdapter tokoAdapter = new TokoAdapter(getActivity(), Baranglist);
                    lvbarang.setAdapter(tokoAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Gagal convert data", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("logku", "onErrorResponse: " + error.getMessage());
            }
        });


        RequestQueue myRequestQueue = Volley.newRequestQueue(getActivity());
        myRequestQueue.add(myRequest);
    }
}