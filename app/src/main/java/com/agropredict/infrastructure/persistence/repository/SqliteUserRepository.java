package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.RegistrationException;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.user.Account;
import com.agropredict.domain.user.AnonymousUser;
import com.agropredict.domain.user.Credential;
import com.agropredict.domain.user.ContactInformation;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.IUser;
import com.agropredict.domain.user.NoUser;
import com.agropredict.domain.user.Phone;
import com.agropredict.domain.user.User;
import com.agropredict.domain.user.occupation.OccupationCatalog;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.visitor.UserPersistenceVisitor;
import java.util.Objects;

public final class SqliteUserRepository implements IUserRepository {
    private final Database database;
    private final IPasswordHasher hasher;
    private final SqliteRowFactory rowFactory;

    public SqliteUserRepository(Database database, IPasswordHasher hasher, SqliteRowFactory rowFactory) {
        this.database = Objects.requireNonNull(database, "user repository requires a database");
        this.hasher = Objects.requireNonNull(hasher, "user repository requires a password hasher");
        this.rowFactory = Objects.requireNonNull(rowFactory, "user repository requires a row factory");
    }

    @Override
    public void register(RegistrationRequest request, ICatalogRepository catalog) {
        SqliteRow row = rowFactory.open();
        UserPersistenceVisitor visitor = new UserPersistenceVisitor(row);
        request.describe(visitor);
        request.contact(visitor);
        request.authenticate(visitor, hasher);
        request.enroll(visitor);
        request.classify(visitor, catalog);
        row.stamp("created_at");
        row.stamp("updated_at");
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
        return assemble(cursor, email, storedHash);
    }

    @Override
    public boolean reset(String email, String newPasswordHash) {
        SqliteRow row = rowFactory.open();
        row.record("email", email);
        row.record("password_hash", newPasswordHash);
        row.stamp("updated_at");
        return row.overwrite("user", "email");
    }

    @Override
    public IUser find(String userIdentifier) {
        SQLiteDatabase readable = database.getReadableDatabase();
        String query = "SELECT id, email, full_name, username, password_hash, occupation_id, phone_number "
                + "FROM user WHERE id = ? AND is_active = 1";
        Cursor cursor = readable.rawQuery(query, new String[]{userIdentifier});
        IUser user = cursor.moveToFirst() ? rebuild(cursor) : new NoUser();
        cursor.close();
        return user;
    }

    private User rebuild(Cursor cursor) {
        return assemble(cursor,
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password_hash")));
    }

    private User assemble(Cursor cursor, String email, String passwordHash) {
        ContactInformation identity = new ContactInformation(
                cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                Phone.resolve(cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))));
        Credential credential = new Credential(email, passwordHash);
        Account account = new Account(
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                credential,
                new OccupationCatalog().classify(cursor.getString(cursor.getColumnIndexOrThrow("occupation_id"))));
        return new User(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                identity,
                account);
    }
}
