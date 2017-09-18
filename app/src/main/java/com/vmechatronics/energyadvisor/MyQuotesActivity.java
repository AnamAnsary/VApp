package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vmplapp on 10/8/17.
 */

public class MyQuotesActivity extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<QuoteListItem> listItems;
    //ArrayList<HashMap<String, String>> QuoteList = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> user;
    UserSessionManager session;
    private static final String TAG = "MyQuotesActivity";

    String email = "";
    String name = "";
    String qid = "";
    String qdate = "";
    TextView tvQID;
    TextView tvQDT;
    TextView tvBSV;
    RelativeLayout RLQuote;
    private String getQuote_url = "https://www.vmechatronics.com/app/get_quote.php";

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myquotes);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        name = user.get("name");

        if (!session.isUserLoggedIn()) {
            Intent i = new Intent(MyQuotesActivity.this, Login.class);
            i.putExtra("class", "MyQuote");
            startActivity(i);
            finish();
        }
        listItems = new ArrayList<>();

        /*for( int i = 0; i<=10; i++) {
            QuoteListItem listItem = new QuoteListItem (
                    "heading" +(i+1),
                    "Lorem ipsum dummy text"
            );
            listItems.add(listItem);
        }
        adapter = new MyQuotesAdapter(listItems, this);
        recyclerView.setAdapter(adapter);*/

        getPersonQuote(email);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                QuoteListItem movie = listItems.get(position);
               // Toast.makeText(getApplicationContext(), movie.getTvqid() + " is selected!", Toast.LENGTH_SHORT).show();

                RLQuote = (RelativeLayout) view.findViewById(R.id.RLQuote);
                tvQID = (TextView) view.findViewById(R.id.tvQID);
                tvQDT = (TextView) view.findViewById(R.id.tvQDT);
                tvBSV = (TextView) view.findViewById(R.id.tvBSV);
                qid = tvQID.getText().toString();
                qdate = tvQDT.getText().toString();
                Log.w(TAG, "onClick: in activity qid and qdate is " +qid+ " " +qdate);
                tvBSV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ii = new Intent(view.getContext(), Book.class);
                        ii.putExtra("qid", qid);
                        ii.putExtra("qdate", qdate);
                        view.getContext().startActivity(ii);
                    }
                });

                RLQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        b.putString("qid",qid);
                        Intent in = new Intent(view.getContext(), DownloadQuotes.class);
                        in.putExtras(b);
                        view.getContext().startActivity(in);
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.w(TAG, "onLongClick: " );

            }
        }));

    }

   /* private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }*/

    public void getPersonQuote(final String email) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (checkInternetConnection()) {
           /* final Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        Log.w(TAG, "onResponse: " + response);
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray jsonArray = jsonObj.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            *//*new code start*//*
                            QuoteListItem item = new QuoteListItem(
                                    c.getString("qid"),
                                    c.getString("qdate")
                            );

                            listItems.add(item);
                            *//*new code ends here*//*

                           *//* String qid = c.getString("qid");
                            String qdate = c.getString("qdate");
                            HashMap<String, String> persons = new HashMap<String, String>();
                            persons.put("qid", qid);
                            persons.put("qdate", qdate);
                            QuoteList.add(persons);*//*
                        }

                        //old code start
                        *//*Log.w(TAG, "onCreate: " + QuoteList.size());
                        if (QuoteList.size() != 0) {
                            ListView listView = getListView();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
                                    RLQuote = (RelativeLayout) view.findViewById(R.id.RLQuote);
                                    tvQID = (TextView) view.findViewById(R.id.tvQID);
                                    tvQDT = (TextView) view.findViewById(R.id.tvQDT);
                                    tvBSV = (TextView) view.findViewById(R.id.tvBSV);
                                    qid = tvQID.getText().toString();
                                    qdate = tvQDT.getText().toString();
                                    tvBSV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent ii = new Intent(MyQuotesActivity.this, Book.class);
                                            ii.putExtra("qid", qid);
                                            ii.putExtra("qdate", qdate);
                                            startActivity(ii);
                                        }
                                    });

                                    RLQuote.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle b = new Bundle();
                                            b.putString("qid",qid);
                                            Intent in = new Intent(MyQuotesActivity.this, DownloadQuotes.class);
                                            in.putExtras(b);
                                            startActivity(in);

                                           *//**//* Intent myIntent = new Intent(MyQuotes.this, ListItemActivity2.class);
                                            setContentView(R.layout.activity_battery_quote);
                                            //Toast.makeText(MyQuotes.this,"List item clicked", Toast.LENGTH_SHORT).show();
                                            TextView tv1 = (TextView) findViewById(R.id.tv1);
                                            TextView tvName = (TextView) findViewById(R.id.tvName);
                                            final TextView tvRefID = (TextView) findViewById(R.id.tvRefID);
                                            ImageButton bGoHom = (ImageButton) findViewById(R.id.bGoHom);
                                            ImageButton bDowQuo = (ImageButton) findViewById(R.id.bDowQuo);
                                            Button rep = (Button) findViewById(R.id.report);*//**//*
                                        }
                                    });
                                }
                            });
                            ListAdapter adapter = new SimpleAdapter(MyQuotesActivity.this, QuoteList,
                                    R.layout.quote_entry, new String[]{"qid", "qdate"},
                                    new int[]{R.id.tvQID, R.id.tvQDT});
                            setListAdapter(adapter);
                        } else {
                            new AlertDialog.Builder(MyQuotesActivity.this)
                                    .setTitle("No Quotes")
                                    .setMessage("You have not generated any Quotes yet..")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MyQuotesActivity.this, Profile.class));
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }*//*
                        //old code end

                       // adapter = new MyQuotesAdapter(listItems,getApplicationContext());
                        adapter = new MyQuotesAdapter(listItems,getApplicationContext());
                        recyclerView.setAdapter(adapter);

                       *//* adapter = new MyQuotesAdapter(MyQuotesActivity.this, QuoteList,
                                R.layout.myquotes_listitem, new String[]{"qid", "qdate"},
                                new int[]{R.id.tvQID, R.id.tvQDT});*//*
                        //setListAdapter(adapter);
                       // recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            QuoteRequest qReq = new QuoteRequest(email, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MyQuotesActivity.this);
            queue.add(qReq);*/

            //Log.w(TAG, "getPersonTransact: list created and returned");

            StringRequest stringRequest = new StringRequest(Request.Method.POST,getQuote_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                Log.w(TAG, "onResponse: " + response);
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray jsonArray = jsonObj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);

                            /*new code start*/
                                    QuoteListItem item = new QuoteListItem(
                                            c.getString("qid"),
                                            c.getString("qdate")
                                    );
                                    listItems.add(item);
                            /*new code ends here*/

                           /* String qid = c.getString("qid");
                            String qdate = c.getString("qdate");
                            HashMap<String, String> persons = new HashMap<String, String>();
                            persons.put("qid", qid);
                            persons.put("qdate", qdate);
                            QuoteList.add(persons);*/
                                }

                                //old code start
                        /*Log.w(TAG, "onCreate: " + QuoteList.size());
                        if (QuoteList.size() != 0) {
                            ListView listView = getListView();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
                                    RLQuote = (RelativeLayout) view.findViewById(R.id.RLQuote);
                                    tvQID = (TextView) view.findViewById(R.id.tvQID);
                                    tvQDT = (TextView) view.findViewById(R.id.tvQDT);
                                    tvBSV = (TextView) view.findViewById(R.id.tvBSV);
                                    qid = tvQID.getText().toString();
                                    qdate = tvQDT.getText().toString();
                                    tvBSV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent ii = new Intent(MyQuotesActivity.this, Book.class);
                                            ii.putExtra("qid", qid);
                                            ii.putExtra("qdate", qdate);
                                            startActivity(ii);
                                        }
                                    });

                                    RLQuote.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle b = new Bundle();
                                            b.putString("qid",qid);
                                            Intent in = new Intent(MyQuotesActivity.this, DownloadQuotes.class);
                                            in.putExtras(b);
                                            startActivity(in);

                                           *//* Intent myIntent = new Intent(MyQuotes.this, ListItemActivity2.class);
                                            setContentView(R.layout.activity_battery_quote);
                                            //Toast.makeText(MyQuotes.this,"List item clicked", Toast.LENGTH_SHORT).show();
                                            TextView tv1 = (TextView) findViewById(R.id.tv1);
                                            TextView tvName = (TextView) findViewById(R.id.tvName);
                                            final TextView tvRefID = (TextView) findViewById(R.id.tvRefID);
                                            ImageButton bGoHom = (ImageButton) findViewById(R.id.bGoHom);
                                            ImageButton bDowQuo = (ImageButton) findViewById(R.id.bDowQuo);
                                            Button rep = (Button) findViewById(R.id.report);*//*
                                        }
                                    });
                                }
                            });
                            ListAdapter adapter = new SimpleAdapter(MyQuotesActivity.this, QuoteList,
                                    R.layout.quote_entry, new String[]{"qid", "qdate"},
                                    new int[]{R.id.tvQID, R.id.tvQDT});
                            setListAdapter(adapter);
                        } else {
                            new AlertDialog.Builder(MyQuotesActivity.this)
                                    .setTitle("No Quotes")
                                    .setMessage("You have not generated any Quotes yet..")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MyQuotesActivity.this, Profile.class));
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }*/
                                //old code end

                                // adapter = new MyQuotesAdapter(listItems,getApplicationContext());
                                adapter = new MyQuotesAdapter(listItems,getApplicationContext());
                                recyclerView.setAdapter(adapter);

                       /* adapter = new MyQuotesAdapter(MyQuotesActivity.this, QuoteList,
                                R.layout.myquotes_listitem, new String[]{"qid", "qdate"},
                                new int[]{R.id.tvQID, R.id.tvQDT});*/
                                //setListAdapter(adapter);
                                // recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w(TAG, "onErrorResponse: Error occurred");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    return params;
                }
            };

            MySingleton.getInstance(MyQuotesActivity.this).addToRequestQueue(stringRequest);
            Log.w(TAG, "onClick: Que added");

        }
        else {
            progressDialog.dismiss();
            Snackbar snackbar = Snackbar.make(recyclerView, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private boolean checkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            return false;
        }
    }

}
