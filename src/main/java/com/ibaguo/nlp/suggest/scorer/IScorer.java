
package com.ibaguo.nlp.suggest.scorer;

import java.util.Map;


public interface IScorer
{
    
    Map<String, Double> computeScore(String outerSentence);

    
    void addSentence(String sentence);
}
