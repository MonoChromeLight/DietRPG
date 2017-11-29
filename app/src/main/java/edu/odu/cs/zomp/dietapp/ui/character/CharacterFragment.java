package edu.odu.cs.zomp.dietapp.ui.character;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import edu.odu.cs.zomp.dietapp.util.Constants;


public class CharacterFragment extends BaseFragment {

    private static final String TAG = CharacterFragment.class.getSimpleName();

    @BindView(R.id.character_root) ScrollView viewRoot;
    @BindView(R.id.character_avatar) ImageView sprite;
    @BindView(R.id.character_name) TextView name;
    @BindView(R.id.character_level) TextView level;
    @BindView(R.id.character_class) TextView characterClass;
    @BindView(R.id.character_helmet) ImageView helmet;
    @BindView(R.id.character_body) ImageView body;
    @BindView(R.id.character_legs) ImageView legs;
    @BindView(R.id.character_weapon) ImageView weapon;
    @BindView(R.id.character_acc1) ImageView acc1;
    @BindView(R.id.character_acc2) ImageView acc2;
    @BindView(R.id.character_statList) RecyclerView attributeList;

    private Character character = null;

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    public CharacterFragment() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_character, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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


        // Set character base details
        name.setText(character.name);
        level.setText(String.format(Locale.US, "%d", character.stats.get("Level")));
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


        // Set equipment sprites
        View.OnClickListener equipmentClickListener = view -> Toast.makeText(getContext(), "No implemented yet", Toast.LENGTH_SHORT).show();
        helmet.setOnClickListener(equipmentClickListener);
        body.setOnClickListener(equipmentClickListener);
        legs.setOnClickListener(equipmentClickListener);
        weapon.setOnClickListener(equipmentClickListener);
        acc1.setOnClickListener(equipmentClickListener);
        acc2.setOnClickListener(equipmentClickListener);


        // Load stat list
        AttributeAdapter attributeAdapter = new AttributeAdapter(getContext(), character.attributes, character.getAugmentedAttributes());
        attributeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        attributeList.setHasFixedSize(true);
        attributeList.setAdapter(attributeAdapter);
    }
}
