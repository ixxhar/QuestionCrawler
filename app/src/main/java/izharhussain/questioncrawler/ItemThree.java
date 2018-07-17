package izharhussain.questioncrawler;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ItemThree extends Fragment {
    private View v;
    private TextView tvLoggedInUserName, tvLoggedInUserEmail;
    private ImageView ivUserImage;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    public static ItemThree newInstance() {
        ItemThree fragment = new ItemThree();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.itemthree, container, false);
        tvLoggedInUserName = (TextView) v.findViewById(R.id.textViewLoggedInUser);
        tvLoggedInUserEmail = (TextView) v.findViewById(R.id.textViewLoggedInEmail);
        ivUserImage = (ImageView) v.findViewById(R.id.imageViewLoggedInUser);

        button = (Button) v.findViewById(R.id.buttonGoToMessages);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() != null) {
            tvLoggedInUserEmail.setVisibility(View.VISIBLE);
            tvLoggedInUserName.setVisibility(View.VISIBLE);
            tvLoggedInUserName.setText(firebaseAuth.getCurrentUser().getDisplayName());
            tvLoggedInUserEmail.setText(firebaseAuth.getCurrentUser().getEmail());

            storageReference.child(firebaseAuth.getCurrentUser().getEmail()).child("profilephoto").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    try {
                        Picasso.get().load(task.getResult()).into(ivUserImage);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.image).into(ivUserImage);
                    }
                }
            });


            button.setText("Go to Messages");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), UsersForChatActivity.class));
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
}