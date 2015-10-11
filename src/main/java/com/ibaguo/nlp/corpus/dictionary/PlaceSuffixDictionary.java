
package com.ibaguo.nlp.corpus.dictionary;

import com.ibaguo.nlp.corpus.dictionary.SuffixDictionary;
import com.ibaguo.nlp.utility.Predefine;


public class PlaceSuffixDictionary
{
    public static SuffixDictionary dictionary = new SuffixDictionary();
    static
    {
        dictionary.addAll(Predefine.POSTFIX_SINGLE);
        dictionary.addAll(Predefine.POSTFIX_MUTIPLE);
    }
}
