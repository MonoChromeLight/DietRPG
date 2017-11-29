package edu.odu.cs.zomp.dietapp.ui.archive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;


public class QuestArchiveDialog extends DialogFragment {

    private static final String TAG = QuestArchiveDialog.class.getSimpleName();
    private static final String ARG_QUEST = "questProgress";

    @BindView(R.id.dialog_questArchive_root) CardView viewRoot;
    @BindView(R.id.dialog_questArchive_title) TextView title;
    @BindView(R.id.dialog_questArchive_description) TextView description;
    @BindView(R.id.dialog_questArchive_closeBtn) TextView closeBtn;

    private QuestProgress questProgress;

    public static QuestArchiveDialog with(@NonNull QuestProgress progress) {
        QuestArchiveDialog dialog = new QuestArchiveDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUEST, progress);
        dialog.setArguments(args);

        return dialog;
    }

    public QuestArchiveDialog() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_questarchive, container, false);
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
        closeBtn.setOnClickListener(view -> dismiss());
    }

    @Override public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override public void onDestroy() {
        questProgress = null;
        super.onDestroy();
    }
}
