package com.fullspringwater.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fullspringwater.memoapp.adapter.MemoAdapter;
import com.fullspringwater.memoapp.data.DatabaseHandler;
import com.fullspringwater.memoapp.model.Memo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MemoAdapter adapter;
    ArrayList<Memo> memoList;
    EditText editKeyword;
    ImageView imgSearch;
    ImageView imgCancel;

    // 깃허브 연동완료
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        imgSearch = findViewById(R.id.imgSearch);
        editKeyword = findViewById(R.id.editKeyword);

        // 검색버튼 눌렀을 때
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editKeyword.getText().toString().trim();

                if(keyword.isEmpty()){
                    return;
                }
                // DB에서 주소록 데이터중 keyword를 포함한 데이터를 모두 가져와서,
                // 리사이클러뷰에 표시한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.getSearchedMemo(keyword);
                adapter = new MemoAdapter(MainActivity.this, memoList);

                recyclerView.setAdapter(adapter);

                // editKeyword의 문자열을 지운다.
                editKeyword.setText("");
            }
        });

        imgCancel = findViewById(R.id.imgCancel);
        // 취소버튼 눌렀을 때
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editKeyword.getText().toString().trim();
                // DB에서 주소록 데이터를 모두 가져와서, 리사이클러뷰에 표시한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.getAllMemo();
                adapter = new MemoAdapter(MainActivity.this, memoList);

                recyclerView.setAdapter(adapter);
                // editKeyword의 문자열을 지운다.
                editKeyword.setText("");
            }
        });

        editKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 키워드 검색 에디트텍스트에 글자를 쓸 때마다,
                // 자동으로 해당 검색어를 가져와서, 디비에서 쿼리해서
                // 검색 결과를 화면에 표시해주는 기능 개발
                // 부하가 많이 걸리는 방법
                // 상황에 맞게 사용해야한다.
                String keyword = editKeyword.getText().toString().trim();

                if(keyword.length()<2){return;}

                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                // DB에서 주소록 데이터중 keyword를 포함한 데이터를 모두 가져와서,
                // 리사이클러뷰에 표시한다.
                memoList = db.getSearchedMemo(keyword);
                adapter = new MemoAdapter(MainActivity.this, memoList);

                recyclerView.setAdapter(adapter);



            }
        });
        printSearchDBData();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // DB에서 주소록 데이터를 모두 가져와서, 리사이클러뷰에 표시한다.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        // 메모 리스트에는 데이터가 없습니다.
        // DB에 있습니다.
        // 따라서 먼저, 디비에 저장된 메모리스트를 가져옵니다.
        memoList = db.getAllMemo();
        adapter = new MemoAdapter(MainActivity.this, memoList);

        recyclerView.setAdapter(adapter);

        printDBData();
    }
    // DB에 있는 모든 주소록 데이터를, 디버깅용으로 로그에 출력하는 함수
    void printDBData(){
        // 데이터베이스에 데이터 넣고, 가져오는 것 테스트
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        // 주소록 데이터를 디비에서 가져와서, 로그 찍어보자
        ArrayList<Memo> contactList = db.getAllMemo();

        for(Memo data : contactList){
            Log.i("MyContact: ", "id : " + data.id +
                    ", title : " + data.title + ", content : " + data.content);
        }
    }
    void printSearchDBData(){
        // 데이터베이스에 데이터 넣고, 가져오는 것 테스트
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        // 주소록 데이터를 디비에서 가져와서, 로그 찍어보자
        ArrayList<Memo> contactList = db.getSearchedMemo("ti");

        for(Memo data : contactList){
            Log.i("MyContact: ", "search result id : " + data.id +
                    ", title : " + data.title + ", content : " + data.content);
        }
    }
}