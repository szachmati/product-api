package pl.wit.shop.shared.exception;


public class NotFoundException extends BaseException {

    public NotFoundException(String cause) {
        super(404, cause);
    }
}