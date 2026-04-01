# AgroPredict

This Android application allows agricultural workers and agronomists to capture or select images of their crops, obtain AI-powered diagnoses through a TensorFlow Lite model and an enrichment API, manage crop records with full change history and auditing, generate exportable reports in PDF and CSV formats, and operate entirely offline with automatic SQLite backups. The project follows a strict Clean Architecture with the Visitor pattern replacing all getters and setters, ensuring that every entity exposes behavior instead of state and that each architectural layer depends only on the layer beneath it.

## Clean Architecture Structure

- **Domain**: Manages the core business entities (Crop, Diagnostic, Photograph, Report), their behavioral components (Assessment, Prediction, Field, Soil, GrowthCycle), flat visitor interfaces for data access without getters, input validators (email, password, username, full name, phone number) and session/login attempt logic.
- **Application**: Defines 18 use cases for authentication, crop management, diagnostics, reports and catalogs, along with repository interfaces (including IDeletable for shared delete behavior), service interfaces, organized request objects (user_registration, diagnostic_submission, ai_questionnaire, report_generation), facades and application-level visitor contracts.
- **Infrastructure**: Implements all external concerns including SQLite persistence with 18 tables, SQL triggers for derived columns, and custom Visitor-based serialization, TensorFlow Lite image classification, HTTP networking to the backend API, PDF generation with iText7, CSV generation with OpenCSV, PBKDF2WithHmacSHA256 password hashing and automatic database backups.
- **Presentation**: Contains 11 Activities with their ViewModels organized by feature (authentication, crop_management, diagnostic_history, home_dashboard, prediction_diagnosis, report_generation), UI components for display/form/selector/catalog/questionnaire input, and navigation logic with role-based visibility through the Session object.

## Android Libraries Used

- **android.app.Application**: Provides the application lifecycle entry point for dependency initialization and backup execution.
- **android.database.sqlite.SQLiteOpenHelper**: Manages SQLite database creation, schema versioning and upgrade migrations.
- **android.database.sqlite.SQLiteDatabase**: Executes raw SQL queries, inserts, updates and deletes against the local database.
- **android.database.Cursor**: Iterates over query result sets row by row for data extraction.
- **android.content.ContentValues**: Maps column names to values for SQLite insert and update operations.
- **android.content.SharedPreferences**: Persists user session data (identifier and occupation) across application restarts.
- **android.content.Intent**: Navigates between Activities and shares generated report files with external applications.
- **android.os.Bundle**: Passes data between Activities during navigation through key-value pairs.
- **android.widget.Toast**: Displays brief feedback messages to the user after operations.
- **android.widget.Spinner**: Renders dropdown selection inputs for catalogs (soil type, phenological stage, occupation).
- **android.widget.ArrayAdapter**: Populates Spinner widgets with string arrays from catalog repositories.
- **android.widget.EditText**: Captures text input from the user in forms across all Activities.
- **android.widget.Button**: Triggers user actions (login, register, submit, generate report, delete).
- **android.widget.ImageView**: Displays captured or selected crop images in the prediction and result screens.
- **android.widget.TextView**: Renders labels, results, severity indicators and diagnostic information.
- **android.view.View**: Base class for all UI components and click listener attachment.
- **android.view.LayoutInflater**: Inflates XML layout files into View hierarchies for Activities and list items.
- **android.provider.MediaStore**: Provides access to the device camera for crop image capture.
- **androidx.appcompat.app.AppCompatActivity**: Base Activity class with backward-compatible action bar and lifecycle support.
- **androidx.core.content.FileProvider**: Grants temporary URI permissions for sharing generated report files.
- **com.google.android.material**: Provides Material Design components for consistent styling across the application.

## Java Libraries Used

- **java.security.SecureRandom**: Generates cryptographically strong random 16-byte salt values for PBKDF2 password hashing.
- **java.security.spec.KeySpec**: Specifies the password, salt, iteration count and key length parameters for PBKDF2 key derivation.
- **javax.crypto.SecretKeyFactory**: Creates the PBKDF2WithHmacSHA256 secret key factory instance for password hash generation.
- **javax.crypto.spec.PBEKeySpec**: Defines the password-based encryption key specification with 65536 iterations and 256-bit key length.
- **java.util.concurrent.atomic.AtomicLong**: Provides atomic counter for collision-free identifier generation in the Identifier class.
- **java.util.List**: Defines list contracts for crop collections, diagnostic histories and traversal results.
- **java.util.ArrayList**: Stores mutable collections of entities, catalog items and questionnaire responses.
- **java.util.Map**: Defines map contracts for JSON payload construction in the API service.
- **java.util.HashMap**: Builds key-value pairs for HTTP request bodies and questionnaire data.
- **java.util.regex.Pattern**: Compiles regular expression patterns for email and full name validation.
- **java.io.File**: Represents file system paths for report export, image compression and database backup.
- **java.io.FileWriter**: Writes CSV report content to files in the external storage directory.
- **java.io.IOException**: Handles file writing, image processing and network communication errors.
- **java.net.HttpURLConnection**: Establishes HTTP connections to the backend API for diagnostic enrichment.
- **java.net.URL**: Locates the backend API endpoint for POST requests.

