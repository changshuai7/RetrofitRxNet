package com.shuai.retrofitrx.net.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by liuqing on 18/2/6.
 */

public class DownloadDBDao extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_DOWNLOAD = "table_downloadinfo";
    private String CREATE_TABLE_DOWNLOAD =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOAD
                    + "(" + DownloadRecord.DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + DownloadRecord.DB_FILEPATH + " TEXT DEFAULT NULL,"
                    + DownloadRecord.DB_DOWNLOADURL + " TEXT DEFAULT NULL,"
                    + DownloadRecord.DB_STATUS + " INTEGER DEFAULT " + DownloadRecord.STATUS_UNKNOW + ","
                    + DownloadRecord.DB_DOWNLOADEDLENGTH + " TEXT DEFAULT NULL,"
                    + DownloadRecord.DB_CONTENTLENGTH + " TEXT DEFAULT NULL,"
                    + DownloadRecord.DB_UPDATETIME + " TEXT DEFAULT NULL,"
                    + DownloadRecord.DB_TAG + " TEXT DEFAULT NULL);";

//    private static DownloadDBDao instance;

//    public static DownloadDBDao getInstance(Context ctx) {
//        if (instance == null) {
//            synchronized (DownloadDBDao.class) {
//                if (instance == null) {
//                    instance = new DownloadDBDao(ctx);
//                }
//            }
//        }
//        return instance;
//    }


    public long insertDownloadRecord(DownloadRecord record) {
        synchronized (this) {
            SQLiteDatabase database = getWritableDatabase();
            try {
                ContentValues contentValues = record.asContentValues();
                return database.insert(TABLE_DOWNLOAD, null, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeDatabaseQuietly(database);
            }
            return 0l;
        }
    }

    public boolean isExistRecord(String filePath, String downloadUrl) {
        synchronized (this) {
            boolean result = false;
            if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(downloadUrl)) {
                SQLiteDatabase database = getWritableDatabase();
                Cursor cursor = database.query(TABLE_DOWNLOAD, null, DownloadRecord.DB_FILEPATH + " = ? and " + DownloadRecord.DB_DOWNLOADURL + " = ? ", new String[]{filePath, downloadUrl}, null, null, null);
                result = cursor.getCount() > 0;
            }
            return result;
        }
    }

    public DownloadRecord getDownloadRecord(String filePath, String downloadUrl) {
        synchronized (this) {
            DownloadRecord record = null;
            if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(downloadUrl)) {
                SQLiteDatabase database = getWritableDatabase();
                Cursor cursor = database.query(TABLE_DOWNLOAD, null, DownloadRecord.DB_FILEPATH + " = ? and " + DownloadRecord.DB_DOWNLOADURL + " = ? ", new String[]{filePath, downloadUrl}, null, null, null);
                record = DownloadRecord.cur2Record(cursor);
            }
            return record;
        }
    }

    public long[] insertDownloadRecords(List<DownloadRecord> records) {
        long[] ids = null;
        if (records != null && !records.isEmpty()) {
            ids = new long[records.size()];
            synchronized (this) {
                SQLiteDatabase database = getWritableDatabase();
                try {
                    database.beginTransaction();
                    for (int i = 0; i < records.size(); i++) {
                        DownloadRecord itemRecord = records.get(i);
                        ids[i] = database.insert(TABLE_DOWNLOAD, null, itemRecord.asContentValues());
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeDatabaseQuietly(database);
                }
            }
        }
        return ids;
    }

    public int updateDownloadRecord(DownloadRecord record) {
        synchronized (this) {
            int updateCount = 0;
            SQLiteDatabase database = getWritableDatabase();
            try {
                updateCount = database.update(TABLE_DOWNLOAD, record.asContentValues(), DownloadRecord.DB_ID + " = ? ", new String[]{String.valueOf(record.getId())});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeDatabaseQuietly(database);
                return updateCount;
            }
        }
    }

    public List<DownloadRecord> getDownloadRecords() {
        synchronized (this) {
            SQLiteDatabase database = getReadableDatabase();
            List<DownloadRecord> records = null;
            Cursor cursor = null;
            try {
                cursor = database.query(TABLE_DOWNLOAD, null, null, null, null, null, DownloadRecord.DB_UPDATETIME + " DESC ");
                records = DownloadRecord.cur2Records(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCursorQuietly(cursor);
                closeDatabaseQuietly(database);
            }
            return records;
        }
    }

    public List<DownloadRecord> getDownloadRecordsByState(int state) {
        synchronized (this) {
            SQLiteDatabase database = getReadableDatabase();
            List<DownloadRecord> records = null;
            Cursor cursor = null;
            try {
                cursor = database.query(TABLE_DOWNLOAD, null, DownloadRecord.DB_STATUS + " = ? ", new String[]{String.valueOf(state)}, null, null, DownloadRecord.DB_UPDATETIME + " DESC ");
                records = DownloadRecord.cur2Records(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCursorQuietly(cursor);
                closeDatabaseQuietly(database);
            }
            return records;
        }
    }

    public DownloadRecord getDownloadRecordByID(int id) {
        synchronized (this) {
            SQLiteDatabase database = getReadableDatabase();
            DownloadRecord record = null;
            Cursor cursor = null;
            try {
                cursor = database.query(TABLE_DOWNLOAD, null, DownloadRecord.DB_ID + " = ? ", new String[]{String.valueOf(id)}, null, null, DownloadRecord.DB_UPDATETIME + " DESC ");
                record = DownloadRecord.cur2Record(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCursorQuietly(cursor);
                closeDatabaseQuietly(database);
            }
            return record;
        }
    }

    public int delRecordByID(int id) {
        synchronized (this) {
            int delCount = 0;
            SQLiteDatabase database = getWritableDatabase();
            try {
                delCount = database.delete(TABLE_DOWNLOAD, DownloadRecord.DB_ID + " = ? ", new String[]{String.valueOf(id)});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeDatabaseQuietly(database);
                return delCount;
            }
        }
    }


    public DownloadDBDao(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(CREATE_TABLE_DOWNLOAD);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD);
            db.execSQL(CREATE_TABLE_DOWNLOAD);
        }
    }

    private void closeDatabaseQuietly(SQLiteDatabase database) {
        if (database == null) return;
        try {
            if (database.inTransaction()) {
                database.endTransaction();
            }
            database.close();
        } catch (Exception ignored) {
        }
    }

    private void closeCursorQuietly(Cursor cursor) {
        if (cursor == null) return;
        try {
            cursor.close();
        } catch (Exception ignored) {
        }
    }
}
