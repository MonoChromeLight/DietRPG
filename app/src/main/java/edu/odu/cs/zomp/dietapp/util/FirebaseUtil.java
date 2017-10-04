package edu.odu.cs.zomp.dietapp.util;


import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.data.models.UserPublic;

public class FirebaseUtil {

    private static final String TAG = "FirebaseUtil";

    public static void initializeNewUser(FirebaseUser authUser, int gender, int playerClass) {
        if (authUser == null || (gender < 0 || gender > 1))
            return;

        String uid = authUser.getUid();

        // Create userData
        UserPrivate privateData = new UserPrivate(uid, "-KvVBIHEIbRB2hCpQ47n", 0);
        UserPublic publicData = new UserPublic(uid, authUser.getDisplayName());


        // Create character
        Character character = CharacterUtil.generateNewCharacter(uid, gender, playerClass);


        // Upload user data to database
        Map<String, Object> newUserUpdates = new HashMap<>();
        newUserUpdates.put("/" + Constants.DATABASE_PATH_USERS_PUBLIC + "/" + uid, publicData.toMap());
        newUserUpdates.put("/" + Constants.DATABASE_PATH_USERS_PRIVATE + "/" + uid, privateData.toMap());
        newUserUpdates.put("/" + Constants.DATABASE_PATH_CHARACTERS + "/" + uid, character.toMap());

        DatabaseReference databaseRoot = FirebaseDatabase.getInstance().getReference();
        databaseRoot.updateChildren(newUserUpdates, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d(TAG, "New user details created successfully");
            } else {
                Log.e(TAG, databaseError.getMessage(), databaseError.toException().fillInStackTrace());
            }
        });


        // Store user avatar in file storage
        if (authUser.getPhotoUrl() != null) {
            StorageReference userAvatarRef = FirebaseStorage.getInstance().getReference()
                    .child(Constants.STORAGE_PATH_USER_AVATARS)
                    .child(uid + Constants.FILE_EXT_PNG);

            UploadTask avatarUpload = userAvatarRef.putFile(authUser.getPhotoUrl());
            avatarUpload
                    .addOnFailureListener(exception -> Log.e(TAG, exception.getMessage(), exception.fillInStackTrace()))
                    .addOnSuccessListener(taskSnapshot -> Log.d(TAG, "Successfully uploaded user avatar"));
        } else {
            Log.d(TAG, "No user avatar to upload");
        }
    }
}
