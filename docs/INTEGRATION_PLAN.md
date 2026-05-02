# AgroPredict Integration Plan

Status: Proposed (not yet started)
Last updated: 2026-05-01

## Goal

Connect the Android app (`AgroPredict`) to AgroPredictAPI-v2 so that v2 is the default backend and local SQLite is the offline fallback. Today the two projects are isolated: Android only talks to the legacy `proyecto-diagnostico.onrender.com/diagnostic` ML service, and v2 is unused.

## Current state (audited 2026-05-01)

- Android calls one URL: `https://proyecto-diagnostico.onrender.com/diagnostic` (real ML model, returns Spanish-shaped `{ reporte_resumido, texto_largo }`).
- AgroPredictAPI-v2 has full CRUD scaffolding (`/api/auth/*`, `/api/crops`, `/api/diagnostics`, `/api/catalogs/*`, `/api/reports`) but its ML services are stubs: `StubImageClassifier` returns `("Unknown", 0)` and `StubDiagnosticApiService` returns `"AI service not yet integrated"`.
- v2 has no `.env`, no Vercel/host config, no outbound HTTP. It runs locally only.
- Android has no connectivity detection (no `ConnectivityManager` references). It is "offline-first" only because it never tries v2.
- Body shapes are incompatible: Android sends `{ cultivo_detectado, confianza, answers }`; v2 expects `{ submission, questionnaire }`.
- The `POST /api/sync` route mentioned in v2's `CLAUDE.md` does not exist.

## Target architecture

```
Android ──► AgroPredictAPI-v2 (default, when online)
            ├── /api/auth, /api/crops, /api/diagnostics, /api/catalogs ── Postgres
            └── /api/diagnostics/submit ──► onrender ML (proxied internally by v2)

Android ◄── (when v2 unreachable) ── local SQLite fallback
```

Local SQLite remains the offline source of truth. v2 owns the onrender call so Android only knows about one backend URL.

## Three scopes

### Scope A — Diagnostic-only (~1 day) — RECOMMENDED FOR THIS MILESTONE

Connect just the diagnostic submission flow. Smallest surface, gives a complete "online via v2 → offline via SQLite" loop to write milestone tests against.

**v2 changes:**
1. Replace `StubDiagnosticApiService` with `OnrenderDiagnosticApiService` that POSTs to `proyecto-diagnostico.onrender.com/diagnostic` with the existing legacy JSON shape, parses Spanish severity, returns enriched `Diagnostic`.
2. Vitest tests for the new adapter using `fetch` mock.

**Android changes:**
3. `IConnectivity` interface in `application/service/`, `AndroidConnectivity` impl in `infrastructure/` using `ConnectivityManager.getActiveNetwork()`.
4. Update `DiagnosticApiService` to send the v2 body shape `{ submission: {...}, questionnaire: {...} }` and parse v2's response shape.
5. Wrap `SubmitDiagnosticUseCase` with a connectivity check: when offline, skip the API call and store the diagnostic locally with severity `"Pending"`. When online, attempt the API call; on `IOException` fall back to local storage.
6. Update `RealDiagnosticApiServiceTest` fixtures to match v2's body shape.

**Tests delivered:**
- Android: connectivity-online → posts to v2; connectivity-offline → skips network, stores locally; v2-down (5xx/timeout) → falls back to local with Pending severity.
- v2: receives Android body, calls onrender, returns enriched diagnostic.

**Out of scope for A:** auth, crop CRUD, catalogs, reports, sync.

### Scope B — Diagnostic + Catalogs (~2 days)

Adds catalog endpoints to the failover path. Useful if you want online catalogs (so server-side catalog updates propagate without an APK rebuild).

Adds: `RestSoilTypeCatalog`, `RestStageCatalog`, `RestOccupationCatalog`, all wrapped in a generic `Failover<ICatalogRepository>` decorator.

**Out of scope for B:** auth, crop CRUD, sync.

### Scope C — Full failover (~3–4 days)

Adds auth and crop CRUD. Requires resolving the auth model conflict (PBKDF2 local vs BetterAuth JWT in v2) and implementing `POST /api/sync` for pending offline operations.

**Open questions for C:**
- Auth: keep local PBKDF2 with v2 as eventual-consistency mirror, or switch Android entirely to v2's BetterAuth flow (rewrites existing 30 security tests)?
- Sync conflict resolution: last-write-wins, or CRDT-style merge?
- Pending-ops queue: how long is the offline window expected to be?

## Recommended path

Pick **Scope A** for this milestone. Reasons:
- Smallest delta; fits a milestone deadline.
- Gives you the full online/offline test surface to grade against.
- Doesn't risk regressing the existing 276-test suite.
- Auth and CRUD can move next milestone without throwing away Scope A work.

## Test surface for the milestone (Scope A)

| Test | Layer | Tool |
|---|---|---|
| Android: post to v2 when online | Infrastructure | MockWebServer |
| Android: skip network when offline (`IConnectivity.isOnline() = false`) | Application | Stub connectivity + capturing repo |
| Android: fall back to local when v2 returns 5xx | Infrastructure | MockWebServer with `setResponseCode(500)` |
| Android: fall back to local when v2 times out | Infrastructure | MockWebServer with `setSocketPolicy(NO_RESPONSE)` |
| v2: proxy to onrender on POST /api/diagnostics/submit | Presentation + Infrastructure | Vitest + fetch-mock |
| v2: surface onrender 5xx as 502 | Presentation | Vitest + fetch-mock |
| v2: timeout on onrender → 504 | Presentation | Vitest + fetch-mock |

## Risks & mitigations

- **v2 stub ML returns useless text.** Mitigated by v2 proxying to onrender for ML; v2 stays the single Android-facing surface.
- **Onrender free-tier cold start can take ~30s.** Mitigated by Android's existing 30s timeouts. v2 should pass through the same timeout budget.
- **Body-shape change breaks current Android users.** No external users yet (pre-release); coordinated cutover acceptable.
- **PostgreSQL not seeded with the same catalog rows as Android SQLite.** Out of scope for A (catalogs stay local), but flag for Scope B: v2 needs a seed migration mirroring Android's `SeedLoader` values.

## Open decisions before starting

1. Onrender endpoint stays the same (`/diagnostic`)? Yes by default — change in v2 only if onrender URL has moved.
2. Where does v2 run during testing? Local `http://10.0.2.2:3000` from the emulator. No deployment needed for the milestone.
3. Auth scope (Scope C only): defer.
