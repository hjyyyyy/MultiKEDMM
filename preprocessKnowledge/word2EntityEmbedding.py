'''
get entity embedding related to word in different corpus in terms of corpus_vocab
word2id -> id2embed
'''

import numpy
from nltk.corpus import wordnet
from nltk.wsd import lesk
import numpy as np

project_root = 'D:/Code/java/MultiKEDMM'
data_root = project_root+'/data'
entityEmbedModel = 'WN2Embed'
corpusList = ['StackOverflow']
for corpus in corpusList:
    corpus_root = data_root + '/shortTextCorpus/'+corpus + '/'  # ../data/shortTextCorpus/StackOverflow/
    corpus_vocab = corpus_root + corpus + '_vocab.txt'  # ../data/shortTextCorpus/snippet/StackOverflow_vocab.txt

    entityEmbedding_root = data_root+'/externalKnowledge/PretrainedEntityEmbeddingOnWordNet/'  # ../data/externalKnowledge/PretrainedEntityEmbeddingOnWordNet/
    entity2id_file = entityEmbedding_root+'entity2id.txt' # ../data/externalKnowledge/PretrainedEntityEmbeddingOnWordNet/entity2id.txt
    entiEmbed_file = entityEmbedding_root+'WNEmbed_300.txt' # entity embedding in WordNet, ../data/externalKnowledge/PretrainedEntityEmbeddingOnWordNet/WNEmbed_300.txt

    word2id = dict()
    id2word = dict()
    entity2id = dict() # entity2id[offset] = id, id is the index of entity defined in file: entity2id

    wordidset = set()
    wordhasEntityEmbeddingSet = set()

    with open(corpus_vocab,'r',encoding='utf-8') as f:
        lines = f.readlines()
        vocab_size = len(lines)
        for i in range(vocab_size):
            word = lines[i].strip()
            wordidset.add(i)
            word2id[word] = i
            id2word[i] = word

    with open(entity2id_file,'r',encoding='utf-8') as f:
        lines = f.readlines()
        lines = lines[1:]
        entity_size = len(lines)
        for i in range(entity_size):
            entity = lines[i].strip().split('\t')
            entity2id[entity[0]] = int(entity[1])

    WNembed = np.loadtxt(entiEmbed_file, dtype=np.float, delimiter=',')
    enti_dim = 300
    word2embed = dict()


    with open(corpus_root+corpus+'_corpusW.txt',encoding='utf-8') as f:
        docs = f.readlines()

    for i in range(len(docs)):
            sent = docs[i].strip().split(' ')
            print(i)
            print(sent)
            for word in sent:
                syn = lesk(sent,word)
                wordid = word2id[word]
                if (syn is not None) and (wordid not in wordhasEntityEmbeddingSet):
                    entityid = wordnet.ss2of(syn).split('-')  # '00001845-n'
                    if entityid[1] == 's':
                        entityid = entityid[0].zfill(8) + 'a'
                    else:
                        entityid = entityid[0].zfill(8) + entityid[1]  # '00001845n'

                    wordhasEntityEmbeddingSet.add(wordid)
                    word2embed[wordid] = WNembed[entity2id[entityid]]

    word2Embed = []
    for i in range(vocab_size):
        if i in wordhasEntityEmbeddingSet:
            word2Embed.append(word2embed[i])
        else:
            word2Embed.append(np.zeros(shape=(300)))
            # set the embedding of a word without related to entity in WordNet as 300-dim zero vector

    np.savetxt(data_root+'/corpusKnowledgeEmbedding/WN2Embed/'+corpus+'_embedding.txt',
               np.array(word2Embed), fmt='%1.8f', delimiter=',')
    #.../MultiKEDMM/data/corpusKnowledgeEmbedding/WN2Embed/StackOverflow_embedding.txt
    print("Finished "+corpus)