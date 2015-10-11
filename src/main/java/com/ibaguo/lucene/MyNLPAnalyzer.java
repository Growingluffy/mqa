package com.ibaguo.lucene;

import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.ibaguo.nlp.MyNLP;

public class MyNLPAnalyzer extends Analyzer
{

    boolean enablePorterStemming;
    public Set<String> filter;

    public MyNLPAnalyzer(Set<String> filter, boolean enablePorterStemming)
    {
        this.filter = filter;
    }

    public MyNLPAnalyzer(boolean enablePorterStemming)
    {
        this.enablePorterStemming = enablePorterStemming;
    }

    public MyNLPAnalyzer()
    {
        super();
    }

    /**
     * make the Tokenizer
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName)
    {
        Tokenizer tokenizer = new MyNLPTokenizer(MyNLP.newSegment().enableOffset(true), filter, enablePorterStemming);
        return new TokenStreamComponents(tokenizer);
    }
}
