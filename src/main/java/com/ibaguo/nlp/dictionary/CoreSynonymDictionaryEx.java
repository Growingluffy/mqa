
package com.ibaguo.nlp.dictionary;

import static com.ibaguo.nlp.utility.Predefine.logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.algoritm.EditDistance;
import com.ibaguo.nlp.dictionary.common.CommonSynonymDictionary;
import com.ibaguo.nlp.dictionary.common.CommonSynonymDictionaryEx;
import com.ibaguo.nlp.dictionary.stopword.CoreStopWordDictionary;
import com.ibaguo.nlp.seg.common.Term;
import com.ibaguo.nlp.utility.TextUtility;

public class CoreSynonymDictionaryEx
{
    static CommonSynonymDictionaryEx dictionary;

    static
    {
        try
        {
            dictionary = CommonSynonymDictionaryEx.create(new FileInputStream(MyNLP.Config.CoreSynonymDictionaryDictionaryPath));
        }
        catch (Exception e)
        {
            System.err.println("载入核心同义词词典失败" + e);
            System.exit(-1);
        }
    }

    public static Long[] get(String key)
    {
        return dictionary.get(key);
    }

    
    public static long distance(CommonSynonymDictionary.SynonymItem itemA, CommonSynonymDictionary.SynonymItem itemB)
    {
        return itemA.distance(itemB);
    }

    
    public static List<Long[]> convert(List<Term> sentence, boolean withUndefinedItem)
    {
        List<Long[]> synonymItemList = new ArrayList<Long[]>(sentence.size());
        for (Term term : sentence)
        {
            // 除掉停用词
            if (term.nature == null) continue;
            String nature = term.nature.toString();
            char firstChar = nature.charAt(0);
            switch (firstChar)
            {
                case 'm':
                {
                    if (!TextUtility.isAllChinese(term.word)) continue;
                }break;
                case 'w':
                {
                    continue;
                }
            }
            // 停用词
            if (CoreStopWordDictionary.contains(term.word)) continue;
            Long[] item = get(term.word);
//            logger.trace("{} {}", wordResult.word, Arrays.toString(item));
            if (item == null)
            {
                if (withUndefinedItem)
                {
                    item = new Long[]{Long.MAX_VALUE / 3};
                    synonymItemList.add(item);
                }

            }
            else
            {
                synonymItemList.add(item);
            }
        }

        return synonymItemList;
    }

    
    public static long[] getLexemeArray(List<CommonSynonymDictionary.SynonymItem> synonymItemList)
    {
        long[] array = new long[synonymItemList.size()];
        int i = 0;
        for (CommonSynonymDictionary.SynonymItem item : synonymItemList)
        {
            array[i++] = item.entry.id;
        }
        return array;
    }


    public long distance(List<CommonSynonymDictionary.SynonymItem> synonymItemListA, List<CommonSynonymDictionary.SynonymItem> synonymItemListB)
    {
        return EditDistance.compute(synonymItemListA, synonymItemListB);
    }

    public long distance(long[] arrayA, long[] arrayB)
    {
        return EditDistance.compute(arrayA, arrayB);
    }
}
