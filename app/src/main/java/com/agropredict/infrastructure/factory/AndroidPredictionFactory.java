package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticArchive;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.infrastructure.api_integration.DiagnosticApiService;
import com.agropredict.infrastructure.api_integration.DiagnosticHTTPGateway;
import com.agropredict.infrastructure.api_integration.DiagnosticResponseReader;
import com.agropredict.infrastructure.persistence.repository.DiagnosticContext;
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
import com.agropredict.infrastructure.notification.CropNotificationService;
import com.agropredict.infrastructure.persistence.schema.CatalogName;

public final class AndroidPredictionFactory implements IPredictionFactory {
    private static final String DIAGNOSTIC_ENDPOINT = "https://proyecto-diagnostico.onrender.com/diagnostic";
    private final Database database;
    private final Context context;
    private final ISeverityFactory severityFactory;

    public AndroidPredictionFactory(Database database, Context context, ISeverityFactory severityFactory) {
        this.database = database;
        this.context = context;
        this.severityFactory = severityFactory;
    }

    @Override
    public IImageClassifier createImageClassifier() {
        InterpreterLoader loader = new InterpreterLoader(context.getAssets());
        LabelCatalog labels = new LabelCatalog(context.getAssets());
        TFLiteModel model = new TFLiteModel(
                loader.load("models/cultivo_model.tflite"),
                labels.load("models/classes.json"));
        return new TFLiteClassifier(model, new ImageProcessor(new ImageValidator(), new ImagePreprocessor()));
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return new BitmapCompressor(context);
    }

    @Override
    public IDiagnosticApiService createApiService() {
        DiagnosticHTTPGateway gateway = new DiagnosticHTTPGateway(DIAGNOSTIC_ENDPOINT);
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        return new DiagnosticApiService(gateway, reader);
    }

    @Override
    public DiagnosticWorkflow createDiagnosticWorkflow() {
        SessionRepository session = new SessionRepository(context);
        SqliteSyncRecorder recorder = new SqliteSyncRecorder(database, session);
        CropDossier dossier = new CropDossier(
                new SyncingCropRepository(new SqliteCropRepository(database, session), recorder),
                new SyncingPhotographRepository(new SqlitePhotographRepository(database, session), recorder));
        CropRegistry registry = new CropRegistry(dossier, createStageCatalog());
        DiagnosticContext context = new DiagnosticContext(session, severityFactory);
        DiagnosticArchive archive = new DiagnosticArchive(
                new SyncingDiagnosticRepository(new SqliteDiagnosticRepository(database, context), recorder),
                new SqliteQuestionnaireRepository(database));
        return new DiagnosticWorkflow(registry, archive, new CropNotificationService(this.context));
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
