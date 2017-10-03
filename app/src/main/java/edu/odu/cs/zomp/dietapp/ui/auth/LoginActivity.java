package edu.odu.cs.zomp.dietapp.ui.auth;

import android.content.Intent;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;
import edu.odu.cs.zomp.dietapp.ui.MainActivity;
import edu.odu.cs.zomp.dietapp.ui.onboarding.OnboardingActivity;


public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;

    @Override protected void onStart() {
        super.onStart();
        updateDatabase();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(MainActivity.createIntent(this));
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.mipmap.ic_launcher)
                            .setTheme(R.style.AppTheme)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
                if (authUser != null) checkUserProfile(authUser);
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show();
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkUserProfile(FirebaseUser authUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users-private")
                .child(authUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                UserPrivate privateInfo = dataSnapshot.getValue(UserPrivate.class);
                if (privateInfo == null) {
                    startActivity(OnboardingActivity.createIntent(LoginActivity.this));
                } else {
                    startActivity(MainActivity.createIntent(LoginActivity.this));
                    finish();
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDatabase() {

    }
}
