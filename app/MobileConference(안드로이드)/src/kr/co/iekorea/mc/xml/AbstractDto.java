package kr.co.iekorea.mc.xml;

import android.os.Parcel;
import android.os.Parcelable;

public class AbstractDto implements Parcelable {
	public int BINDER_ID;
	public String WRITER;
	public String TOPIC_TITLE;
	public String BINDER_TITLE;
	public int AGEND_ID;
	public String PRESENTER;
	public int TOPIC_ID;
	public int USER_CD;
	public String USER_ID;
	public String USER_NAME;
	
	public AbstractDto(){}
	
	public AbstractDto(Parcel in) {
		readFromParcel(in);
	}
	
	
	@Override
	public int describeContents() {
		
		return 0;
	}
	
	private void readFromParcel(Parcel in) {
		BINDER_ID = in.readInt();
		WRITER = in.readString();
		TOPIC_TITLE = in.readString();
		BINDER_TITLE = in.readString();
		PRESENTER = in.readString();
		AGEND_ID = in.readInt();
		TOPIC_ID = in.readInt();
		USER_ID = in.readString();
		USER_NAME = in.readString();
		USER_CD = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(WRITER);
		dest.writeString(TOPIC_TITLE);
		dest.writeString(BINDER_TITLE);
		dest.writeString(PRESENTER);
		dest.writeInt(BINDER_ID);
		dest.writeInt(AGEND_ID);
		dest.writeInt(TOPIC_ID);
		dest.writeString(USER_ID);
		dest.writeString(USER_NAME);
		dest.writeInt(USER_CD);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public AbstractDto createFromParcel(Parcel in) {
			return new AbstractDto(in);
		}

		public AbstractDto[] newArray(int size) {
			return new AbstractDto[size];
		}
	};
}
