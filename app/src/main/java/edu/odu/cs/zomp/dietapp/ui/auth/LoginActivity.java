package edu.odu.cs.zomp.dietapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;
import edu.odu.cs.zomp.dietapp.ui.MainActivity;
import edu.odu.cs.zomp.dietapp.ui.onboarding.OnboardingActivity;


public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth auth = null;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override protected void onStart() {
        super.onStart();
        Log.d(TAG, "Checking for user");

        // DEBUG: For some reason user account data are not getting cleared between app reinstalls
//        AuthUI.getInstance().signOut(this);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "User " + auth.getCurrentUser().getUid() + " logged in");
            checkUserProfile();
        } else {
            Log.d(TAG, "Starting auth for result");
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.mipmap.ic_launcher)
                            .setTheme(R.style.AppTheme)
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Returned from login");
        checkUserProfile();
    }

    private void checkUserProfile() {
        Log.d(TAG, "Checking user profile");
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        if (authUser != null) {
            FirebaseFirestore.getInstance()
                    .collection("characters")
                    .document(authUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                startActivity(MainActivity.createIntent(LoginActivity.this));
                                finish();
                            } else {
                                startActivity(OnboardingActivity.createIntent(LoginActivity.this));
                                finish();
                            }
                        } else {
                            Log.e(TAG,
                                    task.getException().getMessage(),
                                    task.getException().fillInStackTrace());
                        }
                    });
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
