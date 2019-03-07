package izenka.hfad.com.bookstore.qr_code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.book.BookActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        initViews();
        Toast.makeText(this, "Разместите QR-код внутри области", Toast.LENGTH_LONG).show();
    }

    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();
        Intent intent = new Intent();
        intent.putExtra("bookID", Integer.valueOf(result.getText()));
        intent.setClass(this, BookActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
}
