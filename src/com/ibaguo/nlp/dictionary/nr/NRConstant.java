
package com.ibaguo.nlp.dictionary.nr;

import com.ibaguo.nlp.dictionary.CoreDictionary;
import com.ibaguo.nlp.utility.Predefine;


public class NRConstant
{
    
    public static final int WORD_ID = CoreDictionary.getWordID(Predefine.TAG_PEOPLE);
    
    public static final CoreDictionary.Attribute ATTRIBUTE = CoreDictionary.get(WORD_ID);
}
