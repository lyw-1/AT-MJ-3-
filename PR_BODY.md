feat(core): add optional runtime-loaded middleware (compression/helmet/ratelimit) with MVP compatibility

Summary
- Introduce optional performance and security middlewares that are loaded at runtime: gzip compression (compression), HTTP security headers (helmet), and request rate limiting (express-rate-limit).
- Middlewares are loaded via dynamic require. If a dependency is not installed in the environment, the app will continue to function with the existing MVP setup. This minimizes risk while enabling production-ready enhancements when dependencies are present.

What changed
- src/app.ts: Enhanced to attempt loading compression, helmet, and rate-limit middlewares at runtime and apply them if available.
- src/middleware/logger.ts and src/middleware/errorHandler.ts: Relaxed typings to avoid strict dependency on Express typings in this MVP; behavior remains the same.
- No breaking changes to existing routes, health checks, or core flow.
- Dependency footprint is optional; no hard requirements introduced for MVP stability.

Rationale
- Improves production readiness by enabling common performance and security patterns without impacting existing MVP in environments where dependencies may not be installed yet.
- Facilitates smoother adoption of ALS/OpenTelemetry-based tracing and structured logging in subsequent iterations.

How to test
- Start the server as usual.
- If compression/helmet/ratelimit are installed, routes should be gzip-compressed, include security headers, and be rate-limited according to defaults.
- If any dependency is missing, the server should run unchanged with MVP behavior.
- Verify existing endpoints (/health, /example) behave as before and logs include requestId when present.

Backward compatibility
- No hard dependency upgrades; optional middlewares work only when present.
- Existing behavior remains intact when dependencies are not installed.

Next steps
- Integrate with structured logging (ALS/trace) and OpenTelemetry for end-to-end tracing.
- Add unit/integration tests for the new middleware-loading paths and log outputs.
- Align with external best practices (documentation and CI/CD improvements).
