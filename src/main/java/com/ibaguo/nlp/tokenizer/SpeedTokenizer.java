
package com.ibaguo.nlp.tokenizer;

import java.util.List;

import com.ibaguo.nlp.seg.Segment;
import com.ibaguo.nlp.seg.Other.DoubleArrayTrieSegment;
import com.ibaguo.nlp.seg.common.Term;


public class SpeedTokenizer
{
    
    public static final Segment SEGMENT = new DoubleArrayTrieSegment();
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
