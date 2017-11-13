package edu.odu.cs.zomp.dietapp.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.odu.cs.zomp.dietapp.GlideApp;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.data.models.UserPublic;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.ui.auth.LoginActivity;
import edu.odu.cs.zomp.dietapp.util.Constants;


public class ProfileFragment extends BaseFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.profile_avatar) CircleImageView avatar;
    @BindView(R.id.profile_username) TextView username;
    @BindView(R.id.profile_dietCard) CardView dietCard;
    @BindView(R.id.profile_goldAmount) TextView goldIndicator;
    @BindView(R.id.profile_dietCard_dietName) TextView activeDietBtn;
    @BindView(R.id.profile_logout_btn) Button logoutBtn;

    private UserPublic publicUserData = null;
    private UserPrivate privateUserData = null;

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
        dietCard.setOnClickListener(view1 -> startActivity(DietPickerActivity.createIntent(getContext())));
    }

    @Override public void onResume() {
        super.onResume();
        // Load user data
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        if (authUser != null) {
            String uid = authUser.getUid();

            // User public data
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore
                    .collection(Constants.DATABASE_PATH_USERS_PUBLIC)
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        publicUserData = documentSnapshot.toObject(UserPublic.class);
                        username.setText(publicUserData.name);
                    });

            firestore
                    .collection(Constants.DATABASE_PATH_USERS_PRIVATE)
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        privateUserData = documentSnapshot.toObject(UserPrivate.class);
                        activeDietBtn.setText(privateUserData.activeDiet.title);
                        goldIndicator.setText(String.format(Locale.US, "%d", privateUserData.gold));
                    });

            // User Avatar
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user.getPhotoUrl() != null) {
                GlideApp.with(getContext())
                        .load(user.getPhotoUrl())
                        .into(avatar);
            }
        }
    }

    @Override public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.profile_logout_btn)
    void logout() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnSuccessListener(aVoid -> startActivity(LoginActivity.createIntent(getContext())))
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e.fillInStackTrace()));
    }
}
