package izharhussain.questioncrawler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsersForChatActivity extends AppCompatActivity {
    private static final String TAG = "UsersForChatActivity";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ChildEventListener childEventListener;
    private UsersChatAdapter usersChatAdapter;
    private String currentUserUID;
    private List<String> usersKeyList;
    private RecyclerView recyclerView;
    private android.widget.SearchView svSearchNearestUsers;
    private String cityToBeSearhed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersforchat);

        firebaseReferences();
        setUserRecyclerView();
        setUsersKeyList();
        setAuthListener();
        forSearchView();
        forClearingChats();
    }

    private void forClearingChats() {
        findViewById(R.id.imageButtonForClearingChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersChatAdapter.clear();
                usersKeyList.clear();
                recyclerView = null;
                setUserRecyclerView();
            }
        });
    }

    private void forSearchView() {
        svSearchNearestUsers = (android.widget.SearchView) findViewById(R.id.searchViewNearestUsers);
        svSearchNearestUsers.setQueryHint("User's Name");

        svSearchNearestUsers.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharSequence query1 = svSearchNearestUsers.getQuery();
                if (query != null && TextUtils.getTrimmedLength(query) > 0) {
                    cityToBeSearhed = String.valueOf(query1);
                    queryAllUsers();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void firebaseReferences() {
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d(TAG, "firebaseReferences: " + firebaseAuth.getCurrentUser().getEmail());
    }

    private void setUserRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListOfUserForChat);
        usersChatAdapter = new UsersChatAdapter(this, new ArrayList<UserModelClass>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(usersChatAdapter);
    }

    private void setUsersKeyList() {
        usersKeyList = new ArrayList<String>();
    }

    private void setAuthListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    setUserData(user);
                } else {
                    // User is signed out
                    goToLogin();
                }
            }
        };
    }

    private void setUserData(FirebaseUser user) {
        currentUserUID = user.getUid();
    }

    private void queryAllUsers() {
        childEventListener = getChildEventListener();
        databaseReference.orderByChild("name").equalTo(cityToBeSearhed).addChildEventListener(childEventListener);
        //databaseReference.limitToFirst(50).addChildEventListener(childEventListener);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        clearCurrentUsers();

        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

    private void clearCurrentUsers() {
        usersChatAdapter.clear();
        usersKeyList.clear();
    }

    private void logout() {
        setUserOffline();
        firebaseAuth.signOut();
    }

    private void setUserOffline() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child(userId).child("connection").setValue(UsersChatAdapter.OFFLINE);
        }
    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {

                    String userUid = dataSnapshot.getKey();

                    if (dataSnapshot.getKey().equals(currentUserUID)) {
                        UserModelClass currentUser = dataSnapshot.getValue(UserModelClass.class);
                        usersChatAdapter.setCurrentUserInfo(userUid, currentUser.getEmail(), currentUser.getCreatedAt());
                    } else {
                        UserModelClass recipient = dataSnapshot.getValue(UserModelClass.class);
                        recipient.setRecipientID(userUid);
                        usersKeyList.add(userUid);
                        usersChatAdapter.refill(recipient);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if (!userUid.equals(currentUserUID)) {

                        UserModelClass user = dataSnapshot.getValue(UserModelClass.class);

                        int index = usersKeyList.indexOf(userUid);
                        if (index > -1) {
                            usersChatAdapter.changeUser(index, user);
                        }
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
