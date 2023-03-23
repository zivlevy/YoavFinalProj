package services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private FirebaseAuth mAuth;
    private static Auth instance;
    FirebaseUser currentUser;


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


    public Task<AuthResult> signIn(String email, String password) {
       return  mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser = mAuth.getCurrentUser();
                    } else {
                        currentUser = null;
                    }
                });
    }
    public Task<AuthResult> signUp(String email, String password, String userName , String phoneNumber ) {

        // sign up with email and password
        return  mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // add to firestore

                        // save current user
                        currentUser = mAuth.getCurrentUser();
                    } else {

                        // return error message to user
                        currentUser = null;
                    }
                });
    }


}
