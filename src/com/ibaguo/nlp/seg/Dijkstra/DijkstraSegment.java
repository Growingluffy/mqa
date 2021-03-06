
package com.ibaguo.nlp.seg.Dijkstra;

import java.util.*;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.recognition.nr.JapanesePersonRecognition;
import com.ibaguo.nlp.recognition.nr.PersonRecognition;
import com.ibaguo.nlp.recognition.nr.TranslatedPersonRecognition;
import com.ibaguo.nlp.recognition.ns.PlaceRecognition;
import com.ibaguo.nlp.recognition.nt.OrganizationRecognition;
import com.ibaguo.nlp.seg.WordBasedGenerativeModelSegment;
import com.ibaguo.nlp.seg.Dijkstra.Path.State;
import com.ibaguo.nlp.seg.common.*;


public class DijkstraSegment extends WordBasedGenerativeModelSegment
{
    @Override
    public List<Term> segSentence(char[] sentence)
    {
        WordNet wordNetOptimum = new WordNet(sentence);
        WordNet wordNetAll = new WordNet(wordNetOptimum.charArray);
        ////////////////生成词网////////////////////
        GenerateWordNet(wordNetAll);
        ///////////////生成词图////////////////////
        Graph graph = GenerateBiGraph(wordNetAll);
        if (MyNLP.Config.DEBUG)
        {
            System.out.printf("粗分词图：%s\n", graph.printByTo());
        }
        List<Vertex> vertexList = dijkstra(graph);
//        fixResultByRule(vertexList);

        if (config.useCustomDictionary)
        {
            combineByCustomDictionary(vertexList);
        }

        if (MyNLP.Config.DEBUG)
        {
            System.out.println("粗分结果" + convert(vertexList, false));
        }

        // 数字识别
        if (config.numberQuantifierRecognize)
        {
            mergeNumberQuantifier(vertexList, wordNetAll, config);
        }

        // 实体命名识别
        if (config.ner)
        {
            wordNetOptimum.addAll(vertexList);
            int preSize = wordNetOptimum.size();
            if (config.nameRecognize)
            {
                PersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.translatedNameRecognize)
            {
                TranslatedPersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.japaneseNameRecognize)
            {
                JapanesePersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.placeRecognize)
            {
                PlaceRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.organizationRecognize)
            {
                // 层叠隐马模型——生成输出作为下一级隐马输入
                graph = GenerateBiGraph(wordNetOptimum);
                vertexList = dijkstra(graph);
                wordNetOptimum.clear();
                wordNetOptimum.addAll(vertexList);
                preSize = wordNetOptimum.size();
                OrganizationRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (wordNetOptimum.size() != preSize)
            {
                graph = GenerateBiGraph(wordNetOptimum);
                vertexList = dijkstra(graph);
                if (MyNLP.Config.DEBUG)
                {
                    System.out.printf("细分词网：\n%s\n", wordNetOptimum);
                    System.out.printf("细分词图：%s\n", graph.printByTo());
                }
            }
        }

        // 如果是索引模式则全切分
        if (config.indexMode)
        {
            return decorateResultForIndexMode(vertexList, wordNetAll);
        }

        // 是否标注词性
        if (config.speechTagging)
        {
            speechTagging(vertexList);
        }

        return convert(vertexList, config.offset);
    }

    
    private static List<Vertex> dijkstra(Graph graph)
    {
        List<Vertex> resultList = new LinkedList<Vertex>();
        Vertex[] vertexes = graph.getVertexes();
        List<EdgeFrom>[] edgesTo = graph.getEdgesTo();
        double[] d = new double[vertexes.length];
        Arrays.fill(d, Double.MAX_VALUE);
        d[d.length - 1] = 0;
        int[] path = new int[vertexes.length];
        Arrays.fill(path, -1);
        PriorityQueue<State> que = new PriorityQueue<State>();
        que.add(new State(0, vertexes.length - 1));
        while (!que.isEmpty())
        {
            State p = que.poll();
            if (d[p.vertex] < p.cost) continue;
            for (EdgeFrom edgeFrom : edgesTo[p.vertex])
            {
                if (d[edgeFrom.from] > d[p.vertex] + edgeFrom.weight)
                {
                    d[edgeFrom.from] = d[p.vertex] + edgeFrom.weight;
                    que.add(new State(d[edgeFrom.from], edgeFrom.from));
                    path[edgeFrom.from] = p.vertex;
                }
            }
        }
        for (int t = 0; t != -1; t = path[t])
        {
            resultList.add(vertexes[t]);
        }
        return resultList;
    }

}
