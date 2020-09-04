package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> wUsers;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_users);

        final ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        pullToRefresh = findViewById(R.id.swipeContainer);


        wUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, wUsers);



        try {


            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {

                        for (ParseUser twitterUsers : objects) {

                            wUsers.add(twitterUsers.getUsername());
                        }
                        listView.setAdapter(adapter);

                    }
                }
            });


        } catch (Exception e) {


            e.getMessage();
        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.whereNotContainedIn("username", wUsers);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {

                                for (ParseUser twitterUsers : objects) {

                                    wUsers.add(twitterUsers.getUsername());
                                }
                                adapter.notifyDataSetChanged();

                            }
                        }
                    });


                } catch (Exception e) {


                    e.getMessage();
                }
                pullToRefresh.setRefreshing(false);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout_item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Intent intent = new Intent(WhatsAppUsers.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(WhatsAppUsers.this, WhatsAppChatActivity.class);
        intent.putExtra("selectedUser", wUsers.get(i));
        startActivity(intent);
    }
}
