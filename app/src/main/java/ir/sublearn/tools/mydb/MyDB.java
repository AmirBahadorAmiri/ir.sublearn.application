package ir.sublearn.tools.mydb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ir.sublearn.models.TextModel;
import ir.sublearn.models.UserModel;
import ir.sublearn.models.WordModel;

@Database(entities = {WordModel.class, TextModel.class, UserModel.class}, version = 2, exportSchema = false)
public abstract class MyDB extends RoomDatabase {

    private static MyDB myDB;
//
//    public static void copyDatabase(Context context) {
//
//        String database_path = "data/data/" + context.getPackageName() + "/databases/";
//        String database_name = "words";
//
//        try {
//            File dir = new File(database_path);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//
//            File file = new File(database_path, database_name);
//            if (!file.exists()) {
//
//                OutputStream outputStream = new FileOutputStream(database_path + database_name);
//                byte[] buffer = new byte[1024];
//                int lenght;
//                InputStream inputStream = context.getAssets().open("database/words");
//                while ((lenght = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, lenght);
//                }
//                inputStream.close();
//                outputStream.flush();
//                outputStream.close();
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static MyDB getInstance(Context context) {
        if (myDB == null)
            myDB = Room.databaseBuilder(context, MyDB.class, "words")
                    .createFromAsset("database/words")
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                        }
                    })
                    .fallbackToDestructiveMigration(false)
                    .build();

        /*
        *
        * .addMigrations(new Migration(1,2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("CREATE TABLE 'sentence_tb' ('id' INTEGER Not null, 'segment' Text, 'translation' Text , PRIMARY KEY('id'))");
                        }
                    })
        *
        * */
        return myDB;
    }

    public abstract WordDao getWordDao();

    public abstract TextDao getTextDao();

    public abstract UserDao getUserDao();

}