package edu.odu.cs.zomp.dietapp.ui.profile;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Diet;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.data.models.UserPublic;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;


public class ProfileFragment extends BaseFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.view_profile_avatar) ImageView avatar;
    @BindView(R.id.view_profile_username) TextView username;
    @BindView(R.id.view_profile_dietCard) CardView dietCard;
    @BindView(R.id.view_profile_dietCard_dietName) TextView activeDietBtn;
//    @BindView(R.id.view_profile_strengthScore) TextView strengthScore;
//    @BindView(R.id.view_profile_dexterityScore) TextView dexterityScore;
//    @BindView(R.id.view_profile_willpowerScore) TextView willpowerScore;
//    @BindView(R.id.view_profile_agilityScore) TextView agilityScore;
//    @BindView(R.id.view_profile_luckScore) TextView luckScore;

    private UserPublic publicUserData = null;
    private UserPrivate privateUserData = null;
    private Diet activeDiet = null;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load user data
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        if (authUser != null) {
            String uid = authUser.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference publicUserRef = database.getReference()
                    .child("users-public")
                    .child(uid);
            publicUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {
                    publicUserData = dataSnapshot.getValue(UserPublic.class);
                    setUIPublicData();
                }

                @Override public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage(), databaseError.toException().fillInStackTrace());
                }
            });


            DatabaseReference privateUserRef = database.getReference()
                    .child("users-private")
                    .child(uid);
            privateUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {
                    privateUserData = dataSnapshot.getValue(UserPrivate.class);
                    setUIPrivateData();
                }

                @Override public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage(), databaseError.toException().fillInStackTrace());
                }
            });
        }

        username.setText("Krombopulos");

        try {
            InputStream imgStream = getContext().getAssets().open("sprite_char.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            avatar.setImageDrawable(bgImg);
        } catch (IOException e) {
            Log.e("ProfileFragment", e.getMessage(), e.fillInStackTrace());
        }

        dietCard.setOnClickListener(view1 -> startActivity(DietPickerActivity.createIntent(getContext())));
    }

    private void setUIPublicData() {
        username.setText(publicUserData.name);
    }

    private void setUIPrivateData() {

    }

    /**
     * Set profile details that are not saved in Firebase
     */
    void setUI() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        activeDiet = new Diet(
                prefs.getString(getString(R.string.sharedPref_dietId), null),
                prefs.getString(getString(R.string.sharedPref_dietTitle), null));
        // Admittedly a lazy hack but if shared preference is null initially, display "No Filter"
        // This should be fixed to line up with the actual no filter diet object
        if (activeDiet.title == null)
            activeDiet.title = "No Filter";

        activeDietBtn.setText(activeDiet.title);
    }
}
