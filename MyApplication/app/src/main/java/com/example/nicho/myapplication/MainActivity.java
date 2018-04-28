package com.example.nicho.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final TextView textView = findViewById(R.id.text);
        final SpannableString spannableString = new SpannableString(textView.getText().toString());
        CustomBackgroundColorSpan span = new CustomBackgroundColorSpan(0xffffe9f5, 0xFFFEA2D6, 20, 22, 98, textView);
        span.setProgress(0);
        spannableString.setSpan(span, 22, 98, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(span, CUSTOM_BACKGROUND_COLOR_SPAN_FC_PROPERTY, 0, 1000);
        objectAnimator.setDuration(10000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //refresh
                textView.setText(spannableString);
            }
        });
        objectAnimator.start();
        Drawable image = ContextCompat.getDrawable(this, R.drawable.vidy_space);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 99, 100, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

        final ImageView imageView = findViewById(R.id.image);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.pink_tick);

        final ClipDrawable clipDrawable = new ClipDrawable(new BitmapDrawable(getResources(), icon), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        clipDrawable.setLevel(0);
        imageView.setBackground(clipDrawable);
        imageView.setImageResource(android.R.color.transparent);

        final ValueAnimator anim = ValueAnimator.ofInt(0, 10000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                clipDrawable.setLevel(val);
            }
        });
        anim.setStartDelay(10000);
        anim.setDuration(200);
        anim.start();

    }

    private static final Property<CustomBackgroundColorSpan, Integer> CUSTOM_BACKGROUND_COLOR_SPAN_FC_PROPERTY =
            new Property<CustomBackgroundColorSpan, Integer>(Integer.class, "CUSTOM_BACKGROUND_COLOR_SPAN_FC_PROPERTY") {

                @Override
                public void set(CustomBackgroundColorSpan span, Integer value) {
                    span.setProgress(value);
                }

                @Override
                public Integer get(CustomBackgroundColorSpan span) {
                    return span.getProgress();
                }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
