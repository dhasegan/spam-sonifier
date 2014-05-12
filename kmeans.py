import numpy as np
import mlpy
import json
import sys

NR_CLUSTERS = 5
if len(sys.argv) > 1:
    try:
        NR_CLUSTERS = int(sys.argv[1])
    except:
        pass

emails = np.genfromtxt('email-features.csv', delimiter=',')
cls, means, steps = mlpy.kmeans(emails, k=NR_CLUSTERS, plus=True)

print steps, "steps"
print len(means), "clusters"

emails_file = open("emails_parsed_0.json")
emails = json.load(emails_file)

f = open("classifications.txt", "w")
for i in range(len(emails)):
    f.write( "========== EMAIL ==========\n" )
    f.write("==== CLASS " + str(cls[i]) + "\n")
    f.write("==== subject: " + emails[i]['subject'] + '\n')
    f.write("==== email_text: \n" + (emails[i]['email_text']).encode('utf-8') + '\n')

f.close()