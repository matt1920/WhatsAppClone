package com.example.whatsappclone;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_whatsapp_chat);
        super.onCreate(savedInstanceState);

        selectedUser = getIntent().getStringExtra("selectedUser");

        findViewById(R.id.btnChat).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);
          try {
              ParseQuery<ParseObject> firstUserParseQuery = ParseQuery.getQuery("Chat");
              ParseQuery<ParseObject> secondUserParseQuery = ParseQuery.getQuery("Chat");

              firstUserParseQuery.whereEqualTo("wSender", ParseUser.getCurrentUser().getUsername());
              firstUserParseQuery.whereEqualTo("wReceiver", selectedUser);

              secondUserParseQuery.whereEqualTo("wSender", selectedUser);
              secondUserParseQuery.whereEqualTo("wReceiver", ParseUser.getCurrentUser().getUsername());

              ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
              allQueries.add(firstUserParseQuery);
              allQueries.add(secondUserParseQuery);

              ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
              myQuery.orderByAscending("createdAt");

              myQuery.findInBackground(new FindCallback<ParseObject>() {
                  @Override
                  public void done(List<ParseObject> objects, ParseException e) {
                      if (objects.size() > 0 && e == null) {

                          for (ParseObject chatObject : objects) {

                              String wMessage = chatObject.get("wMessage") + "";

                              if (chatObject.get("wSender").equals(ParseUser.getCurrentUser().getUsername())) {

                                  wMessage = ParseUser.getCurrentUser().getUsername() + ": " + wMessage;
                              }
                              if (chatObject.get("wSender").equals(selectedUser)) {

                                  wMessage = selectedUser + ": " + wMessage;
                              }
                              chatsList.add(wMessage);
                          }
                          adapter.notifyDataSetChanged();
                      }
                  }
              });


          } catch (Exception e){

              e.printStackTrace();
          }
    }

    @Override
    public void onClick(View view) {

        final EditText edtMessage = findViewById(R.id.edtChat);

        ParseObject chat = new ParseObject("Chat");
        chat.put("wSender", ParseUser.getCurrentUser().getUsername());
        chat.put("wReceiver", selectedUser);
        chat.put("wMessage", edtMessage.getText().toString());

        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){

                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }


            }
        });

    }
}