## Third-Party Libraries Used

- **com.google.ai.edge.litert (TFLite 2.1.3)**: Loads and executes the TensorFlow Lite crop classification model with 224x224 input tensors and 10-class output probabilities.
- **com.itextpdf.itext7-core (9.5.0)**: Generates styled PDF report documents with diagnostic data, crop information and recommendation text using the DiagnosticTraversal visitor.
- **com.opencsv (5.12.0)**: Writes diagnostic and crop data to CSV files with proper escaping and column formatting for spreadsheet compatibility.

## Dependencies

- Java 17
- Android API 24-36 (Android 7.0 to Android 16)
- TensorFlow Lite 2.1.3 (crop image classification)
- iText7 9.5.0 (PDF report generation)
- OpenCSV 5.12.0 (CSV report generation)
- Material Design 1.13.0 (UI components)
- AppCompat 1.7.1 (backward compatibility)
- Gradle (build tool)
- JUnit 4.13.2 (testing)

## Features

- AI-powered crop diagnosis through TensorFlow Lite with 10-class classification and 0.6 confidence threshold.
- Agricultural questionnaire with soil, climate, pest, symptom, irrigation and farm management inputs.
- Backend API enrichment of diagnoses with severity assessment and personalized recommendations.
- Complete authentication system with login, registration, password recovery and PBKDF2 hashing.
- Account lockout protection after 5 failed login attempts with 5-minute blocking duration.
- Role-based access control with occupation-driven visibility (Farmer, Agronomist, Specialist, Researcher).
- Six input validators: email (regex with consecutive dot rejection), password (8+ chars with complexity), username (5-32 alphanumeric), full name (2-50 alphabetic), phone (7-15 digits) and password criteria (upper, lower, digit, special). Validators live in domain/input_validation/ package.
- Crop management with full CRUD operations, soft delete and automatic change history auditing.
- Diagnostic history with crop-based filtering and color-coded severity indicators.
- PDF report generation with iText7 using the DiagnosticTraversal visitor pattern for data extraction.
- CSV report generation with OpenCSV for spreadsheet-compatible data export.
- Report sharing through Android Intent system with FileProvider URI permissions.
- Offline-first architecture with SQLite persistence across 18 tables with SQL triggers for derived columns and indexes.
- Automatic database backup to external storage on every application startup.
- Image validation with format checking (JPG/PNG), size limits (10KB-10MB) and dimension constraints (100-8000px).
- Image preprocessing with 224x224 resizing and 0.0-1.0 normalization for TFLite inference.
- JPEG compression at 80% quality for storage-efficient image caching.
- 236 automated JUnit tests across 36 test files covering Smoke, Integration, Regression, Security and unit tests.
- 3 defects found and corrected through automated testing (Identifier collision, email consecutive dots, submit null data).
- 11 Activities with complete navigation flow from login through diagnosis to report generation.
- Material Design UI with 12 XML layouts, 19 drawable resources and dark/light theme support.

## Architecture

