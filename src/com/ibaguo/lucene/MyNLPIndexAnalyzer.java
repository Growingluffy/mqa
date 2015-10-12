package com.ibaguo.lucene;

import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.ibaguo.nlp.MyNLP;

public class MyNLPIndexAnalyzer extends Analyzer
{

    private boolean pstemming;
    private Set<String> filter;

    /**
     * @param filter    filter for stopWord
     * @param pstemming 
     */
    public MyNLPIndexAnalyzer(Set<String> filter, boolean pstemming)
    {
        this.filter = filter;
    }

    /**
     * @param pstemming
     */
    public MyNLPIndexAnalyzer(boolean pstemming)
    {
        this.pstemming = pstemming;
    }

    public MyNLPIndexAnalyzer()
    {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName)
    {
        Tokenizer tokenizer = new MyNLPTokenizer(MyNLP.newSegment().enableIndexMode(true), filter, pstemming);
        return new TokenStreamComponents(tokenizer);
    }
}
