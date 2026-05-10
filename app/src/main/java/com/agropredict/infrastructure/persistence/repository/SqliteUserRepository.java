package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.user.Account;
import com.agropredict.domain.user.AnonymousUser;
import com.agropredict.domain.user.Credential;
import com.agropredict.domain.user.ContactInformation;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.User;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.UserPersistenceVisitor;

public final class SqliteUserRepository implements IUserRepository {
    private final Database database;
    private final IPasswordHasher hasher;

    public SqliteUserRepository(Database database, IPasswordHasher hasher) {
        this.database = database;
        this.hasher = hasher;
    }

    @Override
    public void register(RegistrationRequest request, ICatalogRepository catalog) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        UserPersistenceVisitor visitor = new UserPersistenceVisitor(row);
        request.describe(visitor);
        request.contact(visitor);
        request.authenticate(visitor, hasher);
        request.enroll(visitor);
        request.classify(visitor, catalog);
        String now = Clock.read();
        row.record("created_at", now);
        row.record("updated_at", now);
        row.mark("is_active", 1);
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
    public ISessionSubject authenticate(String email, String password) {
        SQLiteDatabase readable = database.getReadableDatabase();
        String query = "SELECT id, full_name, username, password_hash, occupation_id, phone_number "
                + "FROM user WHERE email = ? AND is_active = 1";
        Cursor cursor = readable.rawQuery(query, new String[]{email});
        ISessionSubject user = cursor.moveToFirst() ? confirm(cursor, email, password) : new AnonymousUser();
        cursor.close();
        return user;
    }

    private ISessionSubject confirm(Cursor cursor, String email, String password) {
        String storedHash = cursor.getString(cursor.getColumnIndexOrThrow("password_hash"));
        if (!hasher.verify(password, storedHash)) return new AnonymousUser();
        ContactInformation identity = new ContactInformation(
                cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
        Credential credential = new Credential(email, storedHash);
        Account account = new Account(
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                credential,
                cursor.getString(cursor.getColumnIndexOrThrow("occupation_id")));
        return new User(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                identity,
                account);
    }

    @Override
    public boolean reset(String email, String newPasswordHash) {
        SqliteRow sqliteRow = new SqliteRow(database.getWritableDatabase());
        sqliteRow.record("email", email);
        sqliteRow.record("password_hash", newPasswordHash);
        sqliteRow.record("updated_at", Clock.read());
        return sqliteRow.overwrite("user", "email");
    }

    @Override
    public User find(String userIdentifier) {
        SQLiteDatabase readable = database.getReadableDatabase();
        String query = "SELECT id, email, full_name, username, password_hash, occupation_id, phone_number "
                + "FROM user WHERE id = ? AND is_active = 1";
        Cursor cursor = readable.rawQuery(query, new String[]{userIdentifier});
        User user = cursor.moveToFirst() ? rebuild(cursor) : null;
        cursor.close();
        return user;
    }

    private User rebuild(Cursor cursor) {
        ContactInformation identity = new ContactInformation(
                cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
        Credential credential = new Credential(
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password_hash")));
        Account account = new Account(
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                credential,
                cursor.getString(cursor.getColumnIndexOrThrow("occupation_id")));
        return new User(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                identity,
                account);
    }
}