'''
get multiple knowledge embedding (MultiKE) of word in different corpus in terms of corpus_vocab,
the MultiKE of word is the combination of word vector and entity embedding related to this word,
the combination is defined in getMixVec function.

word2id -> id2MultiKE

'''

import numpy as np

def getMixVec(id2vec, id2emb, dim=None, ratio=None):
    '''
    :param id2vec: word id to word vector
    :param id2emb: word id to entity embedding related to word
    :param ratio: the mixture vector equal ratio*id2vec+(1-ratio)*id2emb
    :return: id2Mix the mixture vector combined with word vector and embedding vector
    '''

    if ratio is not None:
        mixvec = ratio * id2vec + (1 - ratio) * id2emb
        return np.array(mixvec)

def getWordWithoutKnowledge(word2representation):
    wordWithoutEmbeddingSet = set()
    vocab_size = word2representation.shape[0]
    dim = word2representation.shape[1]
    zeroVector = np.zeros(shape=(dim))

    for i in range(vocab_size):
        if all(zeroVector==word2representation[i]):
            wordWithoutEmbeddingSet.add(i)
    return wordWithoutEmbeddingSet

project_root = 'D:/Code/java/MultiKEDMM'
data_root = project_root+'/data'
wordEmbeddingModel = 'Word2Vec' # FastText2Vec, Glove2Vec
corpusList = ['StackOverflow']

for corpus in corpusList:
    corpus_root = data_root + '/shortTextCorpus/' + corpus + '/'  # ../data/shortTextCorpus/StackOverflow/
    corpus_vocab = corpus_root + corpus + '_vocab.txt'  # ../data/shortTextCorpus/snippet/StackOverflow_vocab.txt

    word2id = dict()
    id2word = dict()
    wordIdSet = set()

    with open(corpus_vocab,'r') as f:
        words = f.readlines()
        vocab_size = len(words)
        for i in range(vocab_size):
            word = words[i].strip()
            word2id[word] = i
            id2word[i] = word
            wordIdSet.add(i)


    wordVector_file = data_root + '/corpusKnowledgeEmbedding/Vec/' +wordEmbeddingModel+'/'+corpus + '_' + wordEmbeddingModel + '.txt'
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/Vec/Word2Vec/StackOverflow_Word2Vec.txt
    wordEmbed_file = data_root+'/corpusKnowledgeEmbedding/WN2Embed/'+corpus+'_embedding.txt'
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/WN2Embed/StackOverflow_embedding.txt

    id2Vector = np.loadtxt(wordVector_file, dtype=np.float, delimiter=',')
    id2Embed = np.loadtxt(wordEmbed_file, dtype=np.float, delimiter=',')
    dim =  id2Embed.shape[1]

    wNoWordVectorSet = getWordWithoutKnowledge(id2Vector)  # record word without wordEmbedding
    wNoEntityEmbeddingSet = getWordWithoutKnowledge(id2Embed)  # record word without entityEmbedding
    ratioList = [0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9]
    for ratio in ratioList:
        word2MultiKnowledgeEmbed = []
        for i in range(vocab_size):
            if i in wordIdSet.difference(wNoWordVectorSet) and i in wordIdSet.difference(wNoEntityEmbeddingSet):

            # case 1: if a word both has its word vector and its entity embedding, its MultiKE is the combination
                word2MultiKnowledgeEmbed.append(getMixVec(id2Vector[i], id2Embed[i], ratio=ratio))
            elif i in wordIdSet.difference(wNoWordVectorSet) or wordIdSet.difference(wNoEntityEmbeddingSet):

            # case 2: if a word has only its word vector or its entity embedding, its MultiKE is its owned presentation vector
                if i in wordIdSet.difference(wNoWordVectorSet):
                    word2MultiKnowledgeEmbed.append(id2Vector[i])
                else:
                    word2MultiKnowledgeEmbed.append(id2Embed[i])

            else:
            # case 3: if a word has no word vector and its entity embedding, its MultiKE is a zero vector
                word2MultiKnowledgeEmbed.append(np.zeros(shape=(dim)))

        np.savetxt(data_root + '/corpusKnowledgeEmbedding/MultiKE/Multi' + wordEmbeddingModel + '/' + corpus + '_r'+str(ratio)+'MultiKE.txt',
            np.array(word2MultiKnowledgeEmbed), fmt='%1.8f', delimiter=',')
        # .../MultiKEDMM/data/corpusKnowledgeEmbedding/MultiKE/MultiWord2Vec/StackOverflow_r0.1MultiKE.txt


        print('Finished '+str(ratio)+' '+corpus)







