package com.fullspringwater.memoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fullspringwater.memoapp.model.Memo;
import com.fullspringwater.memoapp.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 테이블 생성
        String CREATE_CONTACT_TABLE = "create table " + Util.TABLE_NAME + "(" +
                Util.KEY_ID + " integer primary key, " +
                Util.KEY_TITLE + " text, " +
                Util.KEY_CONTENT + " text)";

        Log.i("MyMemo", "테이블 생성문 : " + CREATE_CONTACT_TABLE);

        // 쿼리 실행
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 기존의 memo 테이블을 삭제하고,
        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        // 새롭게 테이블을 다시 만든다
        onCreate(sqLiteDatabase);
    }

    // 우리가 앱 동작 시키는데 필요한 SQL 문이 적용된 함수들을 만든다.
    // CRUD 관련 함수들을 만든다.
    public void addMemo(Memo memo){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();

        // 테이블의 컬럼이름과 해당 데이터를 매칭해서 넣어준다.
        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, memo.title);
        values.put(Util.KEY_CONTENT, memo.content);
        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    // 메모 데이터 전체 가져오기
    // select * from memo;
    public ArrayList<Memo> getAllMemo(){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME + ";", null);

        ArrayList<Memo> memoList = new ArrayList<>();

//        if(cursor.moveToFirst()) {
//            for(int i=0; i< cursor.getCount(); i++) {
//                Contact contact = new Contact(
//                        cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//                contactList.add(contact);
//                cursor.moveToNext();
//            }
//        }

        if(cursor.moveToFirst()){
            do{
                Memo contact = new Memo(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                memoList.add(contact);
            }while(cursor.moveToNext());
        }

        db.close();
        return memoList;

    }

    // 검색기능
    // 특정 내용이 들어있는  메모 가져오기
    // select * from memo where content like %keyword% ;
    public ArrayList<Memo> getSearchedMemo(String keyword){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME +
                " where " + Util.KEY_CONTENT +
                " like '%" + keyword + "%';", null);

        ArrayList<Memo> memoList = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Memo contact = new Memo(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                memoList.add(contact);
            }while(cursor.moveToNext());
        }

        db.close();
        return memoList;
    }

    // 데이터 수정하는 함수
    public void updateMemo(Memo memo){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();

        // 방법 1
//        ContentValues values = new ContentValues();
//        values.put(Util.KEY_NAME, contact.name);
//        values.put(Util.KEY_PHONE, contact.phone);
//
//        db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
//                new String[]{contact.id+""});

        // 방법 2
        // 쿼리문 만든다.
        db.execSQL("update " + Util.TABLE_NAME +
                        " set " + Util.KEY_TITLE + "= ?, " +  Util.KEY_CONTENT + " = ? " +
                        "where " + Util.KEY_ID + "= ?;",
                new String[]{memo.title, memo.content, memo.id+""});

        // 방법 3
//        db.execSQL("update " + Util.TABLE_NAME +
//                        " set " + Util.KEY_NAME + "= " + contact.name +
//                        ", " +  Util.KEY_PHONE + " = " + contact.phone +
//                        "where " + Util.KEY_ID + "= " + contact.id);
        db.close();
    }

    // 데이터 삭제하는 함수
    public void deleteMemo(Memo memo){
        // delete from contact where id= 1;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Util.TABLE_NAME +
                " where " + Util.KEY_ID + " = ?;", new String[]{memo.id+""});
        db.close();
    }
}
