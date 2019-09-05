package cn.pers.qhl.sendfile;

public class IllegalUnitException extends Exception {

    public IllegalUnitException() {
    }

    public IllegalUnitException(String message) {
        super(message);
    }

    public IllegalUnitException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUnitException(Throwable cause) {
        super(cause);
    }

    public IllegalUnitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
