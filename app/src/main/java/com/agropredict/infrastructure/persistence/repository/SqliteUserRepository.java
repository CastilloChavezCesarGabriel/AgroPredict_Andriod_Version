package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.PasswordHasher;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.entity.User;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.UserPersistenceVisitor;

public final class SqliteUserRepository extends SqliteRepository<User> implements IUserRepository {
    private static final int COLUMN_IDENTIFIER = 0;
    private static final int COLUMN_PASSWORD_HASH = 1;

    public SqliteUserRepository(Database database) {
        super(database, "user");
    }

    @Override
    protected void persist(User user, SqliteRow row) {
        user.accept(new UserPersistenceVisitor(row));
    }

    @Override
    public String authenticate(String email, String password) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = "SELECT id, password_hash FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        String identifier = cursor.moveToFirst() ? confirm(cursor, password) : null;
        cursor.close();
        return identifier;
    }

    private String confirm(Cursor cursor, String password) {
        String storedHash = cursor.getString(COLUMN_PASSWORD_HASH);
        if (!new PasswordHasher().verify(password, storedHash)) return null;
        return cursor.getString(COLUMN_IDENTIFIER);
    }

    @Override
    public boolean isRegistered(String email) {
        return contains("email", email);
    }

    @Override
    public boolean isTaken(String username) {
        return contains("username", username);
    }

    private boolean contains(String column, String value) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT 1 FROM user WHERE " + column + " = ? LIMIT 1",
                new String[]{value});
        boolean found = cursor.moveToFirst();
        cursor.close();
        return found;
    }

    @Override
    public boolean reset(String email, String newPasswordHash) {
        SqliteRow sqliteRow = new SqliteRow(database.getWritableDatabase());
        sqliteRow.record("email", email);
        sqliteRow.record("password_hash", newPasswordHash);
        return sqliteRow.overwrite("user", "email");
    }
}
