package com.agropredict.presentation.user_interface.export;

public interface IExportedFileConsumer {
    void accept(IExportedFile artifact);
}
