package edu.odu.cs.zomp.dietapp.ui.character;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.ui.character.adapters.AttributeAdapter;
import edu.odu.cs.zomp.dietapp.ui.character.adapters.EquipmentAdapter;
import edu.odu.cs.zomp.dietapp.util.Constants;

public class CharacterFragment extends BaseFragment {

    private static final String TAG = CharacterFragment.class.getSimpleName();

    @BindView(R.id.character_root) ScrollView viewRoot;
    @BindView(R.id.character_avatar) ImageView sprite;
    @BindView(R.id.character_class) TextView characterClass;
    @BindView(R.id.character_level) TextView level;
    @BindView(R.id.character_equipment_grid) GridView equipmentGrid;
    @BindView(R.id.character_statList) ListView attributeList;

    private Character character = null;

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    public CharacterFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_character, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    character = documentSnapshot.toObject(Character.class);
                    initUI();
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e.fillInStackTrace()));
    }

    private void initUI() {
        // Load character sprite
        String spriteFilename = character.gender == Character.GENDER_MALE ? "character_male.png" : "character_female.png";
        try {
            InputStream imgStream = getContext().getAssets().open(spriteFilename);
            Drawable d = Drawable.createFromStream(imgStream, null);
            sprite.setImageDrawable(d);
        } catch (IOException e) {
            Log.e(TAG, "Error loading character sprite from assets", e.fillInStackTrace());
        }


        String className = null;
        switch (character.playerClass) {
            case Character.CLASS_WARRIOR:
                className = "Warrior";
                break;
            case Character.CLASS_MAGE:
                className = "Mage";
                break;
            case Character.CLASS_ROGUE:
                className = "Rogue";
                break;
        }
        characterClass.setText(className);
        level.setText(String.format(Locale.US, "%d", character.stats.get("Level")));

        // Load equipment set
        EquipmentAdapter equipmentAdapter = new EquipmentAdapter(getContext(), character.equipment);
        equipmentGrid.setAdapter(equipmentAdapter);
        equipmentGrid.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getContext(), "Position clicked: " + position, Toast.LENGTH_SHORT).show());

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(equipmentGrid.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < equipmentAdapter.getCount(); i++) {
            view = equipmentAdapter.getView(i, view, equipmentGrid);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = equipmentGrid.getLayoutParams();
        params.height = totalHeight;
        equipmentGrid.setLayoutParams(params);


        // Load stat list
        AttributeAdapter attributeAdapter = new AttributeAdapter(getContext(), character.attributes, character.getAugmentedAttributes());
        attributeList.setAdapter(attributeAdapter);
        // Hack to allow listView to extend full height under scrollview
        desiredWidth = View.MeasureSpec.makeMeasureSpec(attributeList.getWidth(), View.MeasureSpec.UNSPECIFIED);
        totalHeight = 0;
        view = null;
        for (int i = 0; i < attributeAdapter.getCount(); i++) {
            view = attributeAdapter.getView(i, view, attributeList);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        params = attributeList.getLayoutParams();
        params.height = totalHeight + (attributeList.getDividerHeight() * (attributeAdapter.getCount() - 1));
        attributeList.setLayoutParams(params);
    }
}
