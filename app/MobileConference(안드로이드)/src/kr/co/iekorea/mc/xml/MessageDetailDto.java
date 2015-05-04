package kr.co.iekorea.mc.xml;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageDetailDto implements Parcelable{
	public int MESSAGE_ID;
	public String MESSAGE_TYPE;
	public String TO_USER_NAME;
	public int TO_USER_CD;
	public String FROM_USER_NAME;
	public int FROM_USER_CD;
	public String TITLE;
	public String CONTENTS;
	public String REPLY;
	public String REG_DATE;
	
	
	public MessageDetailDto(){}
	
	public MessageDetailDto(Parcel in) {
		readFromParcel(in);
	}
	
	
	@Override
	public int describeContents() {
		
		return 0;
	}
	
	private void readFromParcel(Parcel in) {
		
		MESSAGE_ID = in.readInt();
		TO_USER_CD = in.readInt();
		FROM_USER_CD = in.readInt();
		MESSAGE_TYPE = in.readString();
		TO_USER_NAME = in.readString();
		FROM_USER_NAME = in.readString();
		TITLE = in.readString();
		CONTENTS = in.readString(); 
		REPLY = in.readString();
		REG_DATE = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(MESSAGE_ID);
		dest.writeInt(TO_USER_CD);
		dest.writeInt(FROM_USER_CD);
		dest.writeString(MESSAGE_TYPE);
		dest.writeString(TO_USER_NAME);
		dest.writeString(FROM_USER_NAME);
		dest.writeString(TITLE);
		dest.writeString(CONTENTS);
		dest.writeString(REPLY);
		dest.writeString(REG_DATE);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public MessageDetailDto createFromParcel(Parcel in) {
			return new MessageDetailDto(in);
		}

		public MessageDetailDto[] newArray(int size) {
			return new MessageDetailDto[size];
		}
	};
	
}
