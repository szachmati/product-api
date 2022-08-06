package pl.wit.shop.product.test;

import lombok.Getter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResultCaptor<T> implements Answer<T> {
    @Getter
    private T result;

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        result = (T) invocation.callRealMethod();
        return result;
    }
}
