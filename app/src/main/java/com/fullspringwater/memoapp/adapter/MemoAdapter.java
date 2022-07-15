package com.fullspringwater.memoapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fullspringwater.memoapp.EditActivity;
import com.fullspringwater.memoapp.R;
import com.fullspringwater.memoapp.data.DatabaseHandler;
import com.fullspringwater.memoapp.model.Memo;

import java.util.List;

// 1. RycyclerView.Adapter 를 상속받는다.
// unimplemented method 모두 선택해서 넣는다.

// 5. RecyclerView.Adapter의 데이터 타입을 알려줘야 한다.
// 우리가 만든 ViewHolder 의 타입으로 설정 => 그러면 아래 빨간색이 뜬다.
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    // 4. 어댑터 클래스의 멤버 변수와 생성자를 만들어준다.
    Context context;
    List<Memo> memoList;

    int deleteIndex;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    // 6. 아래 함수들 구현

    // onCreate와 비슷한 기능
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_row, parent, false);

        return new MemoAdapter.ViewHolder(view);
    }

    // 메모리에 있는 데이터(리스트) 를 화면에 표시하는 함수
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memo memo = memoList.get(position);
        holder.txtTitle.setText(memo.title);
        holder.txtContent.setText(memo.content);
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    // 2. ViewHolder 클래스를 만든다.
    // 이 클래스는 contact_row.xml 화면에 있는 뷰를 연결시키는 클래스이다.
    // 빨간색 눌러서 생성자 만든다.
    // 화면과 연결할 자바 변수를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtContent;
        ImageView imgDelete;
        CardView cardView;

        // 3. 생성자 안에다 연결시키는 코드를 작성한다.
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // 카드뷰 클릭시 수정기능
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 카드뷰를 클릭하면 처리할 코드 작성

                    // 1. 유저가 몇번째 행을 클릭했는지, 인덱스로 알려준다.
                    int index = getAdapterPosition();

                    // 2. 이 인덱스에 저장되어있는 데이터를 가져온다.
                    Memo memo = memoList.get(index);

                    // 3. 아이디, 이름, 전화번호를, 수정하는 화면으로 데이터를 넘겨준다.
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("memo", memo);
//                    intent.putExtra("id", contact.id);
//                    intent.putExtra("name", contact.name);
//                    intent.putExtra("phone", contact.phone);
                    context.startActivity(intent);
                }
            });

            // 삭제기능
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // X 이미지 누르면, 해당 메모 삭제하도록 개발!

                    // 1. 어떤 행을 눌렀는지 정보를 얻어온다. 인덱스
                    deleteIndex = getAdapterPosition();

                    // 2. 알러트 다이얼로그를 띄운다.
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("메모 삭제");
                    alert.setMessage("정말 삭제하시겠습니까 ?");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseHandler db = new DatabaseHandler(context);
                            Memo memo = memoList.get(deleteIndex);
                            db.deleteMemo(memo);
                            db.close();

                            memoList.remove(deleteIndex);
                            notifyDataSetChanged();
                        }
                    });

                    // 부정버튼을 눌렀을때
                    // 리스너 메소드는 필요 없으니 null로 설정
                    alert.setNegativeButton("No", null);
                    alert.show();
                }
            });
        }

    }
}
