import json
import re

def get_list_of_words(mystr):
    return [s.lower() for s in re.sub("[^\w]", " ",  mystr).split()]


ALL_WORDS_SUBJECT = {}
ALL_WORDS = {}

emails_file = open("emails_parsed_0.json")
emails = json.load(emails_file)

for email in emails:
    subject_words = get_list_of_words(email['subject'])
    for word in subject_words:
        if not word in ALL_WORDS_SUBJECT:
            ALL_WORDS_SUBJECT[word] = 1
        else:
            ALL_WORDS_SUBJECT[word] += 1

    email_words = get_list_of_words(email['email_text'])
    for word in email_words:
        if not word in ALL_WORDS:
            ALL_WORDS[word] = 1
        else:
            ALL_WORDS[word] += 1

NONUNIQUE_WORDS = []
for k,v in ALL_WORDS.iteritems():
    if v > 1:
        NONUNIQUE_WORDS.append( [k, v] )

NONUNIQUE_WORDS = sorted(NONUNIQUE_WORDS, key=lambda x: x[1], reverse=True)

# print "ALL_WORDS: ", json.dumps(ALL_WORDS)
# print "ALL_WORDS_SUBJECT", json.dumps(ALL_WORDS_SUBJECT)
# print "NONUNIQUE_WORDS: ", json.dumps(NONUNIQUE_WORDS)

f = open("nonunique-words.txt", "w")
for word in NONUNIQUE_WORDS:
    f.write("" + str(word[0]) + "\t\t\t\t\t\t\t\t\t" + str(word[1]) + "\n")
f.close()

# print "len_words", len(ALL_WORDS)
# print "len_words_subject", len(ALL_WORDS_SUBJECT)
# print "len_nonunique", len(NONUNIQUE_WORDS)