
package com.ibaguo.nlp.recognition.nr;

import static com.ibaguo.nlp.dictionary.nr.NRConstant.ATTRIBUTE;
import static com.ibaguo.nlp.dictionary.nr.NRConstant.WORD_ID;

import java.util.List;
import java.util.Map;

import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.dictionary.BaseSearcher;
import com.ibaguo.nlp.dictionary.CoreDictionary;
import com.ibaguo.nlp.dictionary.nr.JapanesePersonDictionary;
import com.ibaguo.nlp.seg.common.Vertex;
import com.ibaguo.nlp.seg.common.WordNet;
import com.ibaguo.nlp.utility.Predefine;


public class JapanesePersonRecognition
{
    
    public static void Recognition(List<Vertex> segResult, WordNet wordNetOptimum, WordNet wordNetAll)
    {
        StringBuilder sbName = new StringBuilder();
        int appendTimes = 0;
        char[] charArray = wordNetAll.charArray;
        BaseSearcher searcher = JapanesePersonDictionary.getSearcher(charArray);
        Map.Entry<String, Character> entry;
        int activeLine = 1;
        int preOffset = 0;
        while ((entry = searcher.next()) != null)
        {
            Character label = entry.getValue();
            String key = entry.getKey();
            int offset = searcher.getOffset();
            if (preOffset != offset)
            {
                if (appendTimes > 1 && sbName.length() > 2) // 日本人名最短为3字
                {
                    wordNetOptimum.insert(activeLine, new Vertex(Predefine.TAG_PEOPLE, sbName.toString(), new CoreDictionary.Attribute(Nature.nrj), WORD_ID), wordNetAll);
                }
                sbName.setLength(0);
                appendTimes = 0;
            }
            if (appendTimes == 0)
            {
                if (label == JapanesePersonDictionary.X)
                {
                    sbName.append(key);
                    ++appendTimes;
                    activeLine = offset + 1;
                }
            }
            else
            {
                if (label == JapanesePersonDictionary.M)
                {
                    sbName.append(key);
                    ++appendTimes;
                }
                else
                {
                    if (appendTimes > 1 && sbName.length() > 2)
                    {
                        wordNetOptimum.insert(activeLine, new Vertex(Predefine.TAG_PEOPLE, sbName.toString(), new CoreDictionary.Attribute(Nature.nrj), WORD_ID), wordNetAll);
                    }
                    sbName.setLength(0);
                    appendTimes = 0;
                }
            }
            preOffset = offset + key.length();
        }
        if (sbName.length() > 0)
        {
            if (appendTimes > 1)
            {
                wordNetOptimum.insert(activeLine, new Vertex(Predefine.TAG_PEOPLE, sbName.toString(), new CoreDictionary.Attribute(Nature.nrj), WORD_ID), wordNetAll);
            }
        }
    }
}
