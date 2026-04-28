package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.Session;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.UserPersistenceVisitor;

public final class SqliteUserRepository implements IUserRepository {
    private final Database database;
    private final ICatalogRepository catalog;
    private final IPasswordHasher hasher;

    public SqliteUserRepository(Database database, ICatalogRepository catalog, IPasswordHasher hasher) {
        this.database = database;
        this.catalog = catalog;
        this.hasher = hasher;
    }

    @Override
    public void register(RegistrationRequest request, IPasswordHasher hasher) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        UserPersistenceVisitor visitor = new UserPersistenceVisitor(row);
        request.authenticate(visitor, hasher);
        request.classify(visitor, catalog);
        try {
            row.flush("user");
        } catch (SQLiteConstraintException exception) {
            throw translate(exception);
        }
    }

    private RegistrationException translate(SQLiteConstraintException exception) {
        String message = exception.getMessage() == null ? "" : exception.getMessage();
        if (message.contains("user.email")) return new RegistrationException("This email is already registered");
        if (message.contains("user.username")) return new RegistrationException("This username already exists");
        return new RegistrationException("Account could not be created");
    }

    @Override
    public Session authenticate(String email, String password) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = "SELECT id, password_hash, occupation_id FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        Session session = cursor.moveToFirst() ? confirm(cursor, password) : null;
        cursor.close();
        return session;
    }

    private Session confirm(Cursor cursor, String password) {
        String storedHash = cursor.getString(cursor.getColumnIndexOrThrow("password_hash"));
        if (!hasher.verify(password, storedHash)) return null;
        return new Session(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("occupation_id")));
    }

    @Override
    public boolean reset(String email, String newPasswordHash) {
        SqliteRow sqliteRow = new SqliteRow(database.getWritableDatabase());
        sqliteRow.record("email", email);
        sqliteRow.record("password_hash", newPasswordHash);
        return sqliteRow.overwrite("user", "email");
    }
}