package com.fullspringwater.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullspringwater.memoapp.data.DatabaseHandler;
import com.fullspringwater.memoapp.model.Memo;

public class AddActivity extends AppCompatActivity {
    EditText editTitle;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 제목과 내용 가져오기
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                if(title.isEmpty()){
                    Toast.makeText(AddActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2. 메모 객체로 만들기
                Memo memo = new Memo(title, content);

                // 3. 디비에 저장하기
                // 3-1. 디비 핸들러 가져오기
                // 데이터베이스에 데이터 넣고, 가져오는 것 테스트
                DatabaseHandler db = new DatabaseHandler(AddActivity.this);

                // 3-2 메모 데이터를 디비에 저장하는 함수 호출
                db.addMemo(memo);

                db.close();
                // 3-3 토스트 보여주기
                Toast.makeText(AddActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

                // 4. 액티비티 종료하기
                finish();
            }
        });
    }
}