# week5 : 메모장 앱
생명주기를 활용하여 메모장 앱 구현

- 화면 구성
  - 메모 화면 (MainActivity)
  - 확인 화면 (MemoActivity)
- 생명주기별 구현 기능
  - onCreate : 화면 생성
  - onResume : onPause에서 저장한 전역변수(memoBuffer) 내용으로 editText 내용으로 설정
    - 변수 값이 비어있다면 아무것도 안하기
  - onPause : 현재끼지 작성한 내용 Activity의 전역변수에 담아두기
  - onRestart : Dialog을 활용하여 다시 작성할 거냐고 묻는 창 띄우기
    - 다시 작성하지 않겠다고 하면 onPause에서 저장했던 변수 비우기