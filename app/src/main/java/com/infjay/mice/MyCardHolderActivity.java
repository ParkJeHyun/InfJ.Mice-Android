package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.artifacts.*;

import java.util.ArrayList;

public class MyCardHolderActivity extends ActionBarActivity {

    private ListView lvCardholder;
    private CardholderAdapter adapter;
    private ArrayList<BusinessCardInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_holder);

        lvCardholder = (ListView)findViewById(R.id.listView_cardholder);
        arrayList = new ArrayList<BusinessCardInfo>();

        BusinessCardInfo bci = new BusinessCardInfo();
        bci.name = "PARK JEHYUN";
        bci.company = "UNIVERSITYOFSEOUL";
        arrayList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "KIM JINSEONG";
        bci.company = "SINRA";
        arrayList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "KIM HEEJOONG";
        bci.company = "SAMSUNG";
        arrayList.add(bci);

        adapter = new CardholderAdapter(this, R.layout.list_row_cardholder, arrayList);
        lvCardholder.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvCardholder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String name = vh.tvCardName.getText().toString();
                String company = vh.tvCardCompany.getText().toString();

                Intent intent = new Intent(MyCardHolderActivity.this,BusinessCardActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("company",company);
                startActivity(intent);

                //start Activity about sponser clicked
                Toast.makeText(getApplicationContext(), name + ", " + company + " clicked()", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ViewBusinessCardActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_breif_holder, menu);
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
