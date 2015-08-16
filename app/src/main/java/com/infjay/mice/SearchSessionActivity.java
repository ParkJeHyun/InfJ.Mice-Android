package com.infjay.mice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;


public class SearchSessionActivity extends CustomActionBarActivity {
    private Spinner spTitle;
    private Button btSearchSession;
    private ListView lvSearchSession;
    private EditText etSearchWord;

    private String selectTitle;
    private String keyWord;
    private String[] titleList;
    private ArrayList<AgendaSessionInfo> resultList;
    private ArrayList<AgendaSessionInfo> dataList;//metaData 후에 DB로 바뀔거

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_session);

        resultList = new ArrayList<AgendaSessionInfo>();
        dataList = new ArrayList<AgendaSessionInfo>();
        titleList = new String[3];


        etSearchWord = (EditText)findViewById(R.id.etSession);
        spTitle = (Spinner)findViewById(R.id.spSessionTitle);

        btSearchSession = (Button)findViewById(R.id.btSearchSession);
        lvSearchSession = (ListView)findViewById(R.id.lvSearchSession);
        setListViewClickListener();

        setTitleList();

        spTitle.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, titleList));
        spTitle.setOnItemSelectedListener(new TitleAdapterListener());

        btSearchSession.setOnClickListener(new FindBtListener());
    }

    public void setTitleList(){
        titleList[0] = "Title";
        titleList[1] = "Writer";
        titleList[2] = "Presenter";
    }

    public void setListViewClickListener(){
        lvSearchSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String sessionSeq = vh.sessionSeq;

                Intent intent = new Intent(SearchSessionActivity.this,SessionInfoActivity.class);

                intent.putExtra("sessionSeq", sessionSeq);
                intent.putExtra("activityFrom", "SearchSessionActivity");
                startActivity(intent);
            }
        });
    }
    public void makeResultList(){

        if (selectTitle.equals("Title")) {
            //세션 제목 검색
            if (keyWord.length() != 0) {
                //제목 검색
                resultList = DBManager.getManager(getApplicationContext()).getSessionFromAgendaBySessionTitle(keyWord);
                System.out.println(resultList.size());
            } else {
                //전체검색
                resultList = DBManager.getManager(getApplicationContext()).getAllSessionFromAgenda();
                System.out.println(resultList.size());
            }
        }
        else if (selectTitle.equals("Writer")) {
           if (keyWord.length() != 0) {
               //저자 검색
               resultList = DBManager.getManager(getApplicationContext()).getSessionFromAgendaBySessionWriter(keyWord);
               System.out.println(resultList.size());
           }
           else {
                //전체 검색
               resultList = DBManager.getManager(getApplicationContext()).getAllSessionFromAgenda();
               System.out.println(resultList.size());
           }
        }
        else {
            //발표자 검색
            if(keyWord.length() != 0){
                //발표자 검색
                resultList = DBManager.getManager(getApplicationContext()).getSessionFromAgendaBySessionPresenter(keyWord);
                System.out.println(resultList.size());

            }
            else{
                resultList = DBManager.getManager(getApplicationContext()).getAllSessionFromAgenda();
                System.out.println(resultList.size());
            }
        }

        System.out.println("Make Result Complete!!");
    }

    public class TitleAdapterListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectTitle = (String)spTitle.getItemAtPosition(position);
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
            new MakeResultTask().execute();
        }
    }

    public class MakeResultTask extends AsyncTask<Void, Void, Void>{
        private ProgressDialog dialog;
        private SessionListAdapter adapter;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(SearchSessionActivity.this);
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
            adapter = new SessionListAdapter(SearchSessionActivity.this, R.layout.list_row_session, resultList);
            lvSearchSession.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
