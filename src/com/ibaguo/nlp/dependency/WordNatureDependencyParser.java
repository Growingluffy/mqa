
package com.ibaguo.nlp.dependency;

import java.util.List;

import com.ibaguo.nlp.corpus.dependency.CoNll.CoNLLSentence;
import com.ibaguo.nlp.dependency.common.Edge;
import com.ibaguo.nlp.dependency.common.Node;
import com.ibaguo.nlp.model.bigram.WordNatureDependencyModel;
import com.ibaguo.nlp.seg.common.Term;
import com.ibaguo.nlp.tokenizer.NLPTokenizer;


public class WordNatureDependencyParser extends MinimumSpanningTreeParser
{
    static final WordNatureDependencyParser INSTANCE = new WordNatureDependencyParser();

    public static CoNLLSentence compute(List<Term> termList)
    {
        return INSTANCE.parse(termList);
    }

    public static CoNLLSentence compute(String text)
    {
        return compute(NLPTokenizer.segment(text));
    }

    @Override
    protected Edge makeEdge(Node[] nodeArray, int from, int to)
    {
        return WordNatureDependencyModel.getEdge(nodeArray[from], nodeArray[to]);
    }
}
