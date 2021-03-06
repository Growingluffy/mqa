
package com.ibaguo.nlp.dictionary;


import static com.ibaguo.nlp.utility.Predefine.logger;

import java.io.*;
import java.util.*;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.ibaguo.nlp.collection.trie.DoubleArrayTrie;
import com.ibaguo.nlp.collection.trie.bintrie.BinTrie;
import com.ibaguo.nlp.corpus.io.ByteArray;
import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.dictionary.other.CharTable;
import com.ibaguo.nlp.utility.Predefine;


public class CustomDictionary
{
    
    public static BinTrie<CoreDictionary.Attribute> trie;
    public static DoubleArrayTrie<CoreDictionary.Attribute> dat = new DoubleArrayTrie<CoreDictionary.Attribute>();
    
    public final static String path[] = MyNLP.Config.CustomDictionaryPath;

    // 自动加载词典
    static
    {
        long start = System.currentTimeMillis();
        if (!loadMainDictionary(path[0]))
        {
            logger.warning("自定义词典" + Arrays.toString(path) + "加载失败");
        }
        else
        {
            logger.info("自定义词典加载成功:" + dat.size() + "个词条，耗时" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private static boolean loadMainDictionary(String mainPath)
    {
        logger.info("自定义词典开始加载:" + mainPath);
        if (loadDat(mainPath)) return true;
        TreeMap<String, CoreDictionary.Attribute> map = new TreeMap<String, CoreDictionary.Attribute>();
        try
        {
            for (String p : path)
            {
                Nature defaultNature = Nature.n;
                int cut = p.indexOf(' ');
                if (cut > 0)
                {
                    // 有默认词性
                    String nature = p.substring(cut + 1);
                    p = p.substring(0, cut);
                    try
                    {
                        defaultNature = Nature.valueOf(nature);
                    }
                    catch (Exception e)
                    {
                        logger.severe("配置文件【" + p + "】写错了！" + e);
                        continue;
                    }
                }
                logger.info("加载自定义词典" + p + "中……");
                boolean success = load(p, defaultNature, map);
                if (!success) logger.warning("失败：" + p);
            }
            logger.info("正在构建DoubleArrayTrie……");
            dat.build(map);
            // 缓存成dat文件，下次加载会快很多
            logger.info("正在缓存词典为dat文件……");
            // 缓存值文件
            List<CoreDictionary.Attribute> attributeList = new LinkedList<CoreDictionary.Attribute>();
            for (Map.Entry<String, CoreDictionary.Attribute> entry : map.entrySet())
            {
                attributeList.add(entry.getValue());
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream(mainPath + Predefine.BIN_EXT));
            out.writeInt(attributeList.size());
            for (CoreDictionary.Attribute attribute : attributeList)
            {
                out.writeInt(attribute.totalFrequency);
                out.writeInt(attribute.nature.length);
                for (int i = 0; i < attribute.nature.length; ++i)
                {
                    out.writeInt(attribute.nature[i].ordinal());
                    out.writeInt(attribute.frequency[i]);
                }
            }
            dat.save(out);
            out.close();
        }
        catch (FileNotFoundException e)
        {
            logger.severe("自定义词典" + mainPath + "不存在！" + e);
            return false;
        }
        catch (IOException e)
        {
            logger.severe("自定义词典" + mainPath + "读取错误！" + e);
            return false;
        }
        catch (Exception e)
        {
            logger.warning("自定义词典" + mainPath + "缓存失败！" + e);
        }
        return true;
    }


    
    public static boolean load(String path, Nature defaultNature, TreeMap<String, CoreDictionary.Attribute> map)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] param = line.split("\\s");
                if (param[0].length() == 0) continue;   // 排除空行
                if (MyNLP.Config.Normalization) param[0] = CharTable.convert(param[0]); // 正规化
                if (CoreDictionary.contains(param[0]) || map.containsKey(param[0]))
                {
                    continue;
                }
                int natureCount = (param.length - 1) / 2;
                CoreDictionary.Attribute attribute;
                if (natureCount == 0)
                {
                    attribute = new CoreDictionary.Attribute(defaultNature);
                }
                else
                {
                    attribute = new CoreDictionary.Attribute(natureCount);
                    for (int i = 0; i < natureCount; ++i)
                    {
                        attribute.nature[i] = Enum.valueOf(Nature.class, param[1 + 2 * i]);
                        attribute.frequency[i] = Integer.parseInt(param[2 + 2 * i]);
                        attribute.totalFrequency += attribute.frequency[i];
                    }
                }
                map.put(param[0], attribute);
            }
            br.close();
        }
        catch (Exception e)
        {
            logger.severe("自定义词典" + path + "读取错误！" + e);
            return false;
        }

