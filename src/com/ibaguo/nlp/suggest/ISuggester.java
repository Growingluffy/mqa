
package com.ibaguo.nlp.suggest;

import java.util.List;


public interface ISuggester
{
    void addSentence(String sentence);

    
    List<String> suggest(String key, int size);
}
