declare const process: any;
interface AppConfig {
  port: number;
  host?: string;
}

const port = process.env.PORT ? parseInt(process.env.PORT, 10) : 3000;
const host = process.env.HOST || '0.0.0.0';

const config: AppConfig = {
  port,
  host,
};

export default config;
export type { AppConfig };
