import csv
import re
import os
from bs4 import BeautifulSoup

# List of all websites
site_list = []
with open("curriculum.csv","r") as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
        site_list.append(row[1])
    del site_list[0]
print("***partial URLs***")
print(site_list)
base = "https://luther.edu/catalog/curriculum/"
url_list = [base + url for url in site_list]
print("***full URLs***")
print(url_list)

courses_list = []
for url in range(len(url_list)):
    os.system(f"wget -P Website {url_list[url]}")

    
    with open(f"Website/{site_list[url]}") as infile:
        soup = BeautifulSoup(infile, features="html.parser")
        for header in soup.find_all('h4'):
            courses_list.append(str(header))
   

with open("Website/curriculum.csv","a+") as csvFile:
    for course in courses_list:
        curse = course[59:-13]
        curses = curse.split("</span>")
        curses[1] = curses[1][27:]
        print(curses)
        writer = csv.writer(csvFile)
        writer.writerow(curses)

