# Web Search, Crawling, and Indexing Projects

This repository contains four homework assignments from the CSCI572 course, covering a range of topics in Information Retrieval and Web Search technologies. The work spans Python, Java, and Hadoop-like paradigms and demonstrates practical skills in search engine simulation, web crawling, text indexing, and big data processing.

---

##  Folder Overview

### `hw1/` — **Bing Search Result Scraper (Python)**
Simulates a search engine client by sending queries to Bing and scraping the top results.

**Files:**
- `hw1.py` – Uses BeautifulSoup and requests to send search queries and parse result snippets.
- `hw1.csv` – Stores the extracted URLs and titles in CSV format.
- `hw1.json` – Same data as JSON.
- `hw1.txt` – Description or summary of the search results.

> **Purpose:** Practice extracting and storing structured search engine result page (SERP) data.

---

### `hw2/` — **Web Crawler for Fox News (Java + Python)**
Builds a focused web crawler using the Crawler4j framework to crawl `www.foxnews.com`.

**Files:**
- `Controller.java` – Main configuration and launcher for the crawl.
- `MyCrawler.java` – Handles the fetch and visit logic, URL filters, and logging.
- `fetch_foxnews.csv` – Logs fetch attempts and HTTP status codes.
- `visit_foxnews.csv` – Logs visited pages with metadata such as size and outlinks.
- `CrawlReport_foxnews.txt` – Summary of crawling behavior.
- `report.ipynb` – Jupyter Notebook analyzing fetch/visit success rates and page characteristics.

> **Purpose:** Explore the structure and ethics of web crawling and analyze large-scale crawl data.

---

### `hw3/` — **Unigram and Bigram Indexing (Java)**
Implements search engine back-end components by creating unigram and bigram inverted indexes.

**Files:**
- `unigram_code.java` – Builds unigram inverted index from a text corpus.
- `bigram_code.java` – Builds bigram inverted index (pairs of words).
- `unigram_index.txt` – Output of the unigram indexing process.
- `selected_bigram_index.txt` – Sample output of bigram indexing.
- `unigram_output_folder_screenshot.PNG` – Screenshot showing unigram index structure.
- `bigram_output_folder_screenshot.PNG` – Screenshot showing bigram output structure.

> **Purpose:** Understand core IR concepts like tokenization, indexing, and dictionary construction.

---

### `hw4/` — **MapReduce Word Count (Conceptual)**
A readme explaining a Hadoop-style MapReduce word count job.

**Files:**
- `readme.txt` – Describes how the word count logic would be implemented and executed in a distributed environment.

> **Purpose:** Understand the MapReduce programming model and its use in big data processing.

---

##  Technologies Used

- **Python**: Web scraping, data storage, data analysis
- **Java**: Crawler4j for web crawling; custom indexing logic
- **Jupyter Notebook**: Data exploration and crawl log analysis
- **IR Concepts**: Inverted indexing, tokenization, bigrams/unigrams
- **Big Data**: MapReduce programming model (conceptual)

---

##  Summary

Each assignment simulates real-world tasks in information retrieval, combining software development with data science and distributed computing concepts.
