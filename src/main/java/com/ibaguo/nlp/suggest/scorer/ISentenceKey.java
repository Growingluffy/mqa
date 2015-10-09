
package com.ibaguo.nlp.suggest.scorer;


public interface ISentenceKey<T>
{
    Double similarity(T other);
}
