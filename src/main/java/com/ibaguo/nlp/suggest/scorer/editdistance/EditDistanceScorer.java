
package com.ibaguo.nlp.suggest.scorer.editdistance;

import com.ibaguo.nlp.suggest.scorer.BaseScorer;


public class EditDistanceScorer extends BaseScorer<CharArray>
{
    @Override
    protected CharArray generateKey(String sentence)
    {
        char[] charArray = sentence.toCharArray();
        if (charArray.length == 0) return null;
        return new CharArray(charArray);
    }
}
