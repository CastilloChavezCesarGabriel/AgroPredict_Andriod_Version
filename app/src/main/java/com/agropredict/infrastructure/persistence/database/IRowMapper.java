package com.agropredict.infrastructure.persistence.database;

import android.database.Cursor;

public interface IRowMapper<T> {
    T map(Cursor cursor);
}
