import re
import json

def is_email_valid(context):
    if not "jacobs-university.de" in context['email_list']:
        return False

    # Delete the encrypted code inside the email text
    found_enc_block = False
    in_enc_block = False
    new_email_text = ""
    for row in context['email_text'].split("\n"):
        if not " " in row and (in_enc_block or len(row) == 76):
            in_enc_block = True
            found_enc_block = True
        else:
            new_email_text += row + "\n"
    if found_enc_block:
        context['email_text'] = new_email_text

    if not context['sender'] or not context['email_list'] or not context['date'] or \
        not context['subject'] or not context['email_text']:
            return False

    return True

def delete_footer(email):
    sep = "________________________________"
    return email.split(sep, 1)[0]

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
                    email_text = email_text + row.replace("=\n", "")
                else:
                    if row == "\n":
                        headers = False
                    else:
                        pass
            else:
                plain_text = False
    context = {
        'sender': sender, 
        'email_list': email_list,
        'subject': subject,
        'date': date,
        'email_text': delete_footer(email_text)
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


detailed_output_file = open("emails_detailed.txt", "w")
for email in emails:
    detailed_output_file.write( "========== EMAIL ==========\n\n" )
    detailed_output_file.write("==== sender: " + email['sender'] + '\n')
    detailed_output_file.write("==== email_list: " + email['email_list'] + '\n')
    detailed_output_file.write("==== date: " + email['date'] + '\n')
    detailed_output_file.write("==== subject: " + email['subject'] + '\n')
    detailed_output_file.write("==== email_text: \n" + email['email_text'] + '\n')

detailed_output_file.close()
