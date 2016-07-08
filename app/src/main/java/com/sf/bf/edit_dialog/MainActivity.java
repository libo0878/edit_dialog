package com.sf.bf.edit_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView videoview;
    private Button btn_send;
    private boolean isShow;
    private Dialog dialog = null;
    private EditText et_keywored;
    private TextView tv_textcount;
    private Button btn_sub;
    private String model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    public void initView() {
        model = android.os.Build.MODEL;
        MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoview = (VideoView) findViewById(R.id.videoview);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(MainActivity.this);
            }
        });
        if (videoview != null) {
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
        videoview.setVideoPath(Environment.getExternalStorageDirectory() + "/1.mp4");
    }


    //点击别的区域的时候,设置输入法选项
    public void showEditDialog(final Context context) {
        isShow = true;
        if (dialog == null) {
            dialog = new Dialog(context, R.style.dialog);
        }
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.view_edittext, null);
        et_keywored = (EditText) view.findViewById(R.id.et_keywored);
        tv_textcount = (TextView) view.findViewById(R.id.tv_textcount);
        btn_sub = (Button) view.findViewById(R.id.btn_sub);

        //设置展示范围
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().
                setAttributes(params);

        dialog.setContentView(view);
        dialog.show();

        et_keywored.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("s.toString()", s.toString());
                if (s.length() <= 10) {
                    tv_textcount.setText((10 - s.length()) + "");
                } else {
                    et_keywored.setText(s.subSequence(0, 10));
                    et_keywored.setSelection(10);
                    Toast.makeText(MainActivity.this, "字数已经超出限制", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        et_keywored.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                 @Override
                                                 public void onFocusChange(View v, boolean hasFocus) {
                                                     if (hasFocus) {
                                                         et_keywored.post(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                 imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                                                                 //自动弹出
                                                                 try {
                                                                     Thread.sleep(40);
                                                                 } catch (InterruptedException e) {

                                                                 }

                                                             }
                                                         });
                                                     }

                                                 }
                                             }
        );

        btn_sub.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           if (!TextUtils.isEmpty(et_keywored.getText().toString().trim())) {
                                               Toast.makeText(MainActivity.this, "发射成功!", Toast.LENGTH_SHORT).show();
                                               dialog.dismiss();
                                               hideKeyBoard();
                                           } else {
                                               Toast.makeText(MainActivity.this, "发射内容不能为空!", Toast.LENGTH_SHORT).show();
                                               return;
                                           }
                                       }
                                   }

        );

        //隐藏dialog的方法
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                       @Override
                                       public void onCancel(final DialogInterface dialog) {
                                           //小米手机
                                           if (model.contains("MI 3")) {
                                               et_keywored.post(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                       imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                                       try {
                                                           Thread.sleep(100);
                                                       } catch (Exception e) {

                                                       }

                                                   }
                                               });
                                           } else {
                                               //普通手机
                                               InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                               imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                           }
                                           isShow = false;
//                                           Log.e("Cancel", "per=" + per);
//                                           mVideoView.seekTo(per);
//                                           mVideoView.start();
//                                           mDanmakuView.resume();
                                       }
                                   }
        );

        et_keywored.setFocusable(true);
    }

    //隐藏键盘和输入框
    public void hideKeyBoard() {
        isShow = false;
        if (model.contains("MI 3")) {
            //小米手机
            et_keywored.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                }
            });
        } else {
            //普通手机
            InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

}
