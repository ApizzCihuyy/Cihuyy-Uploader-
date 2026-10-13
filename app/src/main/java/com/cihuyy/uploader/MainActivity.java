package com.cihuyy.uploader;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

private TextView videoInfo;
private Uri selectedVideo;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(32, 48, 32, 32);
    layout.setGravity(Gravity.CENTER);
    layout.setBackgroundColor(Color.rgb(18, 18, 24));

    TextView title = new TextView(this);
    title.setText("CIHUYY UPLOADER");
    title.setTextSize(27);
    title.setTextColor(Color.WHITE);
    title.setGravity(Gravity.CENTER);
    title.setPadding(0, 0, 0, 16);
    layout.addView(title);

    TextView subtitle = new TextView(this);
    subtitle.setText("Upload video dengan file asli");
    subtitle.setTextSize(15);
    subtitle.setTextColor(Color.LTGRAY);
    subtitle.setGravity(Gravity.CENTER);
    subtitle.setPadding(0, 0, 0, 32);
    layout.addView(subtitle);

    Button chooseButton = new Button(this);
    chooseButton.setText("PILIH VIDEO");
    layout.addView(chooseButton);

    videoInfo = new TextView(this);
    videoInfo.setText("Belum ada video dipilih");
    videoInfo.setTextSize(14);
    videoInfo.setTextColor(Color.WHITE);
    videoInfo.setGravity(Gravity.CENTER);
    videoInfo.setPadding(0, 24, 0, 24);
    layout.addView(videoInfo);

    TextView status = new TextView(this);
    status.setText(
        "Status: Siap\n\n" +
        "Pilih video dari galeri untuk memulai."
    );
    status.setTextSize(14);
    status.setTextColor(Color.LTGRAY);
    status.setGravity(Gravity.CENTER);
    layout.addView(status);

    chooseButton.setOnClickListener(v -> {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 100);
    });

    setContentView(layout);
}

@Override
protected void onActivityResult(
        int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 100 &&
            resultCode == RESULT_OK &&
            data != null) {

        selectedVideo = data.getData();

        if (selectedVideo != null) {
            String name = "Video dipilih";
            String size = "Ukuran tidak diketahui";

            Cursor cursor = getContentResolver().query(
                selectedVideo,
                null,
                null,
                null,
                null
            );

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                        );
                        int sizeIndex = cursor.getColumnIndex(
                            OpenableColumns.SIZE
                        );

                        if (nameIndex >= 0) {
                            name = cursor.getString(nameIndex);
                        }

                        if (sizeIndex >= 0 &&
                                !cursor.isNull(sizeIndex)) {
                            long bytes = cursor.getLong(sizeIndex);
                            size = String.format(
                                java.util.Locale.US,
                                "%.2f MB",
                                bytes / (1024.0 * 1024.0)
                            );
                        }
                    }
                } finally {
                    cursor.close();
                }
            }

            videoInfo.setText(
                "Video: " + name + "\nUkuran: " + size
            );

            Toast.makeText(
                this,
                "Video berhasil dipilih!",
                Toast.LENGTH_SHORT
            ).show();
        }
    }
}

}
