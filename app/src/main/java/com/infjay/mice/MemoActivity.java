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
import com.infjay.mice.database.DBManager;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        lvMemoList = (ListView)findViewById(R.id.lvMemoList);

        memoArrayList = new ArrayList<MemoInfo>();

        memoArrayList = DBManager.getManager(getApplicationContext()).getAllMemo();
        adapter = new MemoAdapter(getApplication(), R.layout.list_row_memo, memoArrayList);

        lvMemoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String memoSeq = vh.memoSeq;

                Intent intent = new Intent(getApplicationContext(), MemoEditActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra("memoSeq", memoSeq);
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
        int id = item.getItemId();

        //Add New Memo
        if (id == R.id.itAddMemo) {
            Intent intent = new Intent(this.getApplicationContext(), MemoEditActivity.class);
            intent.putExtra("isNew", true);
            startActivity(intent);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
