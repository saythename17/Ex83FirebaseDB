package com.icandothisallday2020.ex83firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=findViewById(R.id.et);
        tv=findViewById(R.id.tv);


    }

    public void clickSend(View view) {
        //EditText 에 있는 글씨 얻어오기
        String text=et.getText().toString();

        //Firebase 실시간 DB에 저장
        //1.Firebase 실시간 DB 관리객체 얻어오기
        FirebaseDatabase db=FirebaseDatabase.getInstance();//서버연결완료
        //2.저장시킬 노드 참조객체 얻어오기
        DatabaseReference root=db.getReference();//최상위노드(루트) 얻어옴
        //3.해당노드에 값 설정
        /*3-1. 값 갱신
        root.setValue(text);
        et.setText("");
        //4. DB 값이 변경될 때마다 실시간으로 DB 값 읽어오기
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //값이 변경됐을 때 실행되는 메소드
                //(별도의 값을 읽어오기 위한 버튼,동작이 필요X. 실시간)
                //파라미터로 전달된 DataSnapshot 객체가 데이터를 가지고 옴
                String data=dataSnapshot.getValue(String.class);//가지고 올때 자료형을 지정할 수 있음
                tv.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*3-2.값 누적
        DatabaseReference ref1=root.push(); // 자식 노드(◇node◇)가 새로 생기고 그 참조객체 리턴
        ref1.setValue(text);

        //값 읽기
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //최상위에 내가보낸 최근의 데이터 값이 있지 않음(자식객체로 아래에 추가됨)
                //반복문을 이용해 값을 전부 읽어오기
                StringBuffer buffer=new StringBuffer();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String data= snapshot.getValue(String.class);
                    buffer.append(data+"\n");

                }
                tv.setText(buffer.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*3-3.자식 node 에 키값 지정
        final DatabaseReference dataRet=root.child("SlipIntoThe");
        //SlipIntoThe 의 자식으로 여러개의 값을 저장
        dataRet.push().setValue(text);

        //SlipIntoThe 노드의 변경만 읽어오면 되므로
        dataRet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s="";
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    String data=snap.getValue(String.class);
                    s+=data+"\n";
                }
                tv.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*3-4.하나의 node 에 여러개의 value
        DatabaseReference memberRef=root.child("member");
        DatabaseReference member=memberRef.push();//'member' 노드 아래에 임의의 식별자를 가진  자식 노드 생성
        member.child("name").setValue(text);
        member.child("saythename").setValue(17);*/

        /*3-5.VO(Value Object:나만의 클래스)를 만들어 한번에 멤버값 저장*/
        String name=et.getText().toString();
        int group=20;
        String address="Seoul";

        //저장할 값들을 하나의 객체로 생성
        Person person=new Person(name,group,address);
        //'SVT' 라는 이름의 자식노드를 참조하는 객체 생성 or 참조
        DatabaseReference personRef=root.child("SVT");
        personRef.push().setValue(person);

        //'SVT' 노드의 벨류가 변경되는 것만 듣는 리스너
        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Person p=snapshot.getValue(Person.class);
                    tv.append(p.name+","+p.age+","+p.address+"\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        et.setText("");
    }
}
