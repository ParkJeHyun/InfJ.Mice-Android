package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.MemoAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.MemoInfo;

import java.util.ArrayList;


public class MemoActivity extends ActionBarActivity {

    Button btnAddMemo;

    private ListView lvMemoList;

    private MemoAdapter adapter;
    private MemoInfo mInfo;
    private ArrayList<MemoInfo> memoArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        btnAddMemo = (Button)findViewById(R.id.btnAddMemo);
        btnAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoNewActivity.class);
                startActivity(intent);
            }
        });

        lvMemoList = (ListView)findViewById(R.id.lvMemoList);

        memoArrayList = new ArrayList<MemoInfo>();

        mInfo = new MemoInfo();
        mInfo.memoTitle = "메모제목1";
        memoArrayList.add(mInfo);
        mInfo = new MemoInfo();
        mInfo.memoTitle = "메모제목2";
        memoArrayList.add(mInfo);
        mInfo = new MemoInfo();
        mInfo.memoTitle = "김진성메모";
        memoArrayList.add(mInfo);

        adapter = new MemoAdapter(getApplication(), R.layout.list_row, memoArrayList);
        lvMemoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder)view.getTag();
                String rowName = vh.tvMemoTitle.getText().toString();

                //start Activity about sponser clicked
                Toast.makeText(getApplicationContext(), rowName + " clicked()", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MemoModifyActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
