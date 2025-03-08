import winston from "winston";

const { combine, timestamp, json, errors, cli, align, printf, colorize } = winston.format;

const productionLogger = new winston.transports.Console({
    format: combine(errors({ stack: true }), timestamp(), json())
});

const defaultLogger = new winston.transports.Console({
    format: combine(
        cli(),
        colorize({ all: true }),
        errors({ stack: true }),
        timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
        align(),
        printf((info) => `[${info.timestamp}] ${info.level}: ${info.message}`))
});


export const logger = winston.createLogger({
    level: process.env.LOG_LEVEL || 'info',
    transports: [ process.env.NODE_ENV === 'production' ? productionLogger : defaultLogger ],
});
