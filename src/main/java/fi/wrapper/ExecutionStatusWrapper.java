package fi.wrapper;

public class ExecutionStatusWrapper {

    private String timestamp;
    private String message;
    private boolean hasFinished;

    public ExecutionStatusWrapper(String timestamp, String message, boolean hasFinished) {
        this.timestamp = timestamp;
        this.message = message;
        this.hasFinished = hasFinished;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHasFinished() {
        return hasFinished;
    }
}
