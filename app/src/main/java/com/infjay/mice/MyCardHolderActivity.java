package com.infjay.mice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.CardholderAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.*;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCardHolderActivity extends CustomActionBarActivity {

    private ListView lvCardholder;
    private CardholderAdapter adapter;
    private ArrayList<BusinessCardInfo> arrayList;

    private String mySeq;

    private String TAG = "MYCardHolderActivity";

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_business_card_by_code"))
                    {
                        if(jobj.get("result").equals("GET_BUSINESS_CARD_BY_CODE_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_BUSINESS_CARD_BY_CODE_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_BUSINESS_CARD_BY_CODE_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_BUSINESS_CARD_BY_CODE_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_BUSINESS_CARD_BY_CODE_ALREADY"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_BUSINESS_CARD_BY_CODE_ALREADY", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_BUSINESS_CARD_BY_CODE_SUCCESS"))
                        {
                            JSONObject jobjCardInfo = new JSONObject(jobj.get("attach")+"");

                            BusinessCardInfo bInfo = new BusinessCardInfo();
                            bInfo.userSeq = jobjCardInfo.get("user_seq")+"";
                            bInfo.idFlag = jobjCardInfo.get("id_flag")+"";
                            bInfo.userId = jobjCardInfo.get("user_id")+"";
                            bInfo.name = jobjCardInfo.get("name")+"";
                            bInfo.company = jobjCardInfo.get("company")+"";
                            bInfo.picturePath = "";
                            bInfo.phone = jobjCardInfo.get("phone")+"";
                            bInfo.email = jobjCardInfo.get("email")+"";
                            bInfo.address = jobjCardInfo.get("address")+"";
                            bInfo.authorityKind = jobjCardInfo.get("authority_kind")+"";
                            bInfo.phone_1 = jobjCardInfo.get("phone_1")+"";
                            bInfo.phone_2 = jobjCardInfo.get("phone_2")+"";
                            bInfo.cellPhone_1 = jobjCardInfo.get("cell_phone_1")+"";
                            bInfo.cellPhone_2 = jobjCardInfo.get("cell_phone_2")+"";
                            bInfo.businessCardCode = jobjCardInfo.get("business_card_code")+"";
                            bInfo.businessCardShareFlag = jobjCardInfo.get("business_card_share_flag")+"";
                            bInfo.nationCode = jobjCardInfo.get("nation_code")+"";
                            bInfo.platform = jobjCardInfo.get("platform")+"";
                            bInfo.duty = jobjCardInfo.get("duty")+"";

                            DBManager.getManager(getApplicationContext()).insertBusinessCard(mySeq, bInfo);
                            setBusinessCardList();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MASSAGE_TYPE", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_holder);

        mySeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
        if(mySeq == null)
        {
            Toast.makeText(getApplicationContext(), "WRONG_USER", Toast.LENGTH_SHORT).show();
            return;
        }
        lvCardholder = (ListView)findViewById(R.id.listView_cardholder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        setBusinessCardList();
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
            //showInputDialog();
            Intent intent = new Intent(getApplicationContext(), AddBusinessCardActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setBusinessCardList()
    {
        arrayList = DBManager.getManager(getApplicationContext()).getBusinessCardByUserSeq(mySeq);

        adapter = new CardholderAdapter(this, R.layout.list_row_cardholder, arrayList);
        lvCardholder.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvCardholder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String name = vh.tvCardName.getText().toString();
                String userSeq = vh.userSeq;

                Intent intent = new Intent(MyCardHolderActivity.this, BusinessCardActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("userSeq", userSeq);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
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
        FilterArray[0] = new InputFilter.LengthFilter(8);
        input.setFilters(FilterArray);
        input.setGravity(Gravity.CENTER);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String cardCode = input.getText().toString();

                JSONObject jobj = new JSONObject();
                try
                {
                    jobj.put("messagetype", "get_business_card_by_code");
                    jobj.put("user_seq", mySeq);
                    jobj.put("business_card_code", cardCode);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

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
