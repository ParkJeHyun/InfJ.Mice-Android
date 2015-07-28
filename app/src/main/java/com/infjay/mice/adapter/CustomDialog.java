package com.infjay.mice.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infjay.mice.MakeSurvActivity;
import com.infjay.mice.R;

/**
 * Created by KimJS on 2015-07-27.
 */
public class CustomDialog extends Dialog {

    private Button btCancel, btOk;
    private EditText etSurveyTitle, etNumberOfQuestions;
    private String title, numberOfQuestions;
    private static Context mContext;
    private String mSessionSeq;

    public CustomDialog(Context context, String sessionSeq)
    {
        super(context);
        mContext = context;
        mSessionSeq = sessionSeq;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_survey);
        //Dialog Size
        ViewGroup.LayoutParams params = this.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        etSurveyTitle = (EditText)findViewById(R.id.etSurveyTitle);
        etNumberOfQuestions = (EditText)findViewById(R.id.etNumberOfQuestions);
        btCancel = (Button)findViewById(R.id.btSurveyDialogCancel);
        btOk = (Button)findViewById(R.id.btSurveyDialogOK);

        btOk.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etSurveyTitle.getText().toString();
                numberOfQuestions = etNumberOfQuestions.getText().toString();
                if(title.equals("") || numberOfQuestions.equals(""))
                {
                    Toast.makeText(mContext.getApplicationContext(), "Please fill in the blank", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(mContext.getApplicationContext(), MakeSurvActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("numberOfQuestions", numberOfQuestions);
                    intent.putExtra("sessionSeq", mSessionSeq);
                    dismiss();
                    mContext.startActivity(intent);
                }
            }
        });


        btCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
