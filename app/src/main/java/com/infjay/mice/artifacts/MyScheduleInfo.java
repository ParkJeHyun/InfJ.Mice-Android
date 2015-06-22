package com.infjay.mice.artifacts;

/**
 * Created by HJHOME on 2015-06-07.
 * 등록한 일정정보 저장
 */
public class MyScheduleInfo {
    public String scheduleSeq; //AI PK
    public String userSeq; //userseq, FK
    public String conferenceDate; //컨퍼런스데이트 라고 써있지만 그냥 일정 등록한 날짜
    public String startTime; //시작시간
    public String endTime; //끝나는 시간
    public String scheduleTitle; //일정 제목
    public String scheduleContents; //내용
    public String regDate; //등록일
    public String modDate; //수정일

}
