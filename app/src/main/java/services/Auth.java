package services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Auth {
    private FirebaseAuth mAuth;
    private static Auth instance;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    private Auth() {
        mAuth = FirebaseAuth.getInstance();

    }

    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }


    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }


    public Task<AuthResult> login(String email, String password) {
       return  mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser = mAuth.getCurrentUser();
                    } else {
                        currentUser = null;
                    }
                });
    }
    public Task<AuthResult> signUp(String email, String password, String userName ) {
         return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser = mAuth.getCurrentUser();
                        Map<String, Object> user = new HashMap<>();
                        // add to firestore
                        user.put("email", email);
                        user.put("name", userName);
                        user.put("id", currentUser.getUid());
                        db.document("users/" + currentUser.getUid()).set(user);
//                        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                currentUser = mAuth.getCurrentUser();
////                                return null;
//                            }
//                        })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        currentUser = null;
//                                    }
//                                });
                        // save current user

                    } else {

                        // return error message to user
                        currentUser = null;
                    }
                });
    }



    private Context getApplicationContext() {
        return null;
    }


}
