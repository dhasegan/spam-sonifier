import json
import sys

emails_file = open("emails_parsed_0.json")
emails = json.load(emails_file)

f = open("categories.txt", "w")

for email in emails:

    sys.stdout.write("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n========== EMAIL ==========\n")
    sys.stdout.write("==== sender: " + email['sender'] + '\n')
    sys.stdout.write("==== email_list: " + email['email_list'] + '\n')
    sys.stdout.write("==== date: " + email['date'] + '\n')
    sys.stdout.write("==== subject: " + email['subject'] + '\n')
    sys.stdout.write("==== email_text: \n" + (email['email_text']).encode('utf-8') + '\n')

    category = -1
    while True:
        try:
            category = int(raw_input(''))
            break
        except ValueError:
            print "Not a number"
    f.write(str(category) + "\n")

f.close()