package com.agropredict.infrastructure.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.entity.User;
import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserContact;
import com.agropredict.domain.component.user.UserData;
import com.agropredict.domain.component.user.UserIdentity;
import com.agropredict.domain.component.user.UserProfile;

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
    public String authenticate(String email, String passwordHash) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = "SELECT id, full_name, email, password_hash, "
                + "username, phone_number, occupation_id "
                + "FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        String identifier = verify(cursor, passwordHash);
        cursor.close();
        return identifier;
    }

    private String verify(Cursor cursor, String passwordHash) {
        if (!cursor.moveToFirst()) return null;
        User user = build(cursor);
        if (!user.authenticate(passwordHash)) return null;
        return cursor.getString(COLUMN_IDENTIFIER);
    }

    @Override
    public void store(User user) {
        Recorder recorder = record(user);
        recorder.flush("user");
    }

    private Recorder record(User user) {
        Recorder recorder = new Recorder(databaseHelper.getWritableDatabase());
        user.accept(new UserRecorder(recorder));
        return recorder;
    }

    @Override
    public boolean isRegistered(String email) {
        return existsByEmail(email);
    }

    @Override
    public boolean isTaken(String username) {
        return existsByUsername(username);
    }

    private User build(Cursor cursor) {
        UserIdentity identity = new UserIdentity(
                cursor.getString(COLUMN_IDENTIFIER), cursor.getString(COLUMN_FULL_NAME));
        Credential credential = new Credential(
                cursor.getString(COLUMN_EMAIL), cursor.getString(COLUMN_PASSWORD_HASH));
        UserContact contact = new UserContact(
                cursor.getString(COLUMN_USERNAME), cursor.getString(COLUMN_PHONE_NUMBER));
        UserProfile profile = new UserProfile(contact, cursor.getString(COLUMN_OCCUPATION_ID));
        UserData data = new UserData(credential, profile);
        return User.create(identity, data);
    }

    private boolean existsByEmail(String email) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT 1 FROM user WHERE email = ? LIMIT 1",
                new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    private boolean existsByUsername(String username) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT 1 FROM user WHERE username = ? LIMIT 1",
                new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    @Override
    public boolean reset(String email, String newPasswordHash) {
        Recorder recorder = new Recorder(databaseHelper.getWritableDatabase());
        recorder.record("email", email);
        recorder.record("password_hash", newPasswordHash);
        return recorder.overwrite("user", "email");
    }
}