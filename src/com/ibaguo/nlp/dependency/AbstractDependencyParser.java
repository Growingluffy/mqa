
package com.ibaguo.nlp.dependency;

import java.util.List;

import com.ibaguo.nlp.corpus.dependency.CoNll.CoNLLSentence;
import com.ibaguo.nlp.seg.common.Term;


public abstract class AbstractDependencyParser
{
    public abstract CoNLLSentence parse(List<Term> termList);
}
