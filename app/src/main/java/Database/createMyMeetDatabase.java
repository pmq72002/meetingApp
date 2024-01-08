package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class createMyMeetDatabase extends SQLiteOpenHelper {
    public static String TB_MYMEET = "MYMEET";

    public createMyMeetDatabase(@Nullable Context context) {
        super(context, "MyMeet", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
