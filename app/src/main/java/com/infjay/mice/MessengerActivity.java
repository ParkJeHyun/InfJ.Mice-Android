package com.infjay.mice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.MessengerAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.MessageInfo;
import com.infjay.mice.artifacts.MessengerInfo;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-05-02.
 */
public class MessengerActivity extends CustomActionBarActivity {

    private ListView lvMessenger;
    private MessengerAdapter adapter;
    private ArrayList<MessageInfo> arrayList;

    private String mySeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massenger);

        lvMessenger = (ListView)findViewById(R.id.lvMessenger);
        arrayList = new ArrayList<MessageInfo>();
        mySeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mySeq == null)
        {
            Toast.makeText(getApplicationContext(), "User Sequence Error", Toast.LENGTH_SHORT).show();
            return;
        }
        arrayList = DBManager.getManager(getApplicationContext()).getRecentMessageList(mySeq);
        adapter = new MessengerAdapter(this, R.layout.list_row_messenger, arrayList);
        lvMessenger.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvMessenger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String userName = vh.tvMessengerName.getText().toString();
                String userSeq = vh.targetSeq;

                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userSeq", userSeq);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messenger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newMessage) {
            Intent intent = new Intent(getApplicationContext(),
                    FindReceiverActivity.class);
            intent.putExtra("from","Messenger");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}