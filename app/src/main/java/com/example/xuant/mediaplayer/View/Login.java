package com.example.xuant.mediaplayer.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuant.mediaplayer.Database.DBUser;
import com.example.xuant.mediaplayer.Database.GetUserListener;
import com.example.xuant.mediaplayer.Model.Server.User;
import com.example.xuant.mediaplayer.R;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener,GetUserListener{

    ImageView login_back;
    EditText login_user,login_pass;
    TextView login_button;

    DBUser dbUser;
    String username="",pass="";
    public static User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_back = (ImageView)findViewById(R.id.login_back);
        login_user = (EditText)findViewById(R.id.login_user);
        login_pass = (EditText)findViewById(R.id.login_pass);
        login_button = (TextView)findViewById(R.id.login_button);

        login_button.setOnClickListener(this);

        dbUser = new DBUser(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_button:  //Sự kiện khi click đăng nhâp
                username = login_user.getText().toString();
                pass = login_pass.getText().toString();

                if(username.trim().length() > 0 && pass.trim().length() > 0)  // Kiểm tra chuỗi nhập vào có đầy đủ
                { // Nếu ok thì get user theo username
                    dbUser.getUser(username);
                }
                else
                {
                    Toast.makeText(Login.this,"Nhập đầy đủ email và pass", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    // Lấy user từ dữ liệu nhập vào
    @Override
    public void getUser(ArrayList<User> userArrayList) {
        try{
            user = userArrayList.get(0);
            Log.e("","Đang kiểm tra pass "+user.getName());
            if(!user.getUsername().equals(username) || !user.getPass().equals(pass))  // Kiểm tra user có tồn tại
            {  // Kiểm tra email và pass có trùng khớp với tài khoản hiện tại
                Toast.makeText(Login.this,"Password hoặc mật khẩu không đúng !", Toast.LENGTH_LONG).show();
            }
            else
            {  // Nếu không trùn thì truyền dữ liệu và trả về activity trước
                this.user = user;
                Intent data = new Intent();
                data.putExtra("username",user.getUsername());  //
                data.putExtra("id",user.getId());  //
                setResult(RESULT_OK,data);
                Toast.makeText(Login.this,"Đăng nhập thành công \n đang chuyển hướng !", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        }catch (Exception e){
            Toast.makeText(Login.this,"Password hoặc mật khẩu không đúng !", Toast.LENGTH_LONG).show();
        }
    }
}
