package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private boolean mAnswerIsTrue;
    private boolean mCheater;


    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN ="com.bignerdranch.android.geoquiz.answer_shown";
    private static final String CHEATER="cheater";
    private static final String ANSWER ="answer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState !=null){
            mCheater = savedInstanceState.getBoolean(CHEATER,false);
            mAnswerIsTrue = savedInstanceState.getBoolean(ANSWER,false);
            setAnswerShownResult(mCheater);



        }
        setContentView(R.layout.activity_cheat);

        //Retrieve the value from the Extra
        //getIntent() allways returns the Intent that started the activity
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);

        //Keep the answer shown to the user if the screen rotates
        if(mCheater)
            mAnswerTextView.setText(mAnswerIsTrue? R.string.true_button:R.string.false_button);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }

                else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                mCheater = true;
                setAnswerShownResult(true);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);

        savedState.putBoolean(CHEATER,mCheater);
        savedState.putBoolean(ANSWER,mAnswerIsTrue);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    public static Intent newIntent(Context packageContext,boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;

    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }
}