        return true;
    }

    
    public static boolean add(String word, String natureWithFrequency)
    {
        if (contains(word)) return false;
        return insert(word, natureWithFrequency);
    }

    
    public static boolean add(String word)
    {
        if (MyNLP.Config.Normalization) word = CharTable.convert(word);
        if (contains(word)) return false;
        return insert(word, null);
    }

    
    public static boolean insert(String word, String natureWithFrequency)
    {
        if (word == null) return false;
        if (MyNLP.Config.Normalization) word = CharTable.convert(word);
        CoreDictionary.Attribute att = natureWithFrequency == null ? new CoreDictionary.Attribute(Nature.nz, 1) : CoreDictionary.Attribute.create(natureWithFrequency);
        if (att == null) return false;
        if (dat.set(word, att)) return true;
        if (trie == null) trie = new BinTrie<CoreDictionary.Attribute>();
        trie.put(word, att);
        return true;
    }

    
    public static boolean insert(String word)
    {
        return insert(word, null);
    }

    
    static boolean loadDat(String path)
    {
        try
        {
            ByteArray byteArray = ByteArray.createByteArray(path + Predefine.BIN_EXT);
            int size = byteArray.nextInt();
            CoreDictionary.Attribute[] attributes = new CoreDictionary.Attribute[size];
            final Nature[] natureIndexArray = Nature.values();
            for (int i = 0; i < size; ++i)
            {
                // 第一个是全部频次，第二个是词性个数
                int currentTotalFrequency = byteArray.nextInt();
                int length = byteArray.nextInt();
                attributes[i] = new CoreDictionary.Attribute(length);
                attributes[i].totalFrequency = currentTotalFrequency;
                for (int j = 0; j < length; ++j)
                {
                    attributes[i].nature[j] = natureIndexArray[byteArray.nextInt()];
                    attributes[i].frequency[j] = byteArray.nextInt();
                }
            }
            if (!dat.load(byteArray, attributes) || byteArray.hasMore()) return false;
        }
        catch (Exception e)
        {
            logger.warning("读取失败，问题发生在" + e);
            return false;
        }
        return true;
    }

    
    public static CoreDictionary.Attribute get(String key)
    {
        if (MyNLP.Config.Normalization) key = CharTable.convert(key);
        return trie.get(key);
    }

    
    public static void remove(String key)
    {
        if (MyNLP.Config.Normalization) key = CharTable.convert(key);
        trie.remove(key);
    }

    
    public static LinkedList<Map.Entry<String, CoreDictionary.Attribute>> commonPrefixSearch(String key)
    {
        return trie.commonPrefixSearchWithValue(key);
    }

    
    public static LinkedList<Map.Entry<String, CoreDictionary.Attribute>> commonPrefixSearch(char[] chars, int begin)
    {
        return trie.commonPrefixSearchWithValue(chars, begin);
    }

    public static BaseSearcher getSearcher(String text)
    {
        return new Searcher(text);
    }

    @Override
    public String toString()
    {
        return "CustomDictionary{" +
                "trie=" + trie +
                '}';
    }

    
    public static boolean contains(String key)
    {
        if (dat.exactMatchSearch(key) >= 0) return true;
        return trie != null && trie.containsKey(key);
    }

    
    public static BaseSearcher getSearcher(char[] charArray)
    {
        return new Searcher(charArray);
    }

    static class Searcher extends BaseSearcher<CoreDictionary.Attribute>
    {
        
        int begin;

        private LinkedList<Map.Entry<String, CoreDictionary.Attribute>> entryList;

        protected Searcher(char[] c)
        {
            super(c);
            entryList = new LinkedList<Map.Entry<String, CoreDictionary.Attribute>>();
        }

        protected Searcher(String text)
        {
            super(text);
            entryList = new LinkedList<Map.Entry<String, CoreDictionary.Attribute>>();
        }

        @Override
        public Map.Entry<String, CoreDictionary.Attribute> next()
        {
            // 保证首次调用找到一个词语
            while (entryList.size() == 0 && begin < c.length)
            {
                entryList = trie.commonPrefixSearchWithValue(c, begin);
                ++begin;
            }
            // 之后调用仅在缓存用完的时候调用一次
            if (entryList.size() == 0 && begin < c.length)
            {
                entryList = trie.commonPrefixSearchWithValue(c, begin);
                ++begin;
            }
            if (entryList.size() == 0)
            {
                return null;
            }
            Map.Entry<String, CoreDictionary.Attribute> result = entryList.getFirst();
            entryList.removeFirst();
            offset = begin - 1;
            return result;
        }
    }

    
    public static BinTrie<CoreDictionary.Attribute> getTrie()
    {
        return trie;
    }

    
    public static void parseText(char[] text, AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute> processor)
    {
        if (trie != null)
        {
            BaseSearcher searcher = CustomDictionary.getSearcher(text);
            int offset;
            Map.Entry<String, CoreDictionary.Attribute> entry;
            while ((entry = searcher.next()) != null)
            {
                offset = searcher.getOffset();
                processor.hit(offset, offset + entry.getKey().length(), entry.getValue());
            }
        }
        DoubleArrayTrie<CoreDictionary.Attribute>.Searcher searcher = dat.getSearcher(text, 0);
        while (searcher.next())
        {
            processor.hit(searcher.begin, searcher.begin + searcher.length, searcher.value);
        }
    }
}
