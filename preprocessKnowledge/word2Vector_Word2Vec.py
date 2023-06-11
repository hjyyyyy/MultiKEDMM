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
wordEmbeddingModel = 'Word2Vec'
output_file = data_root+'/externalKnowledge/PretrainedWordVector/GoogleNews-vectors-negative300.bin'
model = gensim.models.KeyedVectors.load_word2vec_format(output_file,binary=True)

# GloVe -> gensim_glove.840B.300d.txt
# FastText  -> crawl-300d-2M.vec

corpusList = ['StackOverflow']
for corpus in corpusList:
    corpus_root = data_root + '/shortTextCorpus/' + corpus + '/'  # .../MultiKEDMM/data/shortTextCorpus/StackOverflow/
    corpus_vocab = corpus_root + corpus + '_vocab.txt'  # .../MultiKEDMM/data/shortTextCorpus/snippet/StackOverflow_vocab.txt

    wordidSet = set()
    word2id = dict()
    id2word = dict()

    hasWordVectorSet = set()  # record word with wordVector
    word2vec = [] # record word vector

    with open(corpus_vocab,'r',encoding='utf-8') as f:
        lines = f.readlines()
        vocab_size = len(lines)
        for i in range(vocab_size):
            word = lines[i].strip()
            wordidSet.add(i)

            word2id[word] = i
            id2word[i] = word

            if word in model.vocab:  # case 1
                hasWordVectorSet.add(i)
                word2vec.append(model[word])

            elif word.upper() in model: # case 2 usa->USA
                hasWordVectorSet.add(i)
                word = word.upper()
                word2vec.append(model[word])

            elif word.capitalize() in model.vocab: # case 3 usa->USA
                hasWordVectorSet.add(i)
                word = word.capitalize()
                word2vec.append(model[word])
            else:
                word_list = list(word.capitalize())  # case 4 upenn -> UPenn
                char_index = lowercase.find(word_list[1])
                word_list[1] = uppercase[char_index]
                if ''.join(word_list) in model.vocab:
                    hasWordVectorSet.add(i)
                    word = ''.join(word_list)
                    word2vec.append(model[word])
                    continue

                word_list = list(word)  # case 5 eserver  -> eServer
                char_index = lowercase.find(word_list[1])
                word_list[1] = uppercase[char_index]
                if ''.join(word_list) in model.vocab:
                    hasWordVectorSet.add(i)
                    word = ''.join(word_list)
                    word2vec.append(model[word])
                    continue

                word2vec.append(np.zeros(shape=(vec_dim))) # case 6 word without vector

    #  data_root = 'D:/Code/java/MultiKEDMM/data'
    np.savetxt(data_root + '/corpusKnowledgeEmbedding/Vec/' +wordEmbeddingModel+'/'+corpus + '_' + wordEmbeddingModel + '.txt',
               np.array(word2vec), fmt='%1.8f', delimiter=',')
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/Vec/Word2Vec/StackOverflow_Word2Vec.txt

    print('Finished ' + corpus)



