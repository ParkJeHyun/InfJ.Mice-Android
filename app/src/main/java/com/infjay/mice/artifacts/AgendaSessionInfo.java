package com.infjay.mice.artifacts;

/**
 * Created by HJHOME on 2015-06-07.
 * 세션정보담는 클래스
 */

public class AgendaSessionInfo {
    public String agendaSessionSeq; //AI, PK
    public String sessionTitle; //세션 제목
    public String sessionContents; //세션 내용
    public String sessionSumarry; //세션 요약
    public String sessionWriterUserSeq; //세션 작성자 Key
    public String sessionPresenterUserSeq; //세션 발표자 Key
    public String sessionStartTime; //세션 시작날짜시간
    public String sessionEndTime; //세션 종료날짜시간
    public String sessionAttached; //세션 첨부파일 .pdf
    public String regDate; //레코드 등록일
    public String modDate; //레코드 수정일

}
