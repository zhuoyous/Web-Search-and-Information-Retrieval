from bs4 import BeautifulSoup
from random import randint
from html.parser import HTMLParser
import time
import requests
import json
import csv

USER_AGENT = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36'}


class SearchEngine:
    @staticmethod
    def search(query, sleep=True):
        if sleep:  # Prevents loading too many pages too soon
            time.sleep(randint(5, 10))
        temp_url = '+'.join(query.split())  # for adding + between words for the query
        url = 'http://www.bing.com/search?q=' + temp_url + "&count=30"
        soup = BeautifulSoup(requests.get(url, headers=USER_AGENT).text, "html.parser")
        new_results = SearchEngine.scrape_search_result(soup)
        return new_results

    @staticmethod
    def scrape_search_result(soup):
        raw_results = soup.find_all("li", attrs={"class": "b_algo"})
        results = []
        duli_l = set()
        # implement a check to get only 10 results and also check that URLs must not be duplicated
        for result in raw_results:
            link = result.find('a').get('href')
            if link not in duli_l:
                results.append(link)
                duli_l.add(link)
            if len(results) == 10:
                break
        return results


class task:
    def __main__(self):
        content = []
        results = {}
        url_comp = {}
        t_o = 0
        t_p= 0
        t_r = 0
        data = []
        with open("100QueriesSet1.txt", "r") as f:
            for x in f:
                content.append(x.rstrip("? \n"))
        with open("Google_Result1.json", "r") as f:
            compar = json.load(f)
        for x, y in enumerate(content):
            results[y] = SearchEngine.search(y, True)
        with open("hw1.json", "w") as f:
            json.dump(results, f, indent=4)
        for num, b in enumerate(content):
            url = results[b]
            compa_url = compar[b]
            for i, j in enumerate(url):
                j = self.URL(j)
                url_comp[j] = i
            o = 0
            s = 0
            match = False
            for a, b in enumerate(compa_url):
                b = self.URL(b)
                if b in url_comp:
                    o = o + 1
                    s = s + ((a - url_comp[b]) ** 2)
                    if a == url_comp[b]:
                        match = True
                    else:
                        match = False
            if o == 0:
                r = 0
            elif o == 1:
                if match:
                    r = 1
                else:
                    r = 0
            else:
                r = 1 - ((6 * s) / (o * (o ** 2 - 1)))
            t_o = t_o + o
            t_p = t_p + (o/len(compa_url))
            t_r = t_r + r
            data.append(["Query"+str(num+1), o, (o/len(compa_url))*100, r])
        with open("hw1.csv", "w", newline='') as f:
            csv.writer(f).writerow(["Queries", "Number of Overlapping Results", "Percent Overlap", "Spearman Coefficient"])
            csv.writer(f).writerows(data)
            csv.writer(f).writerow(["Averages", t_o/len(content), (t_p/len(content))*100, t_r/len(content)])
    def URL(self, link):
        link = link.lower().rstrip(" /")
        link = link.replace("www.", "")
        link = link.replace("https://", "")
        link = link.replace("http://", "")
        return link
 
task().__main__()
