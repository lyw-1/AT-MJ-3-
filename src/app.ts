const express = require('express')
import { requestIdMiddleware } from './middleware/requestId'
import { loggerMiddleware } from './middleware/logger'
import { errorHandler } from './middleware/errorHandler'

declare const require: any

// Optional middlewares: try to load at runtime to avoid hard deps in minimal MVP
let compressionModule: any = null
let helmetModule: any = null
let rateLimitModule: any = null
try { compressionModule = require('compression') } catch { compressionModule = null }
try { helmetModule = require('helmet') } catch { helmetModule = null }
try { rateLimitModule = require('express-rate-limit') } catch { rateLimitModule = null }

const app = express()

app.use(express.json())
app.use(requestIdMiddleware)
app.use(loggerMiddleware)

// Apply optional security/performance middlewares if available
if (compressionModule) {
  try {
    const cm = compressionModule.default ? compressionModule.default() : compressionModule()
    app.use(cm)
  } catch {
    // ignore if runtime middleware cannot be configured
  }
}
if (helmetModule) {
  try {
    const hm = helmetModule.default ? helmetModule.default() : helmetModule()
    app.use(hm)
  } catch {
    // ignore
  }
}
if (rateLimitModule) {
  try {
    const ratelimitFactory = rateLimitModule.default ? rateLimitModule.default : rateLimitModule
    const limiter = ratelimitFactory({ windowMs: 15 * 60 * 1000, max: 100 })
    app.use(limiter)
  } catch {
    // ignore
  }
}

// Health check endpoint
app.get('/health', (req, res) => res.json({ ok: true, t: Date.now() }))

// Simple example route to demonstrate the response wrapper usage pattern
app.get('/example', (req, res) => {
  res.json({ success: true, data: { msg: 'up-and-running' } })
})

// Error handler should be last
app.use((err: any, req: any, res: any, next: any) => {
  errorHandler(err, req, res, next)
})

export default app
