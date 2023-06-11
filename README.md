# MultiKE-DMM
MultiKE-DMM is a topic model for short text leveraging multiple knowledge embeddings.   
For more details, please refer to [this paper](https://link.springer.com/chapter/10.1007/978-3-031-30111-7_44).

If you use this package, please cite the paper:  **Jueying He, Jiayao Chen, and Mark Junjie Li. Multi-knowledge Embeddings Enhanced Topic Modeling for Short Texts. Neural Information Processing: 29th International Conference, ICONIP 2022, Part III page 521–532.**


## Reproducing Results

### Quick Reproduction
To quickly reproduce the results in the paper, follow these steps:  

1.Download the data folder from  _(https://drive.google.com/drive/folders/1beiba3-9MusJnvqzGuklng1TApTmb7MR?usp=sharing)_  and replace  _...\MultiKEDMM\data_  with this folder  
2. Decompress all zip files in   _.../MultiKEDMM/data/_ ,  including  _.../MultiKEDMM/data/shortTextCorpus/xxx/word_wiki.zip_  and  _.../MultiKEDMM/data/corpusKnowledgeEmbedding/MultiKESim.zip_   
3.Run  _.../MultiKEDMM/src/MultiKEDMM/resultMultiKEDMM.java_   
4.Evaluate the perfomance of MultiKE-DMM in   _.../MultiKEDMM/result/result.txt_   


### Step-by-Step Reproduction
To reproduce the results in a step-by-step manner, follow these steps:
#### Part 1: Preprocessing
Part 1 is to preprocess two kinds of external knowledge and four Wikipedia index files for four short text datasets.

1: Download the  4,776,093 wikipedia article from  _(http://pan.baidu.com/s/1slaTPoT)_  or  _(https://drive.google.com/open?id=1JTLu-AqNhf7xUWTgKKm8360wT0OQve7O)_   
2.Decompress the articles to   _.../MultiKEDMM/data/wiki_full/ _    
3.Run  _.../MultiKEDMM/src/Util/WordWikiIndex.java_  for each dataset to get the four wikipedia index files  


4.Download the pre-trained entity embedding on WordNet trained by TransE from  _(https://drive.google.com/file/d/1DSK6M2U7GESYLpUqnp0BTJE1sSx6plMv/view?usp=drive_link)_   
5.Run  _.../MultiKEDMM/precessKnowledge/word2EntityEmbedding.py_  to get the entity embedding related to words according to Lesk algorithm  

6.Download the pre-trained word embedding tools including Word2Vec(GoogleNews-vectors-negative300.bin), GloVe(gensim_glove.840B.300d.txt) and FastText(crawl-300d-2M.vec)   
7.Run  _.../MultiKEDMM/precessKnowledge/word2Vector_xxx.py_  to get word vectors   

8.Run  _.../MultiKEDMM/precessKnowledge/word2MultiKnowledgeEmbedding.py_  to construct the multi-knowledge embedding  of words   

9.Run  _.../MultiKEDMM/precessKnowledge/wordSimilarity.py_  to get word cosine similarity based on their word embeddings   

####  Part 2: Training
Part 2 is to obtain the pdz, pzw, pd, and TopWords file for evaluating the performance of MultiKE-DMM.   

1.Run  _.../MultiKEDMM/src/MultiKEDMM/resultMultiKEDMM.java_  after setting the inputs of MultiKE-DMM,  including corpus, the similarity file of this corpus computed by wordSimilarity.py and a number of parameters  
 
2.Get the evaluation of MultiKE-DMM in  _.../MultiKEDMM/result/result.txt_   


## .bib Citation 
@inproceedings{DBLP:conf/iconip/HeCL22,  
  author       = {Jueying He and  
                  Jiayao Chen and  
                  Mark Junjie Li},  
  editor       = {Mohammad Tanveer and  
                  Sonali Agarwal and  
                  Seiichi Ozawa and  
                  Asif Ekbal and  
                  Adam Jatowt},  
  title        = {Multi-knowledge Embeddings Enhanced Topic Modeling for Short Texts},  
  booktitle    = {Neural Information Processing - 29th International Conference, {ICONIP}  
                  2022, Virtual Event, November 22-26, 2022, Proceedings, Part {III}},  
  series       = {Lecture Notes in Computer Science},  
  volume       = {13625},  
  pages        = {521--532},  
  publisher    = {Springer},  
  year         = {2022},  
  url          = {https://doi.org/10.1007/978-3-031-30111-7\_44},  
  doi          = {10.1007/978-3-031-30111-7\_44},  
  timestamp    = {Thu, 20 Apr 2023 15:54:56 +0200},  
  biburl       = {https://dblp.org/rec/conf/iconip/HeCL22.bib},  
  bibsource    = {dblp computer science bibliography, https://dblp.org}  
}  
