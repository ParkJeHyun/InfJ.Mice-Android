package com.infjay.mice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.Arrays;
import java.util.List;


public class FacebookLoginActivity extends ActionBarActivity {
    Intent intent;
    Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        checkFacebookSession(this);
    }

    public void checkFacebookSession(Context context){
        Session session = Session.getActiveSession();

        if(session == null){
            session = Session.openActiveSessionFromCache(context);
            if(session == null){
                Toast.makeText(getApplicationContext(), "캐시에도 세션이 없음", Toast.LENGTH_SHORT).show();
                Session.openActiveSession(this, true, statusCallback);
            }
            else{
                Toast.makeText(getApplicationContext(), "캐시에 세션이 있음", Toast.LENGTH_SHORT).show();
                getFaceBookMe(session);
            }
        }
        else{
            System.out.println("세션 있음");
            getFaceBookMe(session);
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        private List<String> permitArray = Arrays.asList("user_birthday");
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            boolean isContainPermit = true;
            if (session.isOpened()) {
                //session 이 열려있음
                for(int i=0;i<permitArray.size();i++){
                    if(!session.getPermissions().contains(permitArray.get(i))){
                        isContainPermit = false;
                        break;
                    }
                }
                if(!isContainPermit){
                    Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(FacebookLoginActivity.this,permitArray);
                    session.requestNewReadPermissions(newPermissionsRequest);
                }
                else{
                    getFaceBookMe(session);
                }
            }
            else {
                //session안열림
            }
        }
    }

    private void getFaceBookMe(Session session) {
        if (session.isOpened()) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser me, Response response) {
                    try {
                        response.getError();
                        // 정보가져오기 성공.
                        intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), me.getName(), Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();

        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
