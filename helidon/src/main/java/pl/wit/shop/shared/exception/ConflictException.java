package pl.wit.shop.shared.exception;


public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(409, message);
    }
}
