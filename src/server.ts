import app from './app'
import config from './config'

const PORT = config.port || 3000
const HOST = config.host || '0.0.0.0'

app.listen(PORT, HOST, () => {
  // eslint-disable-next-line no-console
  console.log(`Server listening on ${HOST}:${PORT}`)
})
