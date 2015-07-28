package com.infjay.mice.artifacts;

/**
 * Created by HJHOME on 2015-06-07.
 * 스폰서 정보 담는 클래스
 */
public class SponsorInfo {
    public String sponsorSeq; //AI, PK
    public String sponsorName; //스폰서이름
    public String logoPath; //목록에 나올 이미지의 로고 경로
    public String detailImagePath; //전면광고 이미지 경로
    public String sponsorExplanation; //스폰서 설명
    public String regDate;
    public String modDate;
    public String attached;

    public String getName(){
        return sponsorName;
    }
}
