package edu.odu.cs.zomp.dietapp.features.profile;

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

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;


public class ProfileFragment extends BaseFragment {

    @BindView(R.id.view_profile_avatar) ImageView avatar;
    @BindView(R.id.view_profile_username) TextView username;
    @BindView(R.id.view_profile_dietCard) CardView dietCard;
    @BindView(R.id.view_profile_dietCard_dietName) TextView currentDiet;
    @BindView(R.id.view_profile_strengthScore) TextView strengthScore;
    @BindView(R.id.view_profile_dexterityScore) TextView dexterityScore;
    @BindView(R.id.view_profile_willpowerScore) TextView willpowerScore;
    @BindView(R.id.view_profile_agilityScore) TextView agilityScore;
    @BindView(R.id.view_profile_luckScore) TextView luckScore;

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

        username.setText("Krombopulos");

        try {
            InputStream imgStream = getContext().getAssets().open("sprite_char.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            avatar.setImageDrawable(bgImg);
        } catch (IOException e) {
            Log.e("ProfileFragment", e.getMessage(), e.fillInStackTrace());
        }

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
                .getString("currentDiet", "No Filter");

        currentDiet.setText(dietName);
    }
}
