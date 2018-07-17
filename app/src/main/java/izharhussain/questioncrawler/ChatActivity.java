package izharhussain.questioncrawler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etUserMessageChatText;

    private String recipientId;
    private String currentUserId;
    private MessageChatAdapter messageChatAdapter;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etUserMessageChatText = (EditText) findViewById(R.id.editTextMessage);
        findViewById(R.id.buttonSendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderMessage = etUserMessageChatText.getText().toString().trim();

                if (!senderMessage.isEmpty()) {

                    ChatMessage newMessage = new ChatMessage(senderMessage, currentUserId, recipientId);
                    databaseReference.push().setValue(newMessage);

                    etUserMessageChatText.setText("");
                }
            }
        });


        setDatabaseInstance();
        setUsersId();
        setChatRecyclerView();

    }

    private void setDatabaseInstance() {
        String chatRef = getIntent().getStringExtra(ExtraIntent.EXTRA_CHAT_REF);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(chatRef);
    }

    private void setUsersId() {
        recipientId = getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
        currentUserId = getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
    }

    private void setChatRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        messageChatAdapter = new MessageChatAdapter(new ArrayList<ChatMessage>());
        recyclerView.setAdapter(messageChatAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        childEventListener = databaseReference.limitToFirst(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                if (dataSnapshot.exists()) {
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
                    if (newMessage.getSender().equals(currentUserId)) {
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.SENDER);
                    } else {
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.RECIPIENT);
                    }
                    messageChatAdapter.refillAdapter(newMessage);
                    recyclerView.scrollToPosition(messageChatAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
        messageChatAdapter.cleanUp();

    }
}
