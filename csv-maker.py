import json
from datetime import datetime, tzinfo
import re

f = open("emails_parsed_0.json")
emails = json.load(f)
dates = []

cats = []
category_file = open("categories.txt")
for cat in category_file:
  cats.append(cat.replace("\n", ""))

output_file = open("times.csv", "w")

SONG_START = 1385337600
SECOND_GAP = 4 * 60 * 60
FIRST_GAP = SECOND_GAP / 24

MIN_DATE = 1385466870
MAX_DATE = 1398809693

def time_coord(ALLDATES, date):
  bucket = int(1.0 * (date - SONG_START) / SECOND_GAP)
  small_bucket = int(1.0 * (date - SONG_START - bucket * SECOND_GAP) / FIRST_GAP )
  # print bucket, small_bucket

  dt = small_bucket + bucket * 24
  while dt in ALLDATES:
    dt += 1
  return dt


def get_list_of_words(mystr):
    return [s.lower() for s in re.sub("[^\w]", " ",  mystr).split()]

def length_email(email):
  return len(get_list_of_words(email['email_text']))

ALLDATES = {}

for i in range(len(emails)):
  email = emails[i]
  category = cats[i]
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
  # print int_date >= MIN_DATE
  if int_date >= MIN_DATE:
    tc = time_coord(ALLDATES, int_date)
    ALLDATES[tc] = 1
    output_file.write(str(category) + "," + str( int(1.0 * length_email(email) / 100) + 1 ) + "," + str(tc) + "\n")
  # print date_object

output_file.close()

# print json.dumps(dates)