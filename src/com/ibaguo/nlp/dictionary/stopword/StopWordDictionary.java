
package com.ibaguo.nlp.dictionary.stopword;

import static com.ibaguo.nlp.utility.Predefine.logger;

import java.io.*;
import java.util.Collection;

import com.ibaguo.nlp.collection.MDAG.MDAGSet;
import com.ibaguo.nlp.dictionary.common.CommonDictionary;
import com.ibaguo.nlp.seg.common.Term;


public class StopWordDictionary extends MDAGSet implements Filter
{
    public StopWordDictionary(File file) throws IOException
    {
        super(file);
    }

    public StopWordDictionary(Collection<String> strCollection)
    {
        super(strCollection);
    }

    public StopWordDictionary()
    {
    }

    @Override
    public boolean shouldInclude(Term term)
    {
        return contains(term.word);
    }
}
