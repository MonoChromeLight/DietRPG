package edu.odu.cs.zomp.dietapp.features.profile;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.features.BaseFragment;


public class ProfileFragment extends BaseFragment {

    @BindView(R.id.view_profile_avatar) CircleImageView avatar;
    @BindView(R.id.view_profile_username) TextView username;
    @BindView(R.id.view_profile_dietCard) CardView dietCard;
    @BindView(R.id.view_profile_dietCard_dietName) TextView currentDiet;

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

        dietCard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(DietPickerActivity.createIntent(getContext()));
            }
        });
    }

    @Override public void onResume() {
        super.onResume();
        updateUI();
    }

    void updateUI() {
        username.setText("David");

        String dietName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString("currentDiet", "Error");

        currentDiet.setText(dietName);
    }
}
