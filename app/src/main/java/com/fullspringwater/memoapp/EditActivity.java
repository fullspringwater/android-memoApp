package com.fullspringwater.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullspringwater.memoapp.data.DatabaseHandler;
import com.fullspringwater.memoapp.model.Memo;

public class EditActivity extends AppCompatActivity {

    Memo memo;

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 다른 액티비티로부터 넘겨받은 데이터가 있으면, 이 데이터를 먼저 처리하자.
        memo = (Memo) getIntent().getSerializableExtra("memo");

        // 화면과 변수 연결
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        editTitle.setText(memo.title);
        editContent.setText(memo.content);

        // 버튼 클릭시 처리할 코드 작성 : DB에 저장하고,
        // 액티비티 종료해서, 메인으로 돌아감
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 유저가 입력한 제목과 내용 가져오기
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                if(title.isEmpty()){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. 메모 객체로 만들기
                memo.title = title;
                memo.content = content;

                // 3. 디비에 저장하기
                // 3-1. 디비 핸들러 가져오기
                DatabaseHandler db = new DatabaseHandler(EditActivity.this);

                // 3-2 메모 데이터를 업데이트 하는 함수 호출
                // 업데이트에 필요한 파라미터는 Memo 클래스의 객체다
                db.updateMemo(memo);
                db.close();
                // 3-3 토스트 보여주기
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                // 4. 액티비티 종료하기
                finish();

            }
        });
    }
}