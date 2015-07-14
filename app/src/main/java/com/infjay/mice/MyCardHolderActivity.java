package com.infjay.mice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.infjay.mice.adapter.CardholderAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.*;

import java.util.ArrayList;

public class MyCardHolderActivity extends CustomActionBarActivity {

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

                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_holder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addCard) {
            showInputDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Input Shared Code");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        //EditText setting >> change to infate later
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(4);
        input.setFilters(FilterArray);
        input.setGravity(Gravity.CENTER);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                value.toString();
                // find business card by code
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }
}
