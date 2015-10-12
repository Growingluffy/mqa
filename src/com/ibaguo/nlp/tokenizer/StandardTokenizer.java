
package com.ibaguo.nlp.tokenizer;

import java.util.List;
import java.util.ListIterator;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.dictionary.stopword.CoreStopWordDictionary;
import com.ibaguo.nlp.seg.Segment;
import com.ibaguo.nlp.seg.common.Term;


public class StandardTokenizer
{
    
    public static final Segment SEGMENT = MyNLP.newSegment();

    
    public static List<Term> segment(String text)
    {
        return SEGMENT.seg(text.toCharArray());
    }

    
    public static List<Term> segment(char[] text)
    {
        return SEGMENT.seg(text);
    }

    
    public static List<List<Term>> seg2sentence(String text)
    {
        return SEGMENT.seg2sentence(text);
    }
}
