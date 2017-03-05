package com.example.melody.dementia;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.BitmapFactory;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements
        RecognitionListener {

    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecogActivity";
    private ImageView returnedImages;
    private ImageView sentimentFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        returnedImages = (ImageView) findViewById (R.id.imageView2);
        sentimentFace = (ImageView) findViewById (R.id.imageView1);

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        JSONObject Keywords = new JSONObject();
        String check;
        String returnedImageURl;
        try {
            Log.i(LOG_TAG, "onResults");
            ArrayList<String> matches = results
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            //Toast.makeText(this,"Returned from Spech rec:  \n" , Toast.LENGTH_LONG).show();

            String text = "";
            //for (String result : matches)
            text = matches.get(0);
            // send text off to service
            //Toast.makeText(this,"Returned Text from Speech: \n"+ text, Toast.LENGTH_LONG).show();

            /***** FIND KEYWORDS ************/

            HttpLogin newReq = new HttpLogin();
            String returnedStringKeywords;

            returnedStringKeywords = newReq.sendReceiveRequest(text);


            JSONObject returnedJson = new JSONObject(returnedStringKeywords);


            JSONArray returnedKeyWordsJson = new JSONArray();
            JSONArray theFinalWords = new JSONArray();

            if (returnedJson.getJSONArray("documents") != null) {
                returnedKeyWordsJson = returnedJson.getJSONArray("documents");

                if (returnedKeyWordsJson.getJSONObject(0) != null) {
                    Keywords = returnedKeyWordsJson.getJSONObject(0);

                    if (Keywords.getJSONArray("keyPhrases") != null) {
                        theFinalWords = Keywords.getJSONArray("keyPhrases");
                    }
                }
            }


            /******Disaplay Keyword ********/

            for (int i = 0; i < theFinalWords.length(); i++) {
                theFinalWords.getString(i);
            }
            if(theFinalWords.getString(0)!=null) {
                check = theFinalWords.getString(0);


                findImage letsGetaPicture = new findImage();
                returnedImageURl = letsGetaPicture.getImage(check);


                JSONObject findFirstResult = new JSONObject(returnedImageURl);
                JSONArray firstImageReturned = new JSONArray();
                JSONObject findThumbnail = new JSONObject();
                String thumbnailURL = new String();

                if (findFirstResult.getJSONArray("value") != null)

                {
                    firstImageReturned = findFirstResult.getJSONArray("value");

                    if (firstImageReturned.getJSONObject(0) != null) {
                        findThumbnail = firstImageReturned.getJSONObject(0);
                    }

                    if (findThumbnail.getString("thumbnailUrl") != null) {
                        thumbnailURL = findThumbnail.getString("thumbnailUrl");
                    }

                    URL displayImage = new URL(thumbnailURL);
                    Bitmap bmp = BitmapFactory.decodeStream(displayImage.openConnection().getInputStream());
                    returnedImages.setImageBitmap(bmp);
                }

            }
            /******* Find Sentiment *********/

            String returnedStringSentiment;
            returnedStringSentiment = newReq.sendReceiveSentiment(text);


            JSONObject returnedJsonSentiment = new JSONObject(returnedStringSentiment);


            JSONArray returnedSentimentJson = new JSONArray();
            JSONObject theFinalSentiment = new JSONObject();
            Double feelingSentiment = 0.0;

            if (returnedJsonSentiment.getJSONArray("documents") != null) {
                returnedSentimentJson = returnedJsonSentiment.getJSONArray("documents");

                if (returnedSentimentJson.getJSONObject(0) != null) {
                    theFinalSentiment = returnedSentimentJson.getJSONObject(0);

                    if (theFinalSentiment.getDouble("score") != 0.0) {
                        feelingSentiment = theFinalSentiment.getDouble("score");
                    }
                }
            }


            if (feelingSentiment > 0.7) {
                sentimentFace.setImageResource(R.drawable.emoticon_17_48);
            } else if (feelingSentiment > 0.5) {
                sentimentFace.setImageResource(R.drawable.happy_48);
            } else {
                sentimentFace.setImageResource(R.drawable.emoticon_3_48);
            }


            /*********Predict Next Word ********/


            findNextWords predictNext = new findNextWords();
            String returnedStringPredict;
            returnedStringPredict = predictNext.theNextWordIs(text);
            JSONObject returnedJsonPredict = new JSONObject(returnedStringPredict);
            JSONArray returnedPredictedJson = new JSONArray();
            JSONObject theFinalPrediction = new JSONObject();
            String PredictedWord;

            if (returnedJsonPredict.getJSONArray("candidates") != null) {
                returnedPredictedJson = returnedJsonPredict.getJSONArray("candidates");

                if (returnedPredictedJson.getJSONObject(0) != null) {
                    theFinalPrediction = returnedPredictedJson.getJSONObject(0);

                    if (theFinalPrediction.getString("word") != null) {
                        PredictedWord = theFinalPrediction.getString("word");
                        returnedText.setText(PredictedWord);

                    }
                }
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

}