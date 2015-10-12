
package com.ibaguo.nlp.dictionary.stopword;

import com.ibaguo.nlp.seg.common.Term;


public interface Filter
{
    
    boolean shouldInclude(Term term);
}
