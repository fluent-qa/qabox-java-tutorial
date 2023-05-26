package io.fluentqa.api.exceptions;

/**
 * dao层异常
 */
public class DaoException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DaoException() {
    }

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public DaoException(int status, String msg) {
        super(status, msg);
    }

    public DaoException(int status, Throwable throwable) {
        super(status, throwable);
    }

    public DaoException(int status, String msg, Throwable throwable) {
        super(status, msg, throwable);
    }
}
