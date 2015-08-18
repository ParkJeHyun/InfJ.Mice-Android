package com.infjay.mice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.BusinessCardListAdapter;
import com.infjay.mice.adapter.FindPeopleAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-08-18.
 */
public class AddBusinessCardActivity extends CustomActionBarActivity {

    ListView lvCardList;
    BusinessCardListAdapter adapter;
    ArrayList<BusinessCardInfo> cardsArray;

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
                            BusinessCardInfo bci;
                            cardsArray = new ArrayList<BusinessCardInfo>();

                            JSONArray peopleJsonArray = new JSONArray(jobj.get("attach").toString());

                            for (int i = 0; i < peopleJsonArray.length(); i++) {
                                bci = new BusinessCardInfo();
                                JSONObject peopleJobj = new JSONObject(peopleJsonArray.get(i).toString());

                                bci.userSeq = peopleJobj.get("user_seq").toString();
                                bci.userId = peopleJobj.get("user_id").toString();
                                bci.idFlag = peopleJobj.get("id_flag").toString();
                                bci.name = peopleJobj.get("name").toString();
                                bci.company = peopleJobj.get("company").toString();
                                //bci.picturePath = peopleJobj.get("picture").toString();
                                bci.phone = peopleJobj.get("phone").toString();
                                bci.email = peopleJobj.get("email").toString();
                                bci.address = peopleJobj.get("address").toString();
                                bci.authorityKind = peopleJobj.get("authority_kind").toString();
                                bci.phone_1 = peopleJobj.get("phone_1").toString();
                                bci.phone_2 = peopleJobj.get("phone_2").toString();
                                bci.cellPhone_1 = peopleJobj.get("cell_phone_1").toString();
                                bci.cellPhone_2 = peopleJobj.get("cell_phone_2").toString();
                                bci.businessCardShareFlag = peopleJobj.get("business_card_share_flag").toString();
                                bci.nationCode = peopleJobj.get("nation_code").toString();
                                bci.duty = peopleJobj.get("duty").toString();

                                cardsArray.add(bci);
                            }

                            initView();
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        getAllBusinessCards();

    }

    private void getAllBusinessCards()
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "search_people_by_word");
            //jobj.put("title", "all");
            jobj.put("search_type", "name");
            jobj.put("word","");
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }

    private void initView()
    {
        lvCardList = (ListView)findViewById(R.id.lvBusinessCardList);

        adapter = new BusinessCardListAdapter(AddBusinessCardActivity.this, R.layout.list_row_add_card, cardsArray);
        lvCardList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                CheckBox cb = vh.cbAddCard;
                Boolean status = cb.isChecked();
                cb.setChecked(!status);
            }
        });
    }
}
