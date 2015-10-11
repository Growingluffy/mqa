
package com.ibaguo.nlp.corpus.dependency.CoNll;

import java.util.LinkedList;

import com.ibaguo.nlp.corpus.io.IOUtil;


public class CoNLLLoader
{
    public static LinkedList<CoNLLSentence> loadSentenceList(String path)
    {
        LinkedList<CoNLLSentence> result = new LinkedList<CoNLLSentence>();
        LinkedList<CoNllLine> lineList = new LinkedList<CoNllLine>();
        for (String line : IOUtil.readLineListWithLessMemory(path))
        {
            if (line.trim().length() == 0)
            {
                result.add(new CoNLLSentence(lineList));
                lineList = new LinkedList<CoNllLine>();
                continue;
            }
            lineList.add(new CoNllLine(line.split("\t")));
        }

        return result;
    }
    public static void main(String[] args)
    {
    }
}
