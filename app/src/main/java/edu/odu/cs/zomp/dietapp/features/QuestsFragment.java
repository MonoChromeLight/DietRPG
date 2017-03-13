package edu.odu.cs.zomp.dietapp.features;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;

public class QuestsFragment extends BaseFragment {

    @BindView(R.id.view_quests_root) LinearLayout viewRoot;
    @BindView(R.id.view_quests_headerImg) ImageView headerImg;
    @BindView(R.id.view_quests_emptyQuestListText) TextView emptyQuestListText;

    public static QuestsFragment newInstance() {
        return new QuestsFragment();
    }

    public QuestsFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_quests, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyQuestListText.setVisibility(View.VISIBLE);

        try {
            InputStream imgStream = getContext().getAssets().open("quest_header_img.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            headerImg.setImageDrawable(bgImg);
            headerImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            Log.e("QuestsFragment", e.getMessage(), e.fillInStackTrace());
        }
    }
}
