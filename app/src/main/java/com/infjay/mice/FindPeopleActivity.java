package com.infjay.mice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.infjay.mice.adapter.FindPeopleAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FindPeopleActivity extends CustomActionBarActivity {
    private Spinner spinnerTitle;
    private Spinner spinnerType;
    private Button btFindPeople;
    private ListView lvFindPeople;
    private EditText etSearchWord;
    private FindPeopleAdapter adapter;

    private String selectTitle;
    private String selectType;
    private String keyWord;
    private String[] titleList;
    private String[] typeList;

    ArrayList<BusinessCardInfo> resultList;

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

                                resultList.add(bci);
                            }
                            System.out.println(resultList.size());
                            Toast.makeText(getApplicationContext(), "FIND_PEOPLE_SUCCESS", Toast.LENGTH_SHORT).show();
                            adapter = new FindPeopleAdapter(FindPeopleActivity.this, R.layout.list_row_findpeople, resultList);
                            lvFindPeople.setAdapter(adapter);
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
        setContentView(R.layout.activity_find_people);

        //resultList = new ArrayList<BusinessCardInfo>();
        titleList = new String[4];
        typeList= new String[3];

        etSearchWord = (EditText)findViewById(R.id.etPeople);
        spinnerTitle = (Spinner)findViewById(R.id.spPeopleTitle);
        spinnerType = (Spinner)findViewById(R.id.spPeopleType);
        btFindPeople = (Button)findViewById(R.id.btFindPeople);
        lvFindPeople = (ListView)findViewById(R.id.lvFindPeople);


        setTitleList();
        setTypeList();

        spinnerTitle.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, titleList));
        spinnerTitle.setOnItemSelectedListener(new TitleAdapterListener());
        spinnerType.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, typeList));
        spinnerType.setOnItemSelectedListener(new TypeAdapterListener());

        btFindPeople.setOnClickListener(new FindBtListener());
    }

    public void setTitleList(){
        titleList[0] = "All";
        titleList[1] = "Presenter";
        titleList[2] = "Participant";
        titleList[3] = "Guest";
    }

    public void setTypeList(){
        typeList[0] = "Name";
        typeList[1] = "Company";
        typeList[2] = "E-mail";
    }

    /*
    public void makeResultList(){
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "search_people_by_word");
            //jobj.put("title", selectTitle);
            jobj.put("search_type", selectType.toLowerCase());
            jobj.put("word",keyWord);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

        System.out.println("Make Result Complete!!");
    }
    */
    public void setListViewClickListener(){
        lvFindPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();

                String userSeq = vh.userSeq;
                Intent intent = new Intent(FindPeopleActivity.this, FindPeopleBusinessCardActivity.class);
                intent.putExtra("userSeq", userSeq);

                startActivity(intent);
            }
        });
    }

    public class TypeAdapterListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectType = (String)spinnerType.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class TitleAdapterListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectTitle = (String)spinnerTitle.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class FindBtListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            keyWord = etSearchWord.getText().toString();
            //Toast.makeText(getApplicationContext(), selectTitle+selectType+keyWord, Toast.LENGTH_SHORT).show();
            //new MakeResultTask().execute();

            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "search_people_by_word");
                //jobj.put("title", selectTitle);
                jobj.put("search_type", selectType.toLowerCase());
                jobj.put("word",keyWord);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

            System.out.println("Make Result Complete!!");

        }
    }

    /*
    public class MakeResultTask extends AsyncTask<Void, Void, Void>{
        private ProgressDialog dialog;
        private FindPeopleAdapter adapter;

        @Override
        protected void onPreExecute(){
            //resultList = new ArrayList<BusinessCardInfo>();
            dialog = new ProgressDialog(FindPeopleActivity.this);
            dialog.setMessage("Loading....");
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            makeResultList();
            return null;
        }
        @Override
        protected void onPostExecute(Void id){
            dialog.dismiss();

            adapter = new FindPeopleAdapter(FindPeopleActivity.this, R.layout.list_row_findpeople, resultList);
            lvFindPeople.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_people, menu);
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
