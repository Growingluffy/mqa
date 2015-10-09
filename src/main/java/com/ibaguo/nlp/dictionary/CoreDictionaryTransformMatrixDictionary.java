
package com.ibaguo.nlp.dictionary;

import static com.ibaguo.nlp.utility.Predefine.logger;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.corpus.tag.Nature;


public class CoreDictionaryTransformMatrixDictionary
{
    public static TransformMatrixDictionary<Nature> transformMatrixDictionary;
    static
    {
        transformMatrixDictionary = new TransformMatrixDictionary<Nature>(Nature.class);
        long start = System.currentTimeMillis();
        if (!transformMatrixDictionary.load(MyNLP.Config.CoreDictionaryTransformMatrixDictionaryPath))
        {
            System.err.println("加载核心词典词性转移矩阵" + MyNLP.Config.CoreDictionaryTransformMatrixDictionaryPath + "失败");
            System.exit(-1);
        }
        else
        {
            logger.info("加载核心词典词性转移矩阵" + MyNLP.Config.CoreDictionaryTransformMatrixDictionaryPath + "成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
        }
    }
}
