package at.csdc26bb.discord.bot.exception;

import lombok.Data;

@Data
public abstract class BaseException extends RuntimeException {

    private final int httpStatusCode;
    private final ErrorCode errorCode;

    BaseException(int httpStatusCode, ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
    }

    BaseException(int httpStatusCode, ErrorCode errorCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
    }
}
