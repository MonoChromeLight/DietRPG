package edu.odu.cs.zomp.dietapp.ui.quests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;

import static android.app.Activity.RESULT_OK;


public class QuestInfoDialog extends DialogFragment {

    private static final String TAG = QuestInfoDialog.class.getSimpleName();
    private static final String ARG_QUEST = "questProgress";

    @BindView(R.id.dialog_questInfo_root) CardView viewRoot;
    @BindView(R.id.dialog_questInfo_title) TextView title;
    @BindView(R.id.dialog_questInfo_progressInfo) TextView currentProgressIndicator;
    @BindView(R.id.dialog_questInfo_description) TextView description;
    @BindView(R.id.dialog_questInfo_cancelBtn) TextView cancelBtn;
    @BindView(R.id.dialog_questInfo_startBtn) TextView startBtn;

    private QuestProgress questProgress;

    public static QuestInfoDialog buildDialog(QuestProgress progress) {
        QuestInfoDialog dialog = new QuestInfoDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUEST, progress);
        dialog.setArguments(args);

        return dialog;
    }

    public QuestInfoDialog() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_questinfo, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onStart() {
        super.onStart();
        questProgress = getArguments().getParcelable(ARG_QUEST);
        if (questProgress == null)
            return;

        title.setText(questProgress.questName);
        description.setText(questProgress.questDescription);
        currentProgressIndicator.setText(
                String.format(Locale.US, "%d / %d", questProgress.currentSegment, questProgress.totalSegments) );

        cancelBtn.setOnClickListener(view -> dismiss());
        startBtn.setOnClickListener(view -> {
            Intent dataIntent = new Intent();
            dataIntent.putExtra("questInfo", questProgress);
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, dataIntent);
            dismiss();
        });
    }

    @Override public void onDestroy() {
        questProgress = null;
        super.onDestroy();
    }
}
