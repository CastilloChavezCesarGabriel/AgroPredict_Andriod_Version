package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.entity.User;
import com.agropredict.domain.value.user.Credentials;
import com.agropredict.domain.value.user.UserContact;
import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.value.user.UserProfile;

public final class SqliteUserRepository implements IUserRepository {
    private static final int COLUMN_IDENTIFIER = 0;
    private static final int COLUMN_FULL_NAME = 1;
    private static final int COLUMN_EMAIL = 2;
    private static final int COLUMN_PASSWORD_HASH = 3;
    private static final int COLUMN_USERNAME = 4;
    private static final int COLUMN_PHONE_NUMBER = 5;
    private static final int COLUMN_OCCUPATION_ID = 6;

    private final DatabaseHelper databaseHelper;

    public SqliteUserRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public User authenticate(String email, String passwordHash) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = "SELECT id, full_name, email, password_hash, "
                + "username, phone_number, occupation_id "
                + "FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        User user = extractUser(cursor);
        cursor.close();
        if (user == null) return null;
        if (!user.authenticate(passwordHash)) return null;
        return user;
    }

    @Override
    public void store(User user) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(user);
        database.insert("user", null, values);
    }

    private ContentValues record(User user) {
        ContentValues values = new ContentValues();
        user.accept(new UserRecorder(values));
        return values;
    }

    @Override
    public boolean isRegistered(String email) {
        return existsByColumn("email", email);
    }

    @Override
    public boolean isTaken(String username) {
        return existsByColumn("username", username);
    }

    private User extractUser(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        return build(cursor);
    }

    private User build(Cursor cursor) {
        UserIdentity identity = new UserIdentity(
                cursor.getString(COLUMN_IDENTIFIER), cursor.getString(COLUMN_FULL_NAME));
        Credentials credentials = new Credentials(
                cursor.getString(COLUMN_EMAIL), cursor.getString(COLUMN_PASSWORD_HASH));
        UserContact contact = new UserContact(
                cursor.getString(COLUMN_USERNAME), cursor.getString(COLUMN_PHONE_NUMBER));
        UserProfile profile = new UserProfile(contact, cursor.getString(COLUMN_OCCUPATION_ID));
        UserData data = new UserData(credentials, profile);
        return User.create(identity, data);
    }

    private boolean existsByColumn(String column, String value) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT 1 FROM user WHERE " + column + " = ? LIMIT 1",
                new String[]{value});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}