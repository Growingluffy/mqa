
package com.ibaguo.nlp.dictionary.common;


import static com.ibaguo.nlp.utility.Predefine.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.ibaguo.nlp.collection.trie.bintrie.BinTrie;
import com.ibaguo.nlp.utility.Predefine;


public class CommonStringDictionary
{
    BinTrie<Byte> trie;
    public boolean load(String path)
    {
        trie = new BinTrie<Byte>();
        if (loadDat(path + Predefine.TRIE_EXT)) return true;
        String line = null;
        try
        {
            BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((line = bw.readLine()) != null)
            {
                trie.put(line, null);
            }
            bw.close();
        }
        catch (Exception e)
        {
            logger.warning("加载" + path + "失败，" + e);
        }
        if (!trie.save(path + Predefine.TRIE_EXT)) logger.warning("缓存" + path + Predefine.TRIE_EXT + "失败");
        return true;
    }

    boolean loadDat(String path)
    {
        return trie.load(path);
    }

    public Set<String> keySet()
    {
        Set<String> keySet = new LinkedHashSet<String>();
        for (Map.Entry<String, Byte> entry : trie.entrySet())
        {
            keySet.add(entry.getKey());
        }

        return keySet;
    }
}
