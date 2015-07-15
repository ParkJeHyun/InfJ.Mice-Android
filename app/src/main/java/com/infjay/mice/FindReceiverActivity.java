package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.FindPeopleAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FindReceiverActivity extends CustomActionBarActivity implements View.OnClickListener{
    EditText etReceiverName;
    Button btSearch;
    ListView lvReceiver;
    String name;
    ArrayList<BusinessCardInfo> resultList;
    FindPeopleAdapter adapter;

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout
            System.out.println("in handler");
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            }


            if (msg.what == 1) {
                //핸들러 1번일 때
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("search_people_by_word")){
                        if(jobj.get("result").equals("SEARCH_PEOPLE_BY_WORD_ERROR")){
                            Toast.makeText(getApplicationContext(), "SEARCH_PEOPLE_BY_WORD_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("SEARCH_PEOPLE_BY_WORD_SUCCESS")){
                            //list로 온것 jsonArray파싱을 통해 resultData로 만듬
                            //resultData는 BusinessCard LIst
                            BusinessCardInfo bci;
                            resultList = new ArrayList<BusinessCardInfo>();

                            JSONArray peopleJsonArray = new JSONArray(jobj.get("attach").toString());

                            for (int i = 0; i < peopleJsonArray.length(); i++) {
                                bci = new BusinessCardInfo();
                                JSONObject peopleJobj = new JSONObject(peopleJsonArray.get(i).toString());

                                bci.userSeq = peopleJobj.get("user_seq").toString();
                                bci.userId = peopleJobj.get("user_id").toString();
                                bci.idFlag = peopleJobj.get("id_flag").toString();
                                bci.name = peopleJobj.get("name").toString();
                                bci.company = peopleJobj.get("company").toString();
                                bci.picturePath = peopleJobj.get("picture").toString();
                                bci.phone = peopleJobj.get("phone").toString();
                                bci.email = peopleJobj.get("email").toString();
                                bci.address = peopleJobj.get("address").toString();
                                bci.authorityKind = peopleJobj.get("authority_kind").toString();
                                bci.phone_1 = peopleJobj.get("phone_1").toString();
                                bci.phone_2 = peopleJobj.get("phone_2").toString();
                                bci.cellPhone_1 = peopleJobj.get("cell_phone_1").toString();
                                bci.cellPhone_2 = peopleJobj.get("cell_phone_2").toString();
                                bci.businessCardCode = peopleJobj.get("business_card_code").toString();
                                bci.businessCardShareFlag = peopleJobj.get("business_card_share_flag").toString();
                                bci.nationCode = peopleJobj.get("nation_code").toString();
                                bci.platform = peopleJobj.get("platform").toString();
                                bci.regDate = peopleJobj.get("reg_date").toString();
                                bci.modDate = peopleJobj.get("mod_date").toString();
                                bci.duty = peopleJobj.get("duty").toString();

                                resultList.add(bci);
                            }
                            System.out.println(resultList.size());
                            Toast.makeText(getApplicationContext(), "FIND_PEOPLE_SUCCESS", Toast.LENGTH_SHORT).show();
                            adapter = new FindPeopleAdapter(FindReceiverActivity.this, R.layout.list_row_findpeople, resultList);
                            lvReceiver.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            setListViewClickListener();
                        }

                        else if(jobj.get("result").equals("SEARCH_PEOPLE_BY_WORD_FAIL")){
                            Toast.makeText(getApplicationContext(), "SEARCH_PEOPLE_BY_WORD_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not find_people", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                //response 받은거 파싱해서

            }

            if (msg.what == 2) {
                //핸들러 2번일 때
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_receiver);

        etReceiverName = (EditText)findViewById(R.id.etReceiverName);
        btSearch = (Button)findViewById(R.id.btReceiverSerach);
        lvReceiver = (ListView)findViewById(R.id.lvReceiver);

        btSearch.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_receiver, menu);
        return true;
    }

    public void setListViewClickListener(){
        lvReceiver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String userSeq = vh.userSeq;
                String userName = vh.tvCardName.getText().toString();
                Intent intent = new Intent(FindReceiverActivity.this,ChattingActivity.class);
                intent.putExtra("name", userName);

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btReceiverSerach){
            name = etReceiverName.getText().toString();

            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "search_people_by_word");
                //jobj.put("title", selectTitle);
                jobj.put("search_type", "name");
                jobj.put("word",name);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

        }
    }
}
