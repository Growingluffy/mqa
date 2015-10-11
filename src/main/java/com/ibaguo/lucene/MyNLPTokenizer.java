package com.ibaguo.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.seg.Segment;
import com.ibaguo.nlp.seg.common.Term;
import com.ibaguo.nlp.seg.common.wrapper.SegmentWrapper;

/**
 * Tokenizer,almost like ansj
 */
public class MyNLPTokenizer extends Tokenizer
{
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);
    private TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    protected SegmentWrapper segment;
    private Set<String> filter;
    private boolean enablePorterStemming;
    private final PorterStemmer stemmer = new PorterStemmer();

    public MyNLPTokenizer(Segment segment, Set<String> filter, boolean enablePorterStemming)
    {
        super();
        this.segment = new SegmentWrapper(new BufferedReader(input), segment);
        this.filter = filter;
        this.enablePorterStemming = enablePorterStemming;
    }

    @Override
    final public boolean incrementToken() throws IOException
    {
        clearAttributes();
        int position = 0;
        Term term;
        boolean un_increased = true;
        do
        {
            term = segment.next();
            if (term == null)
            {
                break;
            }
            if (enablePorterStemming && term.nature == Nature.nx)
            {
                term.word = stemmer.stem(term.word);
            }

            if (filter != null && filter.contains(term.word))
            {
                continue;
            }
            else
            {
                ++position;
                un_increased = false;
            }
        }
        while (un_increased);

        if (term != null)
        {
            positionAttr.setPositionIncrement(position);
            termAtt.setEmpty().append(term.word);
            offsetAtt.setOffset(term.offset, term.offset + term.word.length());
            typeAtt.setType(term.nature == null ? "null" : term.nature.toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void reset() throws IOException
    {
        super.reset();
        segment.reset(new BufferedReader(this.input));
    }

}