```
AgroPredict/
├── app/build.gradle
├── app/src/main/AndroidManifest.xml
├── app/src/main/java/com/agropredict/
│   ├── AgroPredictApplication.java                           # Application entry point with backup and dependency initialization
│   ├── core/
│   │   └── Configuration.java                                # Dependency wiring and factory initialization on startup
│   ├── domain/
│   │   ├── Identifier.java                                   # Unique identifier generator with AtomicLong counter for collision prevention
│   │   ├── LoginAttempt.java                                 # Immutable login attempt tracker with 5-attempt lockout and 5-minute block
│   │   ├── Session.java                                      # User session with identifier, occupation and role-based access checks
│   │   ├── entity/
│   │   │   ├── Crop.java                                     # Crop entity with locate(), plant(), schedule() creating Field, Soil, GrowthCycle
│   │   │   ├── Diagnostic.java                               # Diagnostic entity with conclude(), recommend(), isConfident(), isSevere(), classify()
│   │   │   ├── Photograph.java                               # Photograph entity with accept(IPhotographVisitor)
│   │   │   └── Report.java                                   # Report entity with accept(IReportVisitor)
│   │   ├── component/
│   │   │   ├── crop/
│   │   │   │   ├── Field.java                                # Field value object with accept() — created by Crop.locate()
│   │   │   │   ├── Soil.java                                 # Soil value object with accept() — created by Crop.plant()
│   │   │   │   └── GrowthCycle.java                          # Growth cycle value object with accept() — created by Crop.schedule()
│   │   │   └── diagnostic/
│   │   │       ├── Assessment.java                           # Assessment with isSevere(), classify(), conclude() — created by Diagnostic.conclude()
│   │   │       └── Prediction.java                           # Prediction with isConfident() using 0.6 threshold
│   │   ├── input_validation/
│   │   │   ├── ITextValidator.java                           # Polymorphic text validation interface
│   │   │   ├── EmailValidator.java                           # Email regex validation with consecutive dot rejection
│   │   │   ├── PasswordValidator.java                        # Password validation delegating to PasswordCriteria
│   │   │   ├── PasswordCriteria.java                         # Tracks uppercase, lowercase, digit and special character presence
│   │   │   ├── UsernameValidator.java                        # Username validation (5-32 chars, alphanumeric, at least one letter)
│   │   │   ├── FullNameValidator.java                        # Full name validation (2-50 chars, alphabetic with spaces)
│   │   │   └── PhoneNumberValidator.java                     # Phone number validation (7-15 digits, optional)
│   │   └── visitor/
│   │       ├── crop/
│   │       │   ├── ICropVisitor.java                         # Flat crop visitor (visitIdentity, visitField, visitSoil, visitPlanting)
│   │       │   └── IPhotographVisitor.java                   # Flat photograph visitor (identifier, filePath)
│   │       ├── diagnostic/
│   │       │   └── IDiagnosticVisitor.java                   # Flat diagnostic visitor (visitIdentity, visitPrediction, visitAssessment, visitRecommendation)
│   │       ├── report/
│   │       │   └── IReportVisitor.java                       # Flat report visitor (identifier, format)
│   │       ├── session/
│   │       │   └── ISessionVisitor.java                      # Session visitor (identifier, occupation)
│   │       └── user/
│   │           └── IUserVisitor.java                         # User visitor for persistence (identity, credentials, profile)
│   ├── application/
│   │   ├── IRepositoryFactory.java                           # Factory interface for repository creation
│   │   ├── IFactoryConsumer.java                             # Consumer interface for factory injection
│   │   ├── facade/
│   │   │   ├── DiagnosticHistoryFacade.java                  # Facade for diagnostic history retrieval and organization
│   │   │   └── PredictionFacade.java                         # Facade for image classification and diagnostic submission workflow
│   │   ├── operation_result/
│   │   │   ├── OperationResult.java                          # Generic result with success/fail/reject states and visitor accept
│   │   │   ├── RegistrationResult.java                       # Registration-specific result with error message and visitor accept
│   │   │   ├── ClassificationResult.java                     # Classification result with prediction and confidence threshold check
│   │   │   ├── HistoryRecord.java                            # Crop history record with modification and transition data
│   │   │   ├── Modification.java                             # Modified field name and timestamp
│   │   │   └── HistoryTransition.java                        # Previous and current value pair
│   │   ├── repository/
│   │   │   ├── IUserRepository.java                          # User persistence contract (authenticate, store, reset)
│   │   │   ├── ICropRepository.java                          # Crop persistence contract (CRUD, trace history)
│   │   │   ├── IDiagnosticRepository.java                    # Diagnostic persistence contract (CRUD, filter by crop)
│   │   │   ├── IReportRepository.java                        # Report persistence contract (store)
│   │   │   ├── IPhotographRepository.java                    # Photograph persistence contract
│   │   │   ├── IQuestionnaireRepository.java                 # Questionnaire response persistence contract
│   │   │   ├── ICatalogRepository.java                       # Catalog persistence contract (list, resolve by name)
│   │   │   ├── ISessionRepository.java                       # Session persistence extending ISessionVisitor (recall, clear)
│   │   │   ├── IDiagnosticWorkflow.java                      # Diagnostic workflow contract (persist request with enriched result)
│   │   │   └── IDeletable.java                               # Shared delete contract used by DeleteUseCase
│   │   ├── usecase/
│   │   │   ├── DeleteUseCase.java                            # Generic delete using IDeletable interface
│   │   │   ├── authentication/
│   │   │   │   ├── LoginUseCase.java                         # Authenticates user with lockout tracking and session creation
│   │   │   │   ├── RegisterUseCase.java                      # Validates registration data and stores hashed user
│   │   │   │   ├── ResetPasswordUseCase.java                 # Validates email and password then updates hash
│   │   │   │   ├── CheckSessionUseCase.java                  # Recalls stored session and notifies visitor
│   │   │   │   └── LogoutUseCase.java                        # Clears session from repository
│   │   │   ├── crop/
│   │   │   │   ├── FindCropUseCase.java                      # Retrieves single crop by identifier
│   │   │   │   ├── ListCropUseCase.java                      # Lists all crops for authenticated user
│   │   │   │   ├── UpdateCropUseCase.java                    # Updates crop data through repository
│   │   │   │   └── TraceCropHistoryUseCase.java              # Retrieves crop modification history records
│   │   │   ├── diagnostic/
│   │   │   │   ├── ClassifyImageUseCase.java                 # Validates image and delegates to TFLite classification
│   │   │   │   ├── SubmitDiagnosticUseCase.java              # Submits diagnostic to API and persists via workflow
│   │   │   │   ├── FindDiagnosticUseCase.java                # Retrieves single diagnostic by identifier
│   │   │   │   └── ListDiagnosticUseCase.java                # Lists and filters diagnostics by user and crop
│   │   │   ├── report/
│   │   │   │   ├── DiagnosticTraversal.java                  # Multi-interface visitor that traverses diagnostic components for report generation
│   │   │   │   ├── GenerateReportUseCase.java                # Delegates report generation to service
│   │   │   │   └── StoreReportUseCase.java                   # Persists report metadata through repository
│   │   │   └── catalog/
│   │   │       └── ListCatalogUseCase.java                   # Lists catalog entries (occupations, soil types, stages)
│   │   ├── request/
│   │   │   ├── CropUpdateRequest.java                        # Crop update payload that applies to entity
│   │   │   ├── user_registration/
│   │   │   │   ├── RegistrationRequest.java                  # Validates registrant and account, dispatches authenticate/classify directly
│   │   │   │   ├── RegistrationException.java                # Runtime exception for registration validation failures
│   │   │   │   ├── Registrant.java                           # Personal data with name and phone validation
│   │   │   │   ├── Account.java                              # Account data with authentication and profile validation
│   │   │   │   ├── Authentication.java                       # Email and password validation with duplicate checking
│   │   │   │   └── Profile.java                              # Username validation with duplicate checking and occupation
│   │   │   ├── diagnostic_submission/
│   │   │   │   ├── SubmissionRequest.java                    # submit() generates identifier, creates diagnostic internally
│   │   │   │   ├── Classification.java                       # derive(identifier) — TFLite classification result
│   │   │   │   ├── Submission.java                           # Diagnostic submission input
│   │   │   │   ├── Cultivation.java                          # Cultivation data input
│   │   │   │   ├── Field.java                                # Field information block
│   │   │   │   └── PhotographInput.java                      # Image path reference input
│   │   │   ├── ai_questionnaire/
│   │   │   │   ├── Questionnaire.java                        # Full diagnostic questionnaire aggregation
│   │   │   │   ├── Soil.java                                 # Soil composition data
│   │   │   │   ├── Weather.java                              # Temperature, rainfall and humidity data
│   │   │   │   ├── Rainfall.java                             # Rainfall data
│   │   │   │   ├── Condition.java                            # Current crop health condition data
│   │   │   │   ├── Symptom.java                              # Observed symptom type and severity
│   │   │   │   ├── Pest.java                                 # Pest identification data
│   │   │   │   ├── PestControl.java                          # Treatment and control information
│   │   │   │   ├── Irrigation.java                           # Watering practices and frequency
│   │   │   │   ├── Observation.java                          # Farmer field observations
│   │   │   │   ├── CropCare.java                             # Crop care practices aggregation
│   │   │   │   └── FarmManagement.java                       # Farm management practices
│   │   │   └── report_generation/
│   │   │       ├── ReportRequest.java                        # Report metadata compilation request
│   │   │       ├── Destination.java                          # Report destination data
│   │   │       └── Finding.java                              # Report finding data
│   │   ├── service/
│   │   │   ├── IImageService.java                            # Image classification (classify + compress) contract
│   │   │   ├── IPasswordHasher.java                          # Password hashing (hash + verify) contract
│   │   │   ├── IDiagnosticApiService.java                    # Backend API diagnostic submission contract
│   │   │   ├── IReportService.java                           # Report generation contract
│   │   │   ├── IReportWriter.java                            # Report file writing contract (PDF or CSV)
│   │   │   ├── IAuditLogger.java                             # Audit logging contract
│   │   │   ├── IAssetService.java                            # Asset extraction contract
│   │   │   └── IBackupService.java                           # Database backup contract
│   │   └── visitor/
│   │       ├── IOperationResultVisitor.java                  # Visitor for operation result (completed and identifier)
│   │       ├── IRegistrationResultVisitor.java               # Visitor for registration result (completed and error)
│   │       ├── IClassificationResultVisitor.java             # Visitor for classification extending IPredictionVisitor with reject
│   │       ├── IHistoryVisitor.java                          # Visitor for history records (modification and transition)
│   │       ├── IQuestionnaireVisitor.java                    # Visitor for questionnaire response data
│   │       └── ISubmissionVisitor.java                       # Visitor for submission extending IQuestionnaireVisitor
│   ├── infrastructure/
│   │   ├── security/
│   │   │   └── PasswordHasher.java                           # PBKDF2WithHmacSHA256 with 65536 iterations and constant-time comparison
│   │   ├── image_classification/
│   │   │   └── ImageService.java                             # TFLite inference with image validation, compression and 224x224 preprocessing
│   │   ├── api_integration/
│   │   │   └── DiagnosticApiService.java                     # HTTP client for POST /diagnostic with JSON serialization
│   │   ├── report_export/
│   │   │   ├── ReportService.java                            # Template Method: generate() calls prepare() → traverse → finalize()
│   │   │   ├── PdfReportService.java                         # PDF report generation using iText7
│   │   │   ├── CsvReportService.java                         # CSV report generation
│   │   │   ├── PdfReport.java                                # iText7 document wrapper for PDF content writing
│   │   │   └── CsvReport.java                                # CSV writer with header/value line building and escape
│   │   ├── database_backup/
│   │   │   ├── DatabaseBackup.java                           # SQLite file backup to external storage on app startup
│   │   │   └── FileCopier.java                               # File copy utility for backup operations
│   │   ├── ai_model_asset/
│   │   │   └── AssetExtractor.java                           # Extracts TFLite model assets from app bundle
│   │   ├── factory/
│   │   │   ├── RepositoryFactory.java                        # Creates all repository instances with database reference
│   │   │   ├── PersistenceFactory.java                       # Creates database, schema and persistence visitors
│   │   │   └── ServiceFactory.java                           # Creates image, network, export and security services
│   │   └── persistence/
│   │       ├── AuditLogger.java                              # Audit event persistence to SQLite log table
│   │       ├── database/
│   │       │   ├── Database.java                             # SQLiteOpenHelper with schema creation and foreign key enforcement
│   │       │   ├── IRow.java                                 # Row writing contract interface
│   │       │   └── SqliteRow.java                            # Row abstraction for SQLite insert and overwrite operations
│   │       ├── diagnostic_submission/
│   │       │   ├── DiagnosticWorkflow.java                   # Orchestrates diagnostic, photograph and questionnaire persistence
│   │       │   ├── Submission.java                           # Submission data holder for workflow persistence
│   │       │   └── SubmissionRecorder.java                   # Records submission data into SQLite tables
│   │       ├── repository/
│   │       │   ├── SqliteRepository.java                     # Abstract base repository with shared database reference
│   │       │   ├── SqliteUserRepository.java                 # User CRUD with email/username uniqueness and password reset
│   │       │   ├── SqliteCropRepository.java                 # Crop CRUD with history tracking and soft delete
│   │       │   ├── SqliteDiagnosticRepository.java           # Diagnostic CRUD with crop filtering and resolution
│   │       │   ├── SqlitePhotographRepository.java           # Photograph persistence
│   │       │   ├── SqliteQuestionnaireRepository.java        # Questionnaire response persistence
│   │       │   ├── SqliteReportRepository.java               # Report metadata persistence
│   │       │   ├── SqliteCatalog.java                        # Catalog repository for occupation, soil type and stage lookups
│   │       │   └── SessionRepository.java                    # SharedPreferences session persistence
│   │       ├── schema/
│   │       │   ├── Schema.java                               # Orchestrates all table creation in order
│   │       │   ├── ITable.java                               # Table creation contract interface
│   │       │   ├── UserTable.java                            # User table DDL with email/username UNIQUE constraints
│   │       │   ├── CropTable.java                            # Crop and crop_history table DDL with triggers
│   │       │   ├── DiagnosticTable.java                      # Diagnostic table DDL with define() + index() + automate()
│   │       │   ├── QuestionnaireTable.java                   # ai_question, ai_option and ai_user_response DDL
│   │       │   ├── ImageTable.java                           # Image table DDL with foreign keys
│   │       │   ├── ReportTable.java                          # Report and report_sharing table DDL
│   │       │   ├── CatalogTable.java                         # Occupation, soil_type and phenological_stage catalog DDL
│   │       │   ├── SupportTable.java                         # Support knowledge base table DDL
│   │       │   ├── DiagnosticSummaryView.java                # Materialized view DDL for report data aggregation
│   │       │   ├── Seed.java                                 # Seed data orchestrator for all catalog tables
│   │       │   ├── SeedData.java                             # Static seed data with populate() + register() + fill()
│   │       │   ├── QuestionSeed.java                         # Seed data for questionnaire questions
│   │       │   └── OptionSeed.java                           # Seed data for questionnaire options
│   │       └── visitor/
│   │           ├── UserPersistenceVisitor.java                # Extracts User data into SqliteRow for persistence
│   │           ├── CropPersistenceVisitor.java                # Extracts Crop data into SqliteRow (constructor takes IRow, Session)
│   │           ├── DiagnosticPersistenceVisitor.java          # Extracts Diagnostic data into SqliteRow (implements IDiagnosticVisitor only)
│   │           ├── PhotographPersistenceVisitor.java          # Extracts Photograph data into SqliteRow (constructor takes IRow, Session)
│   │           ├── QuestionnairePersistenceVisitor.java       # Extracts questionnaire responses for persistence
│   │           └── ReportPersistenceVisitor.java              # Extracts Report data into SqliteRow for persistence
│   └── presentation/
│       ├── user_interface/
│       │   ├── screen/
│       │   │   ├── BaseActivity.java                         # Base Activity with shared navigation, redirect and notification methods
│       │   │   ├── LoginActivity.java                        # Login screen with email/password and navigation to register/recovery
│       │   │   ├── RegisterActivity.java                     # Registration screen with full form and occupation selection
│       │   │   ├── RecoveryActivity.java                     # Password recovery with String.equals() confirmation (no domain validator)
│       │   │   ├── HomeActivity.java                         # Main dashboard with role-based actions and resource links
│       │   │   ├── PredictionActivity.java                   # Image capture/gallery selection with agricultural questionnaire
│       │   │   ├── PredictionResultActivity.java             # Diagnosis result display with severity and recommendations
│       │   │   ├── HistoryActivity.java                      # Diagnostic history list with crop filtering and deletion
│       │   │   ├── FieldDetailActivity.java                  # Crop detail view with trace() + present() and edit/delete options
│       │   │   ├── EditFieldActivity.java                    # Crop editor with type, name, location, soil and stage fields
│       │   │   └── ReportActivity.java                       # Report generation with crop selection, date range and PDF/CSV export
│       │   ├── display/
│       │   │   ├── DiagnosticDisplay.java                    # Single diagnostic detail display component
│       │   │   ├── DiagnosticHistory.java                    # Diagnostic list display using diagnostic.classify() from domain
│       │   │   ├── EntryAdapter.java                         # RecyclerView adapter for diagnostic list entries
│       │   │   ├── ListEntry.java                            # Single list entry data holder for adapter binding
│       │   │   ├── FieldDetail.java                          # Crop field detail display component
│       │   │   ├── PredictionDisplay.java                    # Classification prediction display with confidence
│       │   │   └── PredictionResult.java                     # Prediction result display with severity coloring
│       │   ├── form/
│       │   │   ├── PredictionForm.java                       # Prediction input form with image and questionnaire
│       │   │   ├── QuestionnaireForm.java                    # Agricultural questionnaire form aggregation
│       │   │   ├── RegistrationForm.java                     # User registration form with all input fields
│       │   │   └── ReportForm.java                           # Report generation form with crop and format selection
│       │   ├── selector/
│       │   │   ├── CropSelection.java                        # Dropdown crop selector for filtering operations
│       │   │   ├── DateSelection.java                        # Date picker dialog wrapper for date range selection
│       │   │   ├── IDateSelectionListener.java               # Callback interface for date selection events
│       │   │   ├── ISelectionListener.java                   # Callback interface for component selection events
│       │   │   └── FieldEditor.java                          # Crop field editing form component
│       │   ├── catalog_input/
│       │   │   ├── Catalog.java                              # Base catalog input helper
│       │   │   ├── CatalogInput.java                         # Catalog dropdown input with label
│       │   │   ├── SpinnerPopulator.java                     # Populates Spinner widgets with catalog data
│       │   │   ├── SpinnerInput.java                         # Generic spinner input with label and adapter
│       │   │   ├── SoilTypeCatalog.java                      # Soil type catalog spinner helper
│       │   │   └── StageCatalog.java                         # Phenological stage catalog spinner helper
│       │   ├── questionnaire_input/
│       │   │   ├── CredentialInput.java                      # Email and password input group
│       │   │   ├── EnvironmentInput.java                     # Weather and climate input group
│       │   │   ├── ManagementInput.java                      # Farm management practices input group
│       │   │   ├── SoilInput.java                            # Soil composition input group
│       │   │   └── SymptomInput.java                         # Symptom type and severity input group
│       │   └── navigation/
│       │       ├── BackNavigationGuard.java                  # Prevents accidental back navigation during critical operations
│       │       └── PdfLauncher.java                          # Opens generated PDF files in external viewer applications
│       └── viewmodel/
│           ├── authentication/
│           │   ├── LoginViewModel.java                       # Login orchestrator delegating to LoginUseCase and result strategy
│           │   ├── RegisterViewModel.java                    # Registration orchestrator with validation and result strategy
│           │   ├── RecoveryViewModel.java                    # Recovery orchestrator with email lookup and password update
│           │   ├── LoginResultStrategy.java                  # Operation result visitor for login success/failure routing
│           │   ├── RecoveryResultStrategy.java               # Operation result visitor for recovery success/failure routing
│           │   ├── RegistrationResultStrategy.java           # Registration result visitor for success/error message routing
│           │   ├── ILoginView.java                           # Login view contract for ViewModel communication
│           │   ├── IRegisterView.java                        # Register view contract for ViewModel communication
│           │   └── IRecoveryView.java                        # Recovery view contract for ViewModel communication
│           ├── prediction_diagnosis/
│           │   ├── PredictionViewModel.java                  # Prediction orchestrator with catalog population and facade delegation
│           │   ├── PredictionResultViewModel.java            # Result display orchestrator with diagnostic data loading
│           │   ├── ClassificationResultStrategy.java         # Classification result visitor for prediction/rejection routing
│           │   ├── DiagnosticResultStrategy.java             # Operation result visitor for submission success/failure
│           │   ├── IPredictionView.java                      # Prediction view contract for ViewModel communication
│           │   └── IPredictionResultView.java                # Prediction result view contract for ViewModel communication
│           ├── crop_management/
│           │   ├── FieldDetailViewModel.java                 # Field detail orchestrator with crop loading and history
│           │   ├── EditFieldViewModel.java                   # Field editing orchestrator with catalog population and update
│           │   ├── IFieldDetailView.java                     # Field detail view contract for ViewModel communication
│           │   └── IEditFieldView.java                       # Field editing view contract for ViewModel communication
│           ├── diagnostic_history/
│           │   ├── HistoryViewModel.java                     # History orchestrator with diagnostic listing and deletion
│           │   └── IHistoryView.java                         # History view contract for ViewModel communication
│           ├── home_dashboard/
│           │   ├── HomeViewModel.java                        # Home orchestrator with session check and logout
│           │   └── IHomeView.java                            # Home view contract for ViewModel communication
│           └── report_generation/
│               ├── ReportViewModel.java                      # Report orchestrator with crop selection and generation
│               ├── ReportExporter.java                       # Operation result visitor with userIdentifier in constructor
│               └── IReportView.java                          # Report view contract for ViewModel communication
├── app/src/main/res/
│   ├── layout/                                               # 12 XML layouts for all Activities
│   └── drawable/                                             # 19 drawable resources (gradients, icons, placeholders)
└── app/src/test/java/com/agropredict/                        # 236 JUnit tests across 36 test files
    ├── smoke/
    │   └── SmokeTest.java                                    # Smoke tests verifying critical component instantiation
    ├── domain/
    │   ├── input_validation/
    │   │   ├── PasswordValidatorTest.java                    # Password complexity rule tests
    │   │   ├── EmailValidatorTest.java                       # Email format validation tests
    │   │   ├── UsernameValidatorTest.java                    # Username length and format tests
    │   │   ├── FullNameValidatorTest.java                    # Name length and character rule tests
    │   │   ├── PhoneNumberValidatorTest.java                 # Phone digit rule tests
    │   │   └── PasswordCriteriaTest.java                     # Incremental criteria tracking tests
    │   ├── LoginAttemptTest.java                             # Lockout timing and reset tests
    │   ├── SessionTest.java                                  # Session activity and role check tests
    │   └── IdentifierTest.java                               # Uniqueness and AtomicLong counter tests
    ├── infrastructure/security/
    │   └── PasswordHasherTest.java                           # PBKDF2 hash and verification tests
    ├── application/
    │   ├── operation_result/
    │   │   ├── OperationResultTest.java                      # Succeed, fail and reject state tests
    │   │   ├── RegistrationResultTest.java                   # Succeed and fail with error tests
    │   │   └── ClassificationResultTest.java                 # Confidence threshold behavior tests
    │   └── usecase/
    │       ├── DeleteUseCaseTest.java                        # Generic delete via IDeletable tests
    │       ├── authentication/
    │       │   ├── LoginUseCaseTest.java                     # Login success, failure, lockout and session tests
    │       │   ├── RegisterUseCaseTest.java                  # Registration with valid/invalid/duplicate data tests
    │       │   ├── ResetPasswordUseCaseTest.java             # Reset with valid/invalid credentials tests
    │       │   ├── LogoutUseCaseTest.java                    # Session clearing test
    │       │   └── CheckSessionUseCaseTest.java              # Session recall and occupation tests
    │       ├── crop/
    │       │   ├── FindCropUseCaseTest.java                  # Existing and non-existing crop tests
    │       │   ├── ListCropUseCaseTest.java                  # Populated and empty list tests
    │       │   ├── UpdateCropUseCaseTest.java                # Repository delegation test
    │       │   └── TraceCropHistoryUseCaseTest.java          # History with and without records tests
    │       ├── diagnostic/
    │       │   ├── ClassifyImageUseCaseTest.java             # Valid, invalid and low-confidence classification tests
    │       │   ├── SubmitDiagnosticUseCaseTest.java          # Null request and API failure tests
    │       │   ├── FindDiagnosticUseCaseTest.java            # Existing and non-existing diagnostic tests
    │       │   └── ListDiagnosticsUseCaseTest.java           # Listing, filtering and empty result tests
    │       └── report/
    │           └── GenerateReportUseCaseTest.java            # Generation success and failure tests
    ├── integration/
    │   └── AuthenticationFlowTest.java                       # Login-reset-session end-to-end flow tests
    ├── regression/
    │   └── RegressionTest.java                               # Edge case and known bug tests
    ├── security/
    │   └── SecurityTest.java                                 # SQL injection, XSS, timing attack and brute force tests
    └── visitor/
        ├── TestOperationResultVisitor.java                   # Test visitor for operation results
        ├── TestRegistrationResultVisitor.java                # Test visitor for registration results
        ├── TestClassificationResultVisitor.java              # Test visitor for classification results
        └── TestSessionVisitor.java                           # Test visitor for session data
```

