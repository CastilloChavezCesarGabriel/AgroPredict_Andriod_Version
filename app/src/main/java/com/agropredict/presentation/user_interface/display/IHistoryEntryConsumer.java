package com.agropredict.presentation.user_interface.display;

public interface IHistoryEntryConsumer {
    void accept(String identifier, ListEntry entry);
}
