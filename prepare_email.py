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

# Get nonunique words
NONUNIQUE_WORDS = []
for k,v in ALL_WORDS.iteritems():
    if v > 2:
        NONUNIQUE_WORDS.append( [k, v] )

NONUNIQUE_WORDS = sorted(NONUNIQUE_WORDS, key=lambda x: x[1], reverse=True)


NONUNIQUE_WORDS_SUBJECT = []
for k,v in ALL_WORDS_SUBJECT.iteritems():
    if v > 2:
        NONUNIQUE_WORDS_SUBJECT.append( [k, v] )

NONUNIQUE_WORDS_SUBJECT = sorted(NONUNIQUE_WORDS_SUBJECT, key=lambda x: x[1], reverse=True)

# Print the files
f = open("nonunique-words.txt", "w")
for word in NONUNIQUE_WORDS:
    f.write("" + str(word[0]) + "\t\t\t\t\t\t\t\t\t" + str(word[1]) + "\n")
f.close()

f = open("nonunique-words-subject.txt", "w")
for word in NONUNIQUE_WORDS_SUBJECT:
    f.write("" + str(word[0]) + "\t\t\t\t\t\t\t\t\t" + str(word[1]) + "\n")
f.close()


# For each email assign a feature vector as a number from 0-100 that is the percentage of words of that type in the whole email/subject
features = open("email-features.csv", "w")
for email in emails:
    email_words = get_list_of_words(email['email_text'])
    email_len = len(email_words)
    subject_words = get_list_of_words(email['subject'])
    subject_len = len(subject_words)
    if email_len == 0 or subject_len == 0:
        continue
    for word in NONUNIQUE_WORDS:
        cnt = email_words.count(word[0])
        features.write( str(1.0 * cnt / email_len) + "," )
    for word in NONUNIQUE_WORDS_SUBJECT:
        cnt = subject_words.count(word[0])
        features.write( str(1.0 * cnt / subject_len) + "," )
    features.write( str(email_len) + "," + str(subject_len) + "\n")

features.close()



# print "ALL_WORDS: ", json.dumps(ALL_WORDS)
# print "ALL_WORDS_SUBJECT", json.dumps(ALL_WORDS_SUBJECT)
# print "NONUNIQUE_WORDS: ", json.dumps(NONUNIQUE_WORDS)
# print "len_words", len(ALL_WORDS)
# print "len_words_subject", len(ALL_WORDS_SUBJECT)
# print "len_nonunique", len(NONUNIQUE_WORDS)