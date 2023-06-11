import numpy as np

def cosineSimilarity(a,b):
    '''
        measure the cosine similarity between two vector
    :param a: word embedding of word a
    :param b:  word embedding of word b
    :return: their cosine similarity distance
    '''
    if a.shape != b.shape:
        raise RuntimeError("array {} shape not match {}".format(a.shape, b.shape))
    if a.ndim == 1:
        a_norm = np.linalg.norm(a)
        b_norm = np.linalg.norm(b)
    elif a.ndim == 2:
        a_norm = np.linalg.norm(a, axis=1, keepdims=True)
        b_norm = np.linalg.norm(b, axis=1, keepdims=True)
    else:
        raise RuntimeError("array dimensions {} not right".format(a.ndim))
    if a_norm * b_norm==0:
        similiarity = 0
    else:
        similiarity = np.dot(a, b.T) / (a_norm * b_norm)
        similiarity = 0.5+0.5*similiarity # normalize
    return similiarity


def tanimotoSimilarity(a,b):
    '''
           measure the tanimoto similarity between two vector
       :param a: word embedding of word a
       :param b:  word embedding of word b
       :return: their tanimoto similarity distance
       '''
    if a.shape != b.shape:
        raise RuntimeError("array {} shape not match {}".format(a.shape, b.shape))
    if a.ndim == 1:
        a_norm = np.linalg.norm(a)
        b_norm = np.linalg.norm(b)
    elif a.ndim == 2:
        a_norm = np.linalg.norm(a, axis=1, keepdims=True)
        b_norm = np.linalg.norm(b, axis=1, keepdims=True)
    else:
        raise RuntimeError("array dimensions {} not right".format(a.ndim))
    dot = np.dot(a, b.T)
    mu = a_norm**2+b_norm**2-dot

    if mu==0:
        similiarity = 0
    else:
        similiarity = dot / mu
    return similiarity



project_root = 'D:/Code/java/MultiKEDMM'
data_root = project_root+'/data'
wordEmbeddingModel = 'Word2Vec' # FastText2Vec, Glove2Vec
corpusList = ['StackOverflow']
for corpus in corpusList:
    id2emb_file =data_root + '/corpusKnowledgeEmbedding/MultiKE/Multi' + wordEmbeddingModel + '/' + 'StackOverflow_r0.1MultiKE.txt',
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/MultiKE/MultiWord2Vec/StackOverflow_r0.1MultiKE.txt
    sim_file = data_root + '/corpusKnowledgeEmbedding/MultiKESim/Sim'+ wordEmbeddingModel + '/' +'StackOverflow_r0.1Sim.txt'
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/MultiKESim/SimWord2Vec/StackOverflow_r0.1Sim.txt

    id2emb = np.loadtxt(id2emb_file, dtype=np.float, delimiter=',')
    vocab_size = id2emb.shape[0]
    dim = id2emb.shape[1]
    zeroVec = np.zeros(shape=(dim))
    sim = np.zeros(shape=(vocab_size, vocab_size))
    for i in range(vocab_size):
        if all(id2emb[i] == zeroVec):
            # if a multiple-knowledge embedding of word is a zero-vector,
            # the cosine similarity with other words are all zero.
            continue
        for j in range(i, vocab_size):
            if all(id2emb[j] == zeroVec):
                continue
            sim[i][j] = cosineSimilarity(id2emb[i], id2emb[j])
            sim[j][i] = sim[i][j]

    np.savetxt(sim_file, sim, fmt='%1.8f', delimiter=' ')
    # .../MultiKEDMM/data/corpusKnowledgeEmbedding/MultiKE/MultiWord2Vec/StackOverflow_r0.1MultiKE.txt
    print('Finished ' + corpus )