package edu.odu.cs.zomp.dietapp.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Diet;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.data.models.UserPublic;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.util.Constants;


public class ProfileFragment extends BaseFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.profile_avatar) CircleImageView avatar;
    @BindView(R.id.profile_username) TextView username;
    @BindView(R.id.profile_dietCard) CardView dietCard;
    @BindView(R.id.profile_goldAmount) TextView goldIndicator;
    @BindView(R.id.profile_dietCard_dietName) TextView activeDietBtn;

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

            // User public data
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference publicUserRef = database.getReference()
                    .child(Constants.DATABASE_PATH_USERS_PUBLIC)
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

            // User private data
            DatabaseReference privateUserRef = database.getReference()
                    .child(Constants.DATABASE_PATH_USERS_PRIVATE)
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

            // User Avatar
            StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_USER_AVATARS)
                    .child(uid + Constants.FILE_EXT_PNG);
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(avatarRef)
                    .error(R.drawable.avatar)
                    .into(avatar);
        }

        setUI();
        dietCard.setOnClickListener(view1 -> startActivity(DietPickerActivity.createIntent(getContext())));
    }

    private void setUIPublicData() {
//        username.setText(publicUserData.name);
    }

    private void setUIPrivateData() {
//        goldIndicator.setText(privateUserData.gold);
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
