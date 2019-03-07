package izenka.hfad.com.bookstore.basket;


import android.os.Parcel;
import android.os.Parcelable;

public class BookIdAndCountModel implements Parcelable {
    public String bookID;
    public int count;

    protected BookIdAndCountModel(Parcel in) {
        bookID = in.readString();
        count = in.readInt();
    }

    public static final Creator<BookIdAndCountModel> CREATOR = new Creator<BookIdAndCountModel>() {
        @Override
        public BookIdAndCountModel createFromParcel(Parcel in) {
            return new BookIdAndCountModel(in);
        }

        @Override
        public BookIdAndCountModel[] newArray(int size) {
            return new BookIdAndCountModel[size];
        }
    };

    public BookIdAndCountModel(String bookID, int count) {
        this.bookID = bookID;
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bookID);
        parcel.writeInt(count);
    }
}
