import re
import json

def is_email_valid(context):
    if not context['sender'] or not context['email_list'] or not context['date'] or \
        not context['subject'] or not context['email_text']:
            return False

    if not "jacobs-university.de" in context['email_list']:
        return False

    return True

def parse_email(email_rows):
    email_details = {}

    # Find Sender
    sender = None
    for row in email_rows:
        if re.match('^From: ', row):
            sender = row.replace("From: ", "").replace("\n", "")

    # Find Email List
    email_list = ""
    start_counting = False
    for row in email_rows:
        if re.match('^To: ', row):
            email_list = row.replace("To: ", "")
            start_counting = True
        else:
            if start_counting and re.match("^\t", row):
                email_list = email_list + row.replace("\n", "")
            else:
                start_counting = False

    # Get Subject
    subject = None
    for row in email_rows:
        if re.match('^Subject: ', row):
            subject = row.replace("Subject: ", "").replace("\n", "")

    # Get Date
    date = None
    for row in email_rows:
        if re.match('^Date: ', row):
            date = row.replace("Date: ", "").replace("\n", "")

    # Get text
    email_text = ""
    plain_text = False
    quoted_printable = False
    for row in email_rows:
        if re.match('^Content-Type: text/plain; ', row):
            plain_text = True
            headers = True
        else:
            if plain_text and not re.match("^--", row):
                if not headers:
                    email_text = email_text + row
                else:
                    if row == "\n":
                        headers = False
                    else:
                        pass
            else:
                plain_text = False

    # print "========== EMAIL =========="
    # print "sender: ", sender
    # print "email_list: ", email_list
    # print "date: ",  date
    # print "subject: ", subject
    # print "email_text: ", email_text
    context = {
        'sender': sender, 
        'email_list': email_list,
        'subject': subject,
        'date': date,
        'email_text': email_text
    }

    if is_email_valid(context):
        return context
    return {}


def parse_all_emails(emails_text_filename):
    emails_file = open(emails_text_filename)

    current_email = []
    emails = []
    for row in emails_file:
        if "EMAIL #" in row:
            parsed_email = parse_email(current_email)
            if parsed_email:
                emails.append( parsed_email )
            current_email = []
        current_email.append(row)

    parsed_email = parse_email(current_email)
    if parsed_email:
        emails.append( parsed_email )
    return emails


emails = []
email_filenames = ["emails-1-1000.txt", "emails-2000-3000.txt", "emails-3000-4000.txt", "emails-4000-5000.txt", "emails-5000-6000.txt", "emails-6000-6900.txt"]
# email_filenames = ["emails-first-15.txt"]

for email_filename in email_filenames:
    emails = emails + parse_all_emails(email_filename)

print len(emails)

output_file = open("emails_parsed_0.json", "w")
output_file.write( json.dumps(emails) )
output_file.close()
