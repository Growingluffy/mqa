
package com.ibaguo.nlp.collection.trie.bintrie;


public class _EmptyValueArray<V> extends _ValueArray<V>
{
    public _EmptyValueArray()
    {
    }

    @Override
    public V nextValue()
    {
        return null;
    }
}
