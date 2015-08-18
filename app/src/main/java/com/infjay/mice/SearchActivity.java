package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.infjay.mice.adapter.FindPeopleAdapter;
import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends CustomActionBarActivity {

    private ScrollView svSearch;
    private LinearLayout llDropdownPeople;
    private LinearLayout llSearchPeopleResult;
    private LinearLayout llDropdownSession;
    private LinearLayout llSearchSessionResult;

    private ListView lvSearchPeopleResult;
    private ListView lvSearchSessionResult;

    private FindPeopleAdapter peopleAdapter;
    private SessionListAdapter sessionAdapter;

    private Boolean isPeopleDropdown = false;
    private Boolean isSessionDropdown = false;

    private Button btFindPeople;
    private EditText etSearchWord;
    private String keyWord;

    ArrayList<BusinessCardInfo> peopleResultList;
    private ArrayList<AgendaSessionInfo> sessionResultList;

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
                            llDropdownPeople.setVisibility(View.VISIBLE);

                            //list로 온것 jsonArray파싱을 통해 resultData로 만듬
                            //resultData는 BusinessCard LIst
                            BusinessCardInfo bci;
                            peopleResultList = new ArrayList<BusinessCardInfo>();

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

                                peopleResultList.add(bci);
                            }
                            System.out.println(peopleResultList.size());
                            Toast.makeText(getApplicationContext(), "FIND_PEOPLE_SUCCESS", Toast.LENGTH_SHORT).show();
                            peopleAdapter = new FindPeopleAdapter(SearchActivity.this, R.layout.list_row_findpeople, peopleResultList);
                            lvSearchPeopleResult.setAdapter(peopleAdapter);
                            peopleAdapter.notifyDataSetChanged();
                            lvSearchPeopleResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    ViewHolder vh = (ViewHolder) view.getTag();

                                    String userSeq = vh.userSeq;
                                    Intent intent = new Intent(SearchActivity.this, FindPeopleBusinessCardActivity.class);
                                    intent.putExtra("userSeq", userSeq);

                                    startActivity(intent);
                                }
                            });
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    public void initView()
    {
        svSearch = (ScrollView)findViewById(R.id.svSearch);
        etSearchWord = (EditText)findViewById(R.id.etPeople);
        btFindPeople = (Button)findViewById(R.id.btFindPeople);

        llDropdownPeople = (LinearLayout)findViewById(R.id.llDropdownPeople);
        llSearchPeopleResult = (LinearLayout)findViewById(R.id.llPeopleResult);
        llDropdownSession = (LinearLayout)findViewById(R.id.llDropdownSession);
        llSearchSessionResult = (LinearLayout)findViewById(R.id.llSessionResult);

        lvSearchPeopleResult = (ListView)findViewById(R.id.lvSearchPeopleResult);
        lvSearchSessionResult = (ListView)findViewById(R.id.lvSearchSessionResult);

        btFindPeople.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyWord = etSearchWord.getText().toString();
                SeachPeople();
                SearchSession();
            }
        });

        llDropdownPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPeopleDropdown) {
                    llSearchPeopleResult.setVisibility(View.GONE);
                    isPeopleDropdown = !isPeopleDropdown;
                } else {
                    llSearchPeopleResult.setVisibility(View.VISIBLE);
                    isPeopleDropdown = !isPeopleDropdown;
                }
            }
        });

        llDropdownSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSessionDropdown) {
                    llSearchSessionResult.setVisibility(View.GONE);
                    isSessionDropdown = !isSessionDropdown;
                } else {
                    llSearchSessionResult.setVisibility(View.VISIBLE);
                    isSessionDropdown = !isSessionDropdown;
                }
            }
        });

        lvSearchPeopleResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                svSearch.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lvSearchSessionResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                svSearch.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
    public void SeachPeople()
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "search_people_by_word");
            //jobj.put("title", "all");
            jobj.put("search_type", "name");
            jobj.put("word",keyWord);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }

    public void SearchSession()
    {
        new MakeResultTask().execute();
    }

    public class MakeResultTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private SessionListAdapter adapter;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(SearchActivity.this);
            dialog.setMessage("Loading....");
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            sessionResultList = DBManager.getManager(getApplicationContext()).getAllSessionFromAgenda();
            return null;
        }
        @Override
        protected void onPostExecute(Void id){
            dialog.dismiss();
            adapter = new SessionListAdapter(SearchActivity.this, R.layout.list_row_session, sessionResultList);
            lvSearchSessionResult.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            lvSearchSessionResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ViewHolder vh = (ViewHolder) view.getTag();
                    String sessionSeq = vh.sessionSeq;
                    Intent intent = new Intent(SearchActivity.this, SessionInfoActivity.class);
                    intent.putExtra("sessionSeq", sessionSeq);
                    intent.putExtra("activityFrom", "SearchSessionActivity");
                    startActivity(intent);
                }
            });

            llDropdownSession.setVisibility(View.VISIBLE);
        }
    }
}