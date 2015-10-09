
package com.ibaguo.nlp.suggest.scorer.pinyin;

import com.ibaguo.nlp.suggest.scorer.BaseScorer;


public class PinyinScorer extends BaseScorer<PinyinKey>
{
    @Override
    protected PinyinKey generateKey(String sentence)
    {
        PinyinKey pinyinKey = new PinyinKey(sentence);
        if (pinyinKey.size() == 0) return null;
        return pinyinKey;
    }
}
