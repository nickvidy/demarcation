package com.example.nicho.myapplication;

/**
 * Created by nicho on 4/26/2018.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomBackgroundColorSpan implements LineBackgroundSpan {
    private float padding;
    private float radius;

    private RectF rect = new RectF();
    private Paint paint = new Paint();
    private Paint paintProgress = new Paint();

    Rect[] rects;

    public CustomBackgroundColorSpan(int backgroundColor, int progressColor,
                                     float radius, int startIndex, int endIndex, TextView textView) {
        this.radius = radius;

        paint.setColor(backgroundColor);
        paintProgress.setColor(progressColor);

        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.textView = textView;
    }

    int startIndex;
    int endIndex;
    TextView textView;

    private int progress;
    private int totalWidth;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private int max_progress = 1000;

    @Override
    public void drawBackground( final Canvas c, final Paint p, final int left, final int right, final int top, final int baseline, final int bottom, final CharSequence text, final int start, final int end, final int lnum) {
        //Log.d("TEST", "left: "+left+" right: "+right+ " top: "+top+" baseline: "+baseline+" bottom: "+bottom+ " start: "+start+" end: "+end+" lnum: "+lnum);

        if(rects==null || rects.length==0) {
            rects = getLocationRects(startIndex, endIndex, textView);
        }

        rect.set(left, top, right, bottom);
        List<RectF> rectFs = new ArrayList<>();

        totalWidth = 0;
        for(Rect r: rects) {
            rect = new RectF();
            rect.set(r.left, r.top, r.right, r.bottom);
            rectFs.add(rect);
            totalWidth += r.right - r.left;
            c.drawRoundRect(rect, radius, radius, paint);
        }

        if(progress > 0) {
            float progressWidth = totalWidth * progress / max_progress;
            int currentWidth = 0;
            for (RectF rf : rectFs) {
                currentWidth += rf.right-rf.left;
                if(progressWidth < currentWidth) {
                    rf.right -= currentWidth-progressWidth;
                    c.drawRoundRect(rf, radius, radius, paintProgress);
                    break;
                }
                else {
                    c.drawRoundRect(rf, radius, radius, paintProgress);
                }
            }
        }
    }

    int startRawY;
    int endRawY;

    private Rect[] getLocationRects(int startIndex, int endIndex, final TextView parentTextView) {

        Layout textViewLayout = parentTextView.getLayout();
        int currentLineStartOffset = textViewLayout.getLineForOffset(startIndex);
        int currentLineEndOffset = textViewLayout.getLineForOffset(endIndex);
        int numOfLines = currentLineEndOffset - currentLineStartOffset + 1;

        Rect[] rect = new Rect[numOfLines];

        float startPrimary = textViewLayout.getPrimaryHorizontal(startIndex);
        float endPrimary = textViewLayout.getPrimaryHorizontal(endIndex) + getCharacterWidth(parentTextView, endIndex);

        //Get height of line and Y
        float lineHeight = parentTextView.getLineHeight();
        int top = (int) (currentLineStartOffset * lineHeight);

        startRawY = top;
        endRawY = top + (int) lineHeight*(numOfLines+1);

        //Factor in scrollview location
        View view = (View) parentTextView.getParent();
        if (view instanceof ScrollView) {
            int scrollY = view.getScrollY();
            top -= scrollY;
        }

        top -= parentTextView.getScrollY();

        //Set Rects for all lines
        for(int i = 0; i < rect.length; i++) {
            rect[i] = new Rect();
            rect[i].left = 0;
            rect[i].right = parentTextView.getWidth();
            rect[i].top = top;

            //Add underline height
            rect[i].bottom = top+(int)lineHeight+4;
            top+=(int)lineHeight;

            //Remove top margin for characters with tails
            if (i <= 1) {
                rect[i].top += 4;
            }
        }

        //Set first and last character position
        rect[0].left = (int) startPrimary;
        rect[rect.length-1].right = (int) endPrimary;

        return rect;
    }

    private float getCharacterWidth(TextView textView, int index) {
        Paint textPaint = textView.getPaint();
        return textPaint.measureText(textView.getText().toString().substring(index, index + 1));
    }

}