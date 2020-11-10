package com.example.draw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button bPoint;
    private Button bLine;
    private Button bCline;
    private Button bRect;
    private Button bOval;
    private static final int toolbarY = 190;
    private static final int GALARY_REQUEST_CODE =123;
    private  MyCanvas myCanvas;
    public enum DrawObject { point, line, curline, rect, oval};
    private DrawObject current = DrawObject.point;
    private int currentColor = Color.BLACK;
    private Path path = new Path();
    private boolean fill = true;
    float sX = -1;
    float sY = -1;
    float fX = -1;
    float fY = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.activity_main);

        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        myCanvas.setBackgroundColor(Color.WHITE);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
            myCanvas.setImageURI((Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bPoint = (Button) findViewById(R.id.bPoint);
        bPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = DrawObject.point;
                updateMyCanvas();
            }
        });

        bLine  = (Button) findViewById(R.id.bLine);
        bLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = DrawObject.line;
                updateMyCanvas();
            }
        });
        bCline = (Button) findViewById(R.id.bCline);
        bCline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = DrawObject.curline;
                updateMyCanvas();
            }
        });
        bRect  = (Button) findViewById(R.id.bRect);
        bRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = DrawObject.rect;
                updateMyCanvas();
            }
        });
        bOval  = (Button) findViewById(R.id.bOval);
        bOval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = DrawObject.oval;
                updateMyCanvas();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pointItem:
                current = DrawObject.point;
                break;
            case R.id.lineItem:
                current = DrawObject.line;
                break;
            case R.id.curlineItem:
                current = DrawObject.curline;
                break;
            case R.id.rectItem:
                current = DrawObject.rect;
                break;
            case R.id.ovalItem:
                current = DrawObject.oval;
                break;

            case R.id.newItem:
                myCanvas.clear();
                break;
            case R.id.openItem:
                open();
                break;
            case R.id.saveItem:
                save(true);
                break;
            case R.id.shareItem:
                share();
                break;
            case R.id.undoItem:
                myCanvas.undo();
                break;
            case R.id.quitItem:
                finish();
                System.exit(0);
                break;

            case R.id.redItem:
                currentColor = Color.RED;
                break;
            case R.id.greenItem:
                currentColor = Color.GREEN;
                break;
            case R.id.blueItem:
                currentColor = Color.BLUE;
                break;
            case R.id.yellowItem:
                currentColor = Color.YELLOW;
                break;
            case R.id.blackItem:
                currentColor = Color.BLACK;
                break;
            case R.id.whiteItem:
                currentColor = Color.WHITE;
                break;

            case R.id.fillItem:
                fill = true;
                break;
            case R.id.strokeItem:
                fill = false;
                break;
        }
        updateMyCanvas();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (current != DrawObject.curline) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sX = event.getX();
                    sY = event.getY() - toolbarY;
                    myCanvas.setCurrentObjectCoordinates(sX, sY, sX, sY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    myCanvas.setCurrentObjectCoordinates(sX, sY, event.getX(), event.getY() - toolbarY);
                    break;
                case MotionEvent.ACTION_UP:
                    fX = event.getX();
                    fY = event.getY() - toolbarY;
                    float[] coordinates = new float[]{sX, sY, fX, fY};
                    myCanvas.addObject(new Object(current, coordinates, currentColor, fill));
                    break;
            }
        }else if (current == DrawObject.curline){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(event.getX(), event.getY() - toolbarY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(event.getX(), event.getY() - toolbarY);
                    break;
                case MotionEvent.ACTION_UP:
                    myCanvas.addObject(new Object(DrawObject.curline, path, currentColor));
                    this.path = new Path();
                    break;
            }
            myCanvas.currentPath(path);
        }
        myCanvas.invalidate();
        return super.onTouchEvent(event);
    }

    private void open(){
        myCanvas.clear();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALARY_REQUEST_CODE);
    }

    private String save(boolean flag){
        myCanvas.setDrawingCacheEnabled(true);
        String name = UUID.randomUUID().toString();
        String imgSave = MediaStore.Images.Media.insertImage(getContentResolver(),
                myCanvas.getDrawingCache(), name,"drawing");
        if (flag) {
            if (imgSave != null) {
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error! Image not save!", Toast.LENGTH_SHORT).show();
            }
        }
        myCanvas.setDrawingCacheEnabled(false);
        return name;
    }

    private void share(){
        String name = save(false);
        File image = new File("/storage/emulated/0/Pictures/" + name + ".jpg");
        Uri uri = (Uri) Uri.fromFile(image);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, null));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            myCanvas.setImageURI(data.getData());
        }
    }

    void updateMyCanvas(){
        myCanvas.setColor(currentColor);
        myCanvas.setCurrentType(current);
        myCanvas.setFill(fill);
    };
}