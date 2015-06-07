package com.infjay.mice;

import android.app.ProgressDialog;
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

import com.infjay.mice.artifacts.BusinessCardInfo;

import java.util.ArrayList;


public class FindPeopleActivity extends ActionBarActivity {
    private Spinner spinnerTitle;
    private Spinner spinnerType;
    private Button btFindPeople;
    private ListView lvFindPeople;
    private EditText etSearchWord;

    private String selectTitle;
    private String selectType;
    private String keyWord;
    private String[] titleList;
    private String[] typeList;


    private ArrayList<BusinessCardInfo> dataList;//metaData 후에 DB로 바뀔거
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        dataList = new ArrayList<BusinessCardInfo>();
        titleList = new String[4];
        typeList= new String[3];

        etSearchWord = (EditText)findViewById(R.id.etPeople);
        spinnerTitle = (Spinner)findViewById(R.id.spinnerTitle);
        spinnerType = (Spinner)findViewById(R.id.spinnerType);
        btFindPeople = (Button)findViewById(R.id.btFindPeople);
        lvFindPeople = (ListView)findViewById(R.id.lvFindPeople);

        setTitleList();
        setTypeList();
        setExampleData();

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

    public void setExampleData(){
        BusinessCardInfo bci = new BusinessCardInfo();
        bci.name = "김진성";
        bci.company = "Google";
        bci.duty = "Presenter";
        bci.email = "bob@gmail.com";
        dataList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "김희중";
        bci.company = "LG";
        bci.duty = "Participant";
        bci.email = "heejung@naver.com";
        dataList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "박제현";
        bci.company = "Samsung";
        bci.duty = "Guest";
        bci.email = "jehyun@hanmail.net";
        dataList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "안세호";
        bci.company = "Samsung";
        bci.duty = "Participant";
        bci.email = "seho@naver.com";
        dataList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "권순구";
        bci.company = "Naver";
        bci.duty = "Guest";
        bci.email = "sungu@gmail.com";
        dataList.add(bci);

        bci = new BusinessCardInfo();
        bci.name = "심정림";
        bci.company = "Naver";
        bci.duty = "Guest";
        bci.email = "jungrim@naver.com";
        dataList.add(bci);

    }

    public void makeResultList(ArrayList<BusinessCardInfo> resultList){
        for(int i=0;i<dataList.size();i++){
            if(selectTitle.equals("All")){
                if(selectType.equals("Name")){
                    if(keyWord.length()!=0){
                        //이름 검색
                        if(dataList.get(i).name.contains(keyWord)){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //전체검색
                        resultList.add(dataList.get(i));
                    }
                }
                else if(selectType.equals("Company")){
                    if(keyWord.length()!=0){
                        //회사 검색
                        if(dataList.get(i).company.contains(keyWord)){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //전체검색
                        resultList.add(dataList.get(i));
                    }
                }
                else{
                    if(keyWord.length()!=0){
                        //Email 검색
                        if(dataList.get(i).email.contains(keyWord)){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //전체검색
                        resultList.add(dataList.get(i));
                    }
                }
            }
            else if(selectTitle.equals("Presenter")){
                if(selectType.equals("Name")){
                    if(keyWord.length()!=0){
                        //이름 검색
                        if(dataList.get(i).name.contains(keyWord)&&dataList.get(i).duty.equals("Presenter")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Presenter")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else if(selectType.equals("Company")){
                    if(keyWord.length()!=0){
                        //회사 검색
                        if(dataList.get(i).company.contains(keyWord)&&dataList.get(i).duty.equals("Presenter")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Presenter")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else{
                    if(keyWord.length()!=0){
                        //Email 검색
                        if(dataList.get(i).email.contains(keyWord)&&dataList.get(i).duty.equals("Presenter")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Presenter")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
            }
            else if(selectTitle.equals("Participant")){
                if(selectType.equals("Name")){
                    if(keyWord.length()!=0){
                        //이름 검색
                        if(dataList.get(i).name.contains(keyWord)&&dataList.get(i).duty.equals("Participant")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Participant")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else if(selectType.equals("Company")){
                    if(keyWord.length()!=0){
                        //회사 검색
                        if(dataList.get(i).company.contains(keyWord)&&dataList.get(i).duty.equals("Participant")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Participant")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else{
                    if(keyWord.length()!=0){
                        //Email 검색
                        if(dataList.get(i).email.contains(keyWord)&&dataList.get(i).duty.equals("Participant")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Participant")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
            }
            else if(selectTitle.equals("Guest")){
                if(selectType.equals("Name")){
                    if(keyWord.length()!=0){
                        //이름 검색
                        if(dataList.get(i).name.contains(keyWord)&&dataList.get(i).duty.equals("Guest")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Guest")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else if(selectType.equals("Company")){
                    if(keyWord.length()!=0){
                        //회사 검색
                        if(dataList.get(i).company.contains(keyWord)&&dataList.get(i).duty.equals("Guest")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Guest")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
                else{
                    if(keyWord.length()!=0){
                        //Email 검색
                        if(dataList.get(i).email.contains(keyWord)&&dataList.get(i).duty.equals("Guest")){
                            resultList.add(dataList.get(i));
                        }
                    }
                    else{
                        //Presenter인 사람 모두
                        if(dataList.get(i).duty.equals("Guest")) {
                            resultList.add(dataList.get(i));
                        }
                    }
                }
            }
        }
        System.out.println("Make Result Complete!!");
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
            new MakeResultTask().execute();
        }
    }

    public class MakeResultTask extends AsyncTask<Void, Void, Void>{
        private ProgressDialog dialog;
        private ArrayList<BusinessCardInfo> resultList;
        private CardholderAdapter adapter;

        @Override
        protected void onPreExecute(){
            resultList = new ArrayList<BusinessCardInfo>();
            dialog = new ProgressDialog(FindPeopleActivity.this);
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
            adapter = new CardholderAdapter(FindPeopleActivity.this, R.layout.list_row_cardholder, resultList);
            lvFindPeople.setAdapter(adapter);
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
