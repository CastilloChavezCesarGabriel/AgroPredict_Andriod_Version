package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.diagnostic_submission.AnswerArchive;
import com.agropredict.application.diagnostic_submission.Archival;
import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.application.diagnostic_submission.DiagnosticArchive;
import com.agropredict.application.diagnostic_submission.DiagnosticWorkflow;
import com.agropredict.application.diagnostic_submission.FieldRecorder;
import com.agropredict.application.diagnostic_submission.FieldStorage;
import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.infrastructure.api_integration.DiagnosticApiService;
import com.agropredict.infrastructure.image_classification.BitmapCompressor;
import com.agropredict.infrastructure.image_classification.ImagePreprocessor;
import com.agropredict.infrastructure.image_classification.ImageProcessor;
import com.agropredict.infrastructure.image_classification.ImageValidator;
import com.agropredict.infrastructure.image_classification.InterpreterLoader;
import com.agropredict.infrastructure.image_classification.LabelCatalog;
import com.agropredict.infrastructure.image_classification.TFLiteClassifier;
import com.agropredict.infrastructure.image_classification.TFLiteModel;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteSyncRecorder;
import com.agropredict.infrastructure.persistence.repository.SyncingCropRepository;
import com.agropredict.infrastructure.persistence.repository.SyncingDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SyncingPhotographRepository;
import com.agropredict.infrastructure.persistence.schema.CatalogName;

public final class AndroidPredictionFactory implements IPredictionFactory {
    private static final String DIAGNOSTIC_ENDPOINT = "https://proyecto-diagnostico.onrender.com/diagnostic";
    private final Database database;
    private final Context context;
    private IImageClassifier classifier;

    public AndroidPredictionFactory(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public IImageClassifier createImageClassifier() {
        if (classifier == null) {
            InterpreterLoader loader = new InterpreterLoader(context.getAssets());
            LabelCatalog labels = new LabelCatalog(context.getAssets());
            TFLiteModel model = new TFLiteModel(
                    loader.load("models/cultivo_model.tflite"),
                    labels.load("models/classes.json"));
            classifier = new TFLiteClassifier(model, new ImageProcessor(new ImageValidator(), new ImagePreprocessor()));
        }
        return classifier;
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return new BitmapCompressor(context);
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return new DiagnosticApiService(DIAGNOSTIC_ENDPOINT);
    }

    @Override
    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        SessionRepository session = new SessionRepository(context);
        SqliteSyncRecorder recorder = new SqliteSyncRecorder(database, session);
        FieldStorage storage = new FieldStorage(
                new SyncingCropRepository(new SqliteCropRepository(database, session), recorder),
                new SyncingPhotographRepository(new SqlitePhotographRepository(database, session), recorder));
        Cropland cropland = new Cropland(storage, createStageCatalog());
        FieldRecorder fieldRecorder = new FieldRecorder(cropland);
        Archival archival = new Archival(
                new DiagnosticArchive(new SyncingDiagnosticRepository(new SqliteDiagnosticRepository(database, session), recorder)),
                new AnswerArchive(new SqliteQuestionnaireRepository(database)));
        return new DiagnosticWorkflow(fieldRecorder, archival);
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return CatalogName.SOIL_TYPE.open(database);
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return CatalogName.PHENOLOGICAL_STAGE.open(database);
    }
}
