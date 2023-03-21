import { LOG_LEVEL } from './environment';

export interface LogFn {
  (message?: any, ...optionalParams: any[]): void;
}

/** Basic logger interface */
export interface Logger {
  log: LogFn;
  warn: LogFn;
  error: LogFn;
}

/** Log levels */
export type LogLevel = 'log' | 'warn' | 'error';

// eslint-disable-next-line @typescript-eslint/no-empty-function,@typescript-eslint/no-explicit-any
const NO_OP: LogFn = (message?: any, ...optionalParams: any[]) => {};

/** Logger which outputs to the browser console */
export class ConsoleLogger implements Logger {
  readonly log: LogFn;
  readonly warn: LogFn;
  readonly error: LogFn;

  constructor(options?: { level?: LogLevel }) {
    const { level } = options || {};

    // eslint-disable-next-line no-console
    this.error = console.error.bind(console);

    if (level === 'error') {
      this.warn = NO_OP;
      this.log = NO_OP;

      return;
    }
    // eslint-disable-next-line no-console
    this.warn = console.warn.bind(console);

    if (level === 'warn') {
      this.log = NO_OP;
      return;
    }
    // eslint-disable-next-line no-console
    this.log = console.log.bind(console);
  }
}

export const logger = new ConsoleLogger({ level: LOG_LEVEL });
