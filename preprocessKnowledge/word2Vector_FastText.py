'''
get word vector in different corpus in terms of corpus_vocab while using the pre-trained tool Word2Vec
word2id -> id2vec
'''

import gensim
import numpy as np
import string
lowercase = string.ascii_lowercase
uppercase = string.ascii_uppercase
project_root = 'D:/Code/java/MultiKEDMM'
data_root = project_root+'/data'

vec_dim = 300
wordEmbeddingModel = 'FastText2Vec'
output_file = data_root+'/externalKnowledge/PretrainedWordVector/crawl-300d-2M.vec'
f = open(output_file,encoding='utf=8')
lines = f.readlines()
model = {}

for line in lines:
    values = line.rstrip().rsplit(' ')
    word = values[0]
    embedd = np.asarray(values[1:],dtype=np.float)
    model[word] = embedd
f.close()


# GloVe -> gensim_glove.840B.300d.txt
# FastText  -> crawl-300d-2M.vec

corpusList = ['StackOverflow']
for corpus in corpusList:
    corpus_root = data_root + '/shortTextCorpus/' + corpus + '/'  # ../MultiKEDMM/data/shortTextCorpus/StackOverflow/
    corpus_vocab = corpus_root + corpus + '_vocab.txt'  # ../MultiKEDMM/data/shortTextCorpus/snippet/StackOverflow_vocab.txt

    wordidSet = set()
    word2id = dict()
    id2word = dict()

    hasWordVectorSet = set()  # record word with wordVector
    word2vec = [] # record word vector

    with open(corpus_vocab, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        vocab_size = len(lines)
        for i in range(vocab_size):
            word = lines[i].strip()
            wordidSet.add(i)

            word2id[word] = i
            id2word[i] = word

            if word in model.keys():  # case 1
                hasWordVectorSet.add(i)
                word2vec.append(model[word])
            else:
                word2vec.append(np.zeros(shape=(vec_dim)))  # case 2 word without vector


    np.savetxt(data_root + '/corpusKnowledgeEmbedding/Vec/' +wordEmbeddingModel+'/'+corpus + '_' + wordEmbeddingModel + '.txt',
               np.array(word2vec), fmt='%1.8f', delimiter=',')
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/Vec/FastText2Vec/StackOverflow_FastText2Vec.txt
    print('Finished ' + corpus)