## Design Patterns

| Pattern | Usage |
|---------|-------|
| **Visitor** | 4 flat visitor interfaces (ICropVisitor, IDiagnosticVisitor, IReportVisitor, IPhotographVisitor) expose entity data without getters. Persistence visitors extract entity data into SqliteRow objects. DiagnosticTraversal traverses diagnostic components for report generation. Result objects (OperationResult, RegistrationResult, ClassificationResult) deliver outcomes through visitors. |
| **Clean Architecture** | Four layers with strict dependency rules: domain has zero external imports, application depends only on domain, infrastructure implements application contracts, presentation depends on application. No layer references the layer above it. |
| **Facade** | `PredictionFacade` orchestrates image classification, diagnostic submission and API enrichment. `DiagnosticHistoryFacade` orchestrates diagnostic history retrieval and organization. |
| **Strategy** | `ITextValidator` with `EmailValidator`, `PasswordValidator`, `UsernameValidator`, `FullNameValidator` and `PhoneNumberValidator` as interchangeable input validation rules. Result strategies (`LoginResultStrategy`, `RegistrationResultStrategy`, `ClassificationResultStrategy`) route outcomes to view callbacks. |
| **Factory** | `RepositoryFactory` creates all repository instances. `PersistenceFactory` creates database and schema objects. `ServiceFactory` creates image, network, export and security services. |
| **Template Method** | `ReportService` defines the report generation skeleton (generate → prepare → traverse → finalize) and delegates format-specific steps to `PdfReportService` and `CsvReportService`. `SqliteRepository` defines shared database operations and delegates entity-specific queries to concrete repositories. |
| **Abstract Class** | `ReportService` provides shared timestamp generation to `PdfReportService` and `CsvReportService`. `SqliteRepository` provides shared database reference to all concrete repository implementations. `BaseActivity` provides shared navigation, redirect and notification methods to all 11 Activities. |

