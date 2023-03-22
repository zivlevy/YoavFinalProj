package services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private FirebaseAuth mAuth;
    private int numEntered = 11;
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

    public int getNum() {
        return numEntered;
    }

    public void doubleIt() {
        numEntered = numEntered * 2;
    }

    public void subtract1() {
        numEntered = numEntered - 1;
    }

}
