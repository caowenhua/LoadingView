package org.cwh;

import android.app.Activity;
import android.os.Bundle;

import org.cwh.widget.CropImageView;

public class MainActivity2 extends Activity {

    CropImageView crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        crop = (CropImageView) findViewById(R.id.crop);
        crop.setCropMode(CropImageView.CropMode.CIRCLE);
        crop.setImageResource(R.mipmap.sample1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
