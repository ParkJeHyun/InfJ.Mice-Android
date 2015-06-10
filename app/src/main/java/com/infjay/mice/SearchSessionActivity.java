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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;

import java.util.ArrayList;


public class SearchSessionActivity extends ActionBarActivity {
    private Spinner spTitle;
    private Button btSearchSession;
    private ListView lvSearchSession;
    private EditText etSearchWord;

    private String selectTitle;
    private String keyWord;
    private String[] titleList;

    private ArrayList<AgendaSessionInfo> dataList;//metaData 후에 DB로 바뀔거
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_session);

        dataList = new ArrayList<AgendaSessionInfo>();
        titleList = new String[3];


        etSearchWord = (EditText)findViewById(R.id.etSession);
        spTitle = (Spinner)findViewById(R.id.spSessionTitle);

        btSearchSession = (Button)findViewById(R.id.btSearchSession);
        lvSearchSession = (ListView)findViewById(R.id.lvSearchSession);
        setListViewClickListener();

        setTitleList();
        setExampleData();

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
                String title = vh.tvSessionName.getText().toString();
                String writer = vh.tvSessionWriter.getText().toString();
                String presenter = vh.tvSessionPresenter.getText().toString();

                Intent intent = new Intent(SearchSessionActivity.this,SessionInfoActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("writer",writer);
                intent.putExtra("presenter",presenter);

                startActivity(intent);
            }
        });
    }

    public void setExampleData(){
        AgendaSessionInfo agi = new AgendaSessionInfo();
        agi.setSessionTitle("Session No.1");
        agi.setSessionWriterUserSeq("김진성");
        agi.setSessionPresenterUserSeq("김희중");
        dataList.add(agi);

        agi = new AgendaSessionInfo();
        agi.setSessionTitle("Session No.2");
        agi.setSessionWriterUserSeq("김희중");
        agi.setSessionPresenterUserSeq("박제현");
        dataList.add(agi);

        agi = new AgendaSessionInfo();
        agi.setSessionTitle("Session No.3");
        agi.setSessionWriterUserSeq("박제현");
        agi.setSessionPresenterUserSeq("김진성");
        dataList.add(agi);

    }

    public void makeResultList(ArrayList<AgendaSessionInfo> resultList){
        for(int i=0;i<dataList.size();i++) {
            if (selectTitle.equals("Title")) {
                //세션 제목 검색
                if (keyWord.length() != 0) {
                    //제목 검색
                    if (dataList.get(i).getSessionTitle().contains(keyWord)) {
                        resultList.add(dataList.get(i));
                    }
                } else {
                    //전체검색
                    resultList.add(dataList.get(i));
                }
            }
            else if (selectTitle.equals("Writer")) {
                if (keyWord.length() != 0) {
                    //저자 검색
                    if (dataList.get(i).getSessionWriterUserSeq().contains(keyWord)) {
                        resultList.add(dataList.get(i));
                    }
                } else {
                    //전체 검색
                    resultList.add(dataList.get(i));
                }
            }
            else {
                //발표자 검색
                if(keyWord.length() != 0){
                    //발표자 검색
                    if (dataList.get(i).getSessionPresenterUserSeq().contains(keyWord)){
                        resultList.add(dataList.get(i));
                    }
                }
                else{
                    resultList.add(dataList.get(i));
                }
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
        private ArrayList<AgendaSessionInfo> resultList;
        private SessionListAdapter adapter;

        @Override
        protected void onPreExecute(){
            resultList = new ArrayList<AgendaSessionInfo>();
            dialog = new ProgressDialog(SearchSessionActivity.this);
            dialog.setMessage("Loading....");
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            makeResultList(resultList);
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
