package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.MessengerAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.MessengerInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-05-02.
 */
public class MessengerActivity extends CustomActionBarActivity {

    private ListView lvMessenger;
    private MessengerAdapter adapter;
    private ArrayList<MessengerInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massenger);

        lvMessenger = (ListView)findViewById(R.id.lvMessenger);
        arrayList = new ArrayList<MessengerInfo>();

        MessengerInfo bci = new MessengerInfo();
        bci.setName("PARK JEHYUN");
        bci.setDate("05.30 10:30AM");
        bci.setMessage("Lets meet at 12:00");
        arrayList.add(bci);

        bci = new MessengerInfo();
        bci.setName("KIM HEEJOONG");
        bci.setDate("05.29 12:00AM");
        bci.setMessage("Meet at front door now");
        arrayList.add(bci);

        adapter = new MessengerAdapter(this, R.layout.list_row_messenger, arrayList);
        lvMessenger.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvMessenger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String name = vh.tvMessengerName.getText().toString();
                String date = vh.tvMessengerDate.getText().toString();
                String message = vh.tvMessengerMessage.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("userName", name);
                intent.putExtra("userSeq", "null");
                startActivity(intent);

                //start Activity about sponser clicked
                Toast.makeText(getApplicationContext(), name + ", " + date + " clicked()", Toast.LENGTH_SHORT).show();
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
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}