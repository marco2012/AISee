/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package me.r00t.aisee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import me.r00t.aisee.Classifier.Recognition;

public class RecognitionScoreView extends View implements ResultsView, TextToSpeech.OnInitListener{
  private static final float TEXT_SIZE_DIP = 24;
  private List<Recognition> results;
  private final float textSizePx;
  private final Paint fgPaint;
  private final Paint bgPaint;
    private TextToSpeech tts;
    private boolean t = false;

  public RecognitionScoreView(final Context context, final AttributeSet set) {
    super(context, set);


    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(0xcc4285f4);

      tts = new TextToSpeech(getContext(), this);
  }

  @Override
  public void setResults(final List<Recognition> results) {
    this.results = results;
    postInvalidate();
  }

  @Override
  public void onDraw(final Canvas canvas) {
    final int x = 10;
    int y = (int) (fgPaint.getTextSize() * 1.5f);
    canvas.drawPaint(bgPaint);


    if (results != null) {

      for (final Recognition recog : results) {
        Float confidence = Float.valueOf(new DecimalFormat("#.000").format(recog.getConfidence()));
        canvas.drawText(recog.getTitle().toUpperCase() + ", sicuro al " + ( confidence * 100 + "%"), x, y, fgPaint);
        y += fgPaint.getTextSize() * 1.5f;

          //color
        if (recog.getConfidence() >= 0.86f) bgPaint.setColor(Color.parseColor("#4caf50"));
        else bgPaint.setColor(Color.parseColor("#f44336"));

          if(t && recog.getConfidence() > 0.86f) speak(recog.getTitle().toLowerCase());
      }

    }
  }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

            int eventAction = event.getAction();

            // you may need the x/y location
            int x = (int)event.getX();
            int y = (int)event.getY();

            // put your code in here to handle the event
            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:

                    if (!t) {
                        t=true;
                        Toast.makeText(getContext(), "Tocca di nuovo per disattivare audio", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        t=false;
                        Toast.makeText(getContext(), "Tocca di nuovo per attivare audio", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }

            // tell the View to redraw the Canvas
            invalidate();

            // tell the View that we handled the event
            return true;

    }

    public void speak(String recog){
      switch(recog){
          case "federico":
              tts.setLanguage(Locale.ITALIAN);
              tts.speak ("Ciao federico, come va?",
                      TextToSpeech.QUEUE_ADD,
                      null,
                      "result");
              break;
          case "laila":
              tts.setLanguage(Locale.ITALIAN);
              tts.speak ("Laila puzzolotta",
                      TextToSpeech.QUEUE_ADD,
                      null,
                      "result");
              break;
          case "pru":
              tts.setLanguage(Locale.ITALIAN);
              tts.speak ("prupru",
                      TextToSpeech.QUEUE_ADD,
                      null,
                      "result");
              break;
          default:
              tts.setLanguage(Locale.ENGLISH);
              tts.speak ("I see " + recog,
                      TextToSpeech.QUEUE_ADD,
                      null,
                      "result");
              break;
      }
  }

    @Override
    public void onInit(int status) {

    }
}
