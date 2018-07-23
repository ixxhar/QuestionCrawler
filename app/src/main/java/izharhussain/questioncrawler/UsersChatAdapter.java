package izharhussain.questioncrawler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolderUsers> {

    private static final String TAG = "UsersChatAdapter";

    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";

    private List<UserModelClass> userModelClassList;
    private Context context;
    private String currentUserEmail;
    private Long currentUserCreatedAt;
    private String currentUserID;

    public UsersChatAdapter(Context context, List<UserModelClass> userModelClassList) {
        this.userModelClassList = userModelClassList;
        this.context = context;
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsers(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderUsers holder, int position) {

        UserModelClass userModelClass = userModelClassList.get(position);

        holder.tvUserName.setText(userModelClass.getName());
        holder.tvConnectionStatus.setText(userModelClass.getConnection());

        if (userModelClass.getConnection().equals(ONLINE)) {
            // Green color
            holder.tvConnectionStatus.setTextColor(Color.parseColor("#00FF00"));
        } else {
            // Red color
            holder.tvConnectionStatus.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return userModelClassList.size();
    }

    public void refill(UserModelClass userModelClass) {
        userModelClassList.add(userModelClass);
        notifyDataSetChanged();
    }

    public void changeUser(int index, UserModelClass userModelClass) {
        userModelClassList.set(index, userModelClass);
        notifyDataSetChanged();
    }

    public void setCurrentUserInfo(String userUID, String email, long createdAt) {
        currentUserEmail = email;
        currentUserCreatedAt = createdAt;
        currentUserID = userUID;

    }

    public void clear() {
        userModelClassList.clear();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUserName, tvConnectionStatus;
        private Context contexttt;

        public ViewHolderUsers(Context context, View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.textViewDisplayName);
            tvConnectionStatus = (TextView) itemView.findViewById(R.id.textViewConnectionStatus);
            contexttt = context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserModelClass userModelClass = userModelClassList.get(getLayoutPosition());

            Log.d(TAG, "onClick currentUserCreatedAt: " + currentUserCreatedAt);
            Log.d(TAG, "onClick: userModelClass.getCreatedAt() " + userModelClass.getCreatedAt());

            try {
                String uniqueChatRef = "";
                if (currentUserCreatedAt > userModelClass.getCreatedAt()) {
                    uniqueChatRef = cleanEmailAddress(currentUserEmail) + "-" + cleanEmailAddress(userModelClass.getEmail());
                } else {

                    uniqueChatRef = cleanEmailAddress(userModelClass.getEmail()) + "-" + cleanEmailAddress(currentUserEmail);
                }


                Intent chatIntent = new Intent(contexttt, ChatActivity.class);
                chatIntent.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID, currentUserID);
                chatIntent.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, userModelClass.getRecipientID());
                chatIntent.putExtra(ExtraIntent.EXTRA_CHAT_REF, uniqueChatRef);

                // Start new activity
                contexttt.startActivity(chatIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private String cleanEmailAddress(String email) {
            //replace dot with comma since firebase does not allow dot
            return email.replace(".", "-");
        }

    }
}
