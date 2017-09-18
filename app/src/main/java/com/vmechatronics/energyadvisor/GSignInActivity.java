package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class GSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAnalytics mFirebaseAnalytics;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    HashMap<String,String> user;

    UserSessionManager session;

    String email;
    String pass;
    String name;
    String phone;
    String state;
//    String phone;
    String pho;
    private String checkGoogleAcc_url = "https://vmechatronics.com/app/checkGplusAcc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("Islogin", false).commit();

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/

        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
// [END on_stop_remove_listener]

   /* @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        //hideProgressDialog();
       // mProgressDialog.dismiss();
    } */

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        // if (requestCode ==  RC_SIGN_IN && resultCode== RESULT_OK) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
                name = acct.getDisplayName();
                email = acct.getEmail();
                Log.e(TAG, "Name: " + name + ", email: " + email);
                Toast.makeText(GSignInActivity.this, "Signed in as:"+name , Toast.LENGTH_LONG).show();
                //name = acct.getDisplayName();
                // email = acct.getEmail();
                firebaseAuthWithGoogle(acct);
                //handleSignInResult(result);

               /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            jsonResponse = array.getJSONObject(0);
                            String success = jsonResponse.getString("success");
                            if (success.equals("Email and phone exists")) {
                                Intent pro = new Intent(GSignInActivity.this, Profile.class);
                                phone = jsonResponse.getString("phone");
                                state = jsonResponse.getString("state");
                                session = new UserSessionManager(getApplicationContext());
                                session.createGLoginSession(name, email, phone, state);
                                //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GSignInActivity.this);
                                //Boolean deatilsinDB = prefs.getBoolean("deatilsinDB", true);
                                //prefs.edit().putBoolean("deatilsinDB", true).commit();

                                startActivity(pro);
                                finish();

                            } else if (success.equals("Contact number doesnot exists") || success.equals("No such email and phone")) {
                                Bundle b = new Bundle();
                                b.putString("name", name);
                                b.putString("email", email);
                                Intent ph = new Intent(GSignInActivity.this, Getph.class);
                                ph.putExtras(b);
                                startActivity(ph);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CheckGAcc cgacc = new CheckGAcc(email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GSignInActivity.this);
                queue.add(cgacc);*/

                StringRequest stringRequest = new StringRequest(Request.Method.POST, checkGoogleAcc_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);
                                    JSONObject jsonResponse = array.getJSONObject(0);
                                    String success = jsonResponse.getString("success");
                                    Log.w(TAG, "onResponse: jsonresponse" + success);
                                    if (success.equals("Email and phone exists")) {
                                        Intent pro = new Intent(GSignInActivity.this, Profile.class);
                                        phone = jsonResponse.getString("phone");
                                        state = jsonResponse.getString("state");
                                        session = new UserSessionManager(getApplicationContext());
                                        session.createGLoginSession(name, email, phone, state);
                                        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GSignInActivity.this);
                                        //Boolean deatilsinDB = prefs.getBoolean("deatilsinDB", true);
                                        //prefs.edit().putBoolean("deatilsinDB", true).commit();
                                        startActivity(pro);
                                        finish();
                                    } else if (success.equals("Contact number doesnot exists") || success.equals("No such email and phone")) {
                                        Bundle b = new Bundle();
                                        b.putString("name", name);
                                        b.putString("email", email);
                                        Intent ph = new Intent(GSignInActivity.this, Getph.class);
                                        ph.putExtras(b);
                                        startActivity(ph);
                                        finish();
                                    }
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
                MySingleton.getInstance(GSignInActivity.this).addToRequestQueue(stringRequest);
            }
            else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                Toast.makeText(GSignInActivity.this, "Try Again!", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        }
    }
            //String personGivenName = acct.getGivenName();
            // String personFamilyName = acct.getFamilyName();
           // String personId = acct.getId();
           // Uri personPhoto = acct.getPhotoUrl();
    // [END onActivityResult]


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
// [END auth_with_google]


   /* // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            name = acct.getDisplayName();
            email = acct.getEmail();
            Log.e(TAG, "Name: " + name + ", email: " + email);
            Toast.makeText(GSignInActivity.this, "Signed in as:"+name , Toast.LENGTH_LONG).show();

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]
    */

    // [START signIn]//if(resultCode == 2)
           // pho = data.getStringExtra("pho");
           //Intent intent = getIntent();
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //gp=true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("IsGp", true).commit();
        prefs.edit().putBoolean("IsEditPro", false).commit();
        prefs.edit().putBoolean("Islogin", true).commit();

       /* GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount acct = result.getSignInAccount();
        String personName = acct.getDisplayName();
        String personGivenName = acct.getGivenName();
        String personFamilyName = acct.getFamilyName();
        String personEmail = acct.getEmail();
        String personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();
        */
    }
    // [END signIn]

    // [START signOut]
    public void signOut() {

        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(null);

                        // [END_EXCLUDE]
                    }
                });
        //Islogin = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("Islogin", false).commit();
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            /*case R.id.disconnect_button:
                revokeAccess();
                break;*/
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Google_SignIn", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}