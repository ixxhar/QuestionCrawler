package izharhussain.questioncrawler;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class ItemThree extends Fragment {
    private static final String TAG = "ItemThree";

    private View v;
    private TextView tvLoggedInUserName, tvLoggedInUserEmail;
    private ImageView ivUserImage;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    private DatabaseReference databaseReference;
    private UserModelClass userModelClass;


    private Transformation transformation;

    public static ItemThree newInstance() {
        ItemThree fragment = new ItemThree();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.itemthree, container, false);

        tvLoggedInUserName = (TextView) v.findViewById(R.id.textViewLoggedInUser);
        tvLoggedInUserEmail = (TextView) v.findViewById(R.id.textViewLoggedInEmail);
        ivUserImage = (ImageView) v.findViewById(R.id.imageViewLoggedInUser);

        transformationForPicasso();

        button = (Button) v.findViewById(R.id.buttonGoToMessages);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() != null) {
            tvLoggedInUserEmail.setVisibility(View.VISIBLE);
            tvLoggedInUserName.setVisibility(View.VISIBLE);
            tvLoggedInUserName.setText(firebaseAuth.getCurrentUser().getDisplayName());
            tvLoggedInUserEmail.setText(firebaseAuth.getCurrentUser().getEmail());

            storageReference.child(firebaseAuth.getCurrentUser().getEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    try {
                        Picasso.get().load(task.getResult()).transform(transformation).into(ivUserImage);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.image).transform(transformation).into(ivUserImage);
                    }
                }
            });

            if (storageReference != null && firebaseAuth != null) {
                databaseReference.child("RegisteredUsers").orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List list = new ArrayList<UserModelClass>();
                        for (DataSnapshot dSShot : dataSnapshot.getChildren()) {
                            list.add(dSShot.getValue(UserModelClass.class));
                        }
                        Log.d(TAG, "Records: for UserModelClass " + list.size());

                        for (int i = 0; i < list.size(); i++) {

                            userModelClass = (UserModelClass) list.get(i);

                            if (new String(userModelClass.getEmail()).equals(firebaseAuth.getCurrentUser().getEmail())) {
                                Log.d(TAG, "onDataChange: true!   " + userModelClass.toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            button.setText("Go to Messages");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), UsersForChatActivity.class);
                    if (userModelClass != null) {
                        intent.putExtra("LoggedInUser", userModelClass);
                        startActivity(intent);
                    }
                }
            });

            v.findViewById(R.id.buttonLogut).setVisibility(View.VISIBLE);
            v.findViewById(R.id.buttonLogut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDatabase.getInstance()
                            .getReference().
                            child("RegisteredUsers").
                            child(firebaseAuth.getUid()).
                            child("connection").
                            setValue(UsersChatAdapter.OFFLINE);

                    firebaseAuth.signOut();
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
            });
        } else {
            tvLoggedInUserEmail.setVisibility(View.INVISIBLE);
            tvLoggedInUserName.setVisibility(View.INVISIBLE);
            ivUserImage.setVisibility(View.INVISIBLE);
            v.findViewById(R.id.buttonLogut).setVisibility(View.INVISIBLE);
            button.setText("Sign In Now");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        }

        return v;
    }

    private void transformationForPicasso() {
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
    }
}