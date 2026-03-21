package com.agropredict.infrastructure.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.PasswordHasher;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.entity.User;

public final class SqliteUserRepository implements IUserRepository {
    private static final int COLUMN_IDENTIFIER = 0;
    private static final int COLUMN_PASSWORD_HASH = 1;

    private final DatabaseHelper databaseHelper;

    public SqliteUserRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public String authenticate(String email, String password) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = "SELECT id, password_hash FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        String identifier = verify(cursor, password);
        cursor.close();
        return identifier;
    }

    private String verify(Cursor cursor, String password) {
        if (!cursor.moveToFirst()) return null;
        String storedHash = cursor.getString(COLUMN_PASSWORD_HASH);
        if (!new PasswordHasher().verify(password, storedHash)) return null;
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