## Program Flow

1. When the application launches, `AgroPredictApplication` initializes `Configuration` which creates the `Database`, `PersistenceFactory`, `RepositoryFactory` and `ServiceFactory`, wires all dependencies and executes `DatabaseBackup` to copy the SQLite file to external storage.

2. `LoginActivity` appears as the entry point. The user enters their email and password, and `LoginViewModel` delegates to `LoginUseCase` which checks the `LoginAttempt` tracker for lockout status, calls `IUserRepository.authenticate()` to verify credentials against the PBKDF2 hash stored in SQLite, and on success resets the tracker, persists the `Session` in `SharedPreferences` through the `ISessionRepository` visitor, and returns an `OperationResult` that `LoginResultStrategy` routes to the view for navigation to `HomeActivity`.

3. `HomeActivity` displays role-based actions: "Generar Nueva Prediccion", "Consultar Historial" and (for advanced users) "Generar Reporte". When the user taps "Generar Nueva Prediccion", the system navigates to `PredictionActivity`.

4. In `PredictionActivity`, the user captures or selects a crop image. `ClassifyImageUseCase` validates the image through `ImageService` (format JPG/PNG, size 10KB-10MB, dimensions 100-8000px), compresses it to JPEG 80%, resizes to 224x224, normalizes pixel values to 0.0-1.0 and runs TFLite inference. If confidence is above 0.6, the predicted crop is displayed; otherwise the image is marked as unrecognizable. The user then completes the agricultural questionnaire covering soil, weather, pests, symptoms, irrigation and farm management.

