import json
from datetime import datetime, tzinfo
import re

f = open("emails_parsed_0.json")
emails = json.load(f)
dates = []

output_file = open("times.csv", "w")

SONG_START = 1385337600
SECOND_GAP = 4 * 60 * 60
FIRST_GAP = SECOND_GAP / 24

MIN_DATE = 1385466870
MAX_DATE = 1398809693

def time_coord(date):
  bucket = int(1.0 * (date - SONG_START) / SECOND_GAP)
  small_bucket = int(1.0 * (date - SONG_START - bucket * SECOND_GAP) / FIRST_GAP )

  print bucket, small_bucket


def get_list_of_words(mystr):
    return [s.lower() for s in re.sub("[^\w]", " ",  mystr).split()]

def length_email(email):
  return len(get_list_of_words(email['email_text']))

for email in emails[0:15]:
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
  int_date = int(date_object.strftime('%s'))
  if int_date >= MIN_DATE:
    output_file.write("0," + str( length_email(email) ) + "," + str( time_coord(int_date) ) + "\n")
  # print date_object

output_file.close()

# print json.dumps(dates)