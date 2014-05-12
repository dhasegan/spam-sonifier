import json
from datetime import datetime, tzinfo
import re

f = open("emails_parsed_0.json")
emails = json.load(f)

for email in emails:
  date = email['date']
  # print date
  date = re.sub(" \+[0-9]+", "", date)
  date = re.sub(" \-[0-9]+", "", date)
  formats = ['%a, %d %b %Y %H:%M:%S', '%a, %d %b %Y %H:%M', ]
  found_format = False
  for format in formats:
  	try:
  		date_object = datetime.strptime(date, format)
  		found_format = True
  		break
  	except:
  		pass
  if not found_format:
    print "ERROR:", date
  # print date_object
