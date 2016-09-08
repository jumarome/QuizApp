package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private boolean mIsCheater;
    private int mCurrentIndex = 0;
    private static final String TAG = QuizActivity.class.getCanonicalName();
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT=0;
    private static final String CHEATER="cheater";




    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_frontera,false),
            new Question(R.string.question_censo,true)

    };





    //logging Activity lyfeCycle

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() called");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate(Bundle) called");
        super.onCreate(savedInstanceState);

        //We restore the question index from the Bundle
        if(savedInstanceState !=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER,false);
        }
        setContentView(R.layout.activity_quiz);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        // Linking member variables to XML layout widgets
        mTrueButton  = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton  = (Button) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);




        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });





        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start  CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                // startActivity(i);

                //We start a child activity with a code in order to reference when the user goes back,
                startActivityForResult(i,REQUEST_CODE_CHEAT);

            }
        });



        // Display the first question when the app starts
        updateQuestion();



    }

    // We save the question index in the Bundle so that it is available after
    // a device rotation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putBoolean(CHEATER,mIsCheater);

    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        boolean answerIsTrue = currentQuestion.isAnswerTrue();

        int messageResId=0;

        if(mIsCheater)
            messageResId = R.string.judgement_toast;

        else{
            if(userPressedTrue == answerIsTrue)
                messageResId = R.string.correct_toast;
            else
                messageResId = R.string.incorrect_toast;
        }


        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode != Activity.RESULT_OK){
           return;
       }

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }

            // this checks if the user CHEATS (It could also be implemented here)
            mIsCheater = CheatActivity.wasAnswerShown(data);

        }

    }
}
