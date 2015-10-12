
package com.ibaguo.nlp.recognition.nr;

import static com.ibaguo.nlp.dictionary.nr.NRConstant.WORD_ID;

import java.util.List;
import java.util.ListIterator;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.dictionary.CoreDictionary;
import com.ibaguo.nlp.dictionary.nr.TranslatedPersonDictionary;
import com.ibaguo.nlp.seg.common.Vertex;
import com.ibaguo.nlp.seg.common.WordNet;
import com.ibaguo.nlp.utility.Predefine;


public class TranslatedPersonRecognition
{
    
    public static void Recognition(List<Vertex> segResult, WordNet wordNetOptimum, WordNet wordNetAll)
    {
        StringBuilder sbName = new StringBuilder();
        int appendTimes = 0;
        ListIterator<Vertex> listIterator = segResult.listIterator();
        listIterator.next();
        int line = 1;
        int activeLine = 1;
        while (listIterator.hasNext())
        {
            Vertex vertex = listIterator.next();
            if (appendTimes > 0)
            {
                if (vertex.guessNature() == Nature.nrf || TranslatedPersonDictionary.containsKey(vertex.realWord))
                {
                    sbName.append(vertex.realWord);
                    ++appendTimes;
                }
                else
                {
                    // 识别结束
                    if (appendTimes > 1)
                    {
                        if (MyNLP.Config.DEBUG)
                        {
                            System.out.println("音译人名识别出：" + sbName.toString());
                        }
                        wordNetOptimum.insert(activeLine, new Vertex(Predefine.TAG_PEOPLE, sbName.toString(), new CoreDictionary.Attribute(Nature.nrf), WORD_ID), wordNetAll);
                    }
                    sbName.setLength(0);
                    appendTimes = 0;
                }
            }
            else
            {
                // nrf和nsf触发识别
                if (vertex.guessNature() == Nature.nrf || vertex.getNature() == Nature.nsf
//                        || TranslatedPersonDictionary.containsKey(vertex.realWord)
                        )
                {
                    sbName.append(vertex.realWord);
                    ++appendTimes;
                    activeLine = line;
                }
            }

            line += vertex.realWord.length();
        }
    }
}