5. `SubmitDiagnosticUseCase` packages the classification and questionnaire into a `SubmissionRequest`, calls `DiagnosticApiService` to POST the data to the backend API for enrichment with severity and recommendations, then delegates to `DiagnosticWorkflow` which persists the diagnostic, crop, image and questionnaire responses across multiple SQLite tables in a single coordinated operation.

6. The result appears in `PredictionResultActivity` with the predicted crop, confidence percentage, severity level (with color-coded gradient), temperature, humidity and recommendation text. The diagnostic is now visible in `HistoryActivity` where it can be filtered by crop and deleted with confirmation.

7. Through `ReportActivity`, advanced users select a crop and format (PDF or CSV), and `GenerateReportUseCase` delegates to either `PdfReportService` or `CsvReportService`. The `DiagnosticTraversal` visitor walks through all diagnostic components extracting data for the report. The generated file is saved to `ExternalFilesDir/reports/` and can be shared via `Intent.ACTION_SEND` with `FileProvider`.

## Setting up on Mac

### Prerequisites

1. **Java 17+**:
   - Download from [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
   - Choose the **macOS** tab and select the `.dmg` file matching your chip.
   - Verify with:
   ```bash
   java --version
   ```

2. **Android Studio**:
   - Download from [developer.android.com](https://developer.android.com/studio)
   - Install and open the project at `AndroidStudioProjects/AgroPredict/`

### Steps

1. Clone the repository and create a new working branch:
   ```bash
   git clone <repository-url>
   cd AgroPredict
   git checkout -b <branch-name>
   ```
2. Open in Android Studio and sync Gradle.
3. Run the application on an emulator or device (API 24+):
   ```bash
   ./gradlew installDebug
   ```
4. Run the tests:
   ```bash
   ./gradlew test
   ```

## Setting up on Windows

### Prerequisites

1. **Java 17+**:
   - Download from [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
   - Choose the **Windows** tab and select the `.msi` or `.exe` file.
   - Verify with:
   ```cmd
   java --version
   ```

2. **Android Studio**:
   - Download from [developer.android.com](https://developer.android.com/studio)
   - Install and open the project.

### Steps

1. Clone the repository and create a new working branch:
   ```cmd
   git clone <repository-url>
   cd AgroPredict
   git checkout -b <branch-name>
   ```
2. Open in Android Studio and sync Gradle.
3. Run the application on an emulator or device (API 24+):
   ```cmd
   gradlew.bat installDebug
   ```
4. Run the tests:
   ```cmd
   gradlew.bat test
   ```

## License

Creative Commons Attribution 4.0 International License (CC BY 4.0).

## Acknowledgements

This project demonstrates a clean code approach to Android development by applying Clean Architecture with the Visitor pattern to eliminate all getters and setters, ensuring that domain entities (Crop, Diagnostic, Photograph, Report) expose behavior instead of state and that persistence, networking and UI concerns are completely isolated from business logic through strict layer boundaries.
