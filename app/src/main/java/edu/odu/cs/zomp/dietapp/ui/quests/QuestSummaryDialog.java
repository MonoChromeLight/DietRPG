package edu.odu.cs.zomp.dietapp.ui.quests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.QuestSummary;


public class QuestSummaryDialog extends DialogFragment {

    private static final String TAG = QuestSummaryDialog.class.getSimpleName();
    private static final String ARG_SUMMARY = "summary";

    @BindView(R.id.dialog_questSummary_root) CardView viewRoot;
    @BindView(R.id.dialog_questSummary_title) TextView title;
    @BindView(R.id.dialog_questSummary_expGained) TextView expGained;
    @BindView(R.id.dialog_questSummary_lvlUpIndicator) TextView levelUpIndicator;
    @BindView(R.id.dialog_questSummary_lootFrame) LinearLayout lootFrame;
    @BindView(R.id.dialog_questSummary_lootGained) TextView lootGained;
    @BindView(R.id.dialog_questSummary_questsUnlockedFrame) LinearLayout questsUnlockedFrame;
    @BindView(R.id.dialog_questSummary_questsUnlocked) TextView questsUnlocked;
    @BindView(R.id.dialog_questSummary_closeBtn) TextView closeBtn;

    private QuestSummary summary;

    public static QuestSummaryDialog with(@NonNull QuestSummary summary) {
        QuestSummaryDialog dialog = new QuestSummaryDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SUMMARY, summary);
        dialog.setArguments(args);

        return dialog;
    }

    public QuestSummaryDialog() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_questsummary, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onStart() {
        super.onStart();
        summary = getArguments().getParcelable(ARG_SUMMARY);
        if (summary == null)
            return;

        // Set experience
        expGained.setText(String.format(Locale.US, "%d", summary.expGained));
        if (summary.userLeveledUp)
            levelUpIndicator.setVisibility(View.VISIBLE);
        else
            levelUpIndicator.setVisibility(View.GONE);

        // Set loot
        if (summary.loot == null || summary.loot.size() == 0) {
            lootFrame.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(summary.loot.get(0));
            for (int i = 1; i < summary.loot.size(); i++) {
                sb.append("\n");
                sb.append(summary.loot.get(i));
            }

            lootGained.setText(sb.toString());
        }

        // Set new quests
        if (summary.questsUnlocked == null || summary.questsUnlocked.size() == 0) {
            questsUnlockedFrame.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(summary.questsUnlocked.get(0));
            for (int i = 1; i < summary.questsUnlocked.size(); i++) {
                sb.append("\n");
                sb.append(summary.questsUnlocked.get(i));
            }

            questsUnlocked.setText(sb.toString());
        }
        
        closeBtn.setOnClickListener(view -> dismiss());
    }

    @Override public void onDestroy() {
        summary = null;
        super.onDestroy();
    }
}